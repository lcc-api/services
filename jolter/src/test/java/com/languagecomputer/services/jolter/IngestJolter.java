package com.languagecomputer.services.jolter;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.docprocess.*;
import com.languagecomputer.services.examplestore.ExampleUpdateStrategy;
import com.languagecomputer.services.job.JobPropertyKey;
import com.languagecomputer.services.job.JobService;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.job.JobState;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class processes documents, waits for them to complete, and runs the jolter model on them
 *
 *
 */
public class IngestJolter extends CommandLineUtils.ServiceArgs implements Runnable {
  public static final String DOCUMENT_PROCESSING_SERVICE = "DOCUMENT_PROCESSING";
  @CommandLine.Option(
      names={"--type", "--docType", "--documentType"},
      description = {"Type of document being uploaded.",
          "Options: ${COMPLETION-CANDIDATES}"
      },
      arity = "1")
  DocumentType type = null;

  @CommandLine.Option(
      names={"--path", "--paths", "--file", "--files", "--directory"},
      arity = "1..*",
      paramLabel = "file/dir",
      description = { "files to upload", "if a directory is given it will be searched recursively."
  })
  List<String> fileNames = new LinkedList<>();

  @CommandLine.Option(
      names = "--model-name",
      description = {"the name to give the model being created."},
      required = true)
  String modelName = null;

  @CommandLine.Option(
      names = "--model-type",
      description = {
          "The type of jolter model to create.",
          "Default: " + CommandLineUtils.DEFAULT_VALUE,
          "Possible Values: " + CommandLineUtils.ENUM_VALUES
      })
  StartJolterBuild.ModelType modelType = StartJolterBuild.ModelType.JOINT_LM_PREDICATE;

  @Override
  public void run() {
    boolean found = new LCCServiceInfo(this).ensureAvailable("JOLTER");
    if(!found) {
      SampleOutput.outputErr("Cannot find jolter service, exiting thread");
      return;
    }
    try {
      Set<String> documentIds = createDocuments();
      if (documentIds.isEmpty()) {
        SampleOutput.println("No documents successfully processed.  Jolter model will not be built.  Exiting.");
        return;
      }
      buildModel(documentIds);
    } catch(IOException e) {
      SampleOutput.outputErr("IOException processing jolter");
      SampleOutput.outputException(e);
    }
  }

  private Set<String> createDocuments() throws IOException {
    DocumentProcessingService client = RestClients.create(configURL, DocumentProcessingService.class, 20, TimeUnit.MINUTES);
    final DocumentProcessingLevel level = new DocumentProcessingLevel(DocumentProcessingLevel.Level.EXAMPLE, ExampleUpdateStrategy.ADD_OR_UPDATE_ALL);
    DocumentProcessingDriver driver = new DocumentProcessingDriver(type, client, level);
    final Set<String> documentIds = new HashSet<>();
    Iterable<File> files = Iterables.transform(fileNames, (s) -> new File(s));
    Iterable<DocumentJob> jobs = DocumentProcessingUtils.getMessages(files, null, type, level);
    Set<Long> jobids = new HashSet<>();
    for(DocumentJob job : jobs) {
      driver.process(job);
      jobids.add(job.getJobId());
    }
    driver.waitForJobs(jobids, statusDetail -> updatePrinter(statusDetail, documentIds), 20L);
    SampleOutput.println("");
    return documentIds;
  }

  private void updatePrinter(DocumentProcessingStatusDetail statusDetail, Set<String> documentIds) {
    // add relevant document ids
    documentIds.addAll(statusDetail.getStatuses().stream()
        .filter(status -> status.getStatus() != null && status.getStatus().isSuccessfullyFinished())
        .map(DocumentStatus::getDocID).collect(Collectors.toList()));

    //get finished document statuses
    long un = statusDetail.getStatuses().stream().filter(s -> s.getStatus() != null && s.getStatus().isUnsuccessfullyFinished()).count();
    long suc = statusDetail.getStatuses().stream().filter(s -> s.getStatus() != null && s.getStatus().isSuccessfullyFinished()).count();

    //get unfinished document statuses
    final Supplier<Stream<DocumentStatus>> unfinishedStatuses = () ->
        statusDetail.getStatuses().stream().filter(s -> s.getStatus() != null && !s.getStatus().isFinished());
    final long processing = unfinishedStatuses.get().count();

    //create string detailing unfinished document statuses
    String processingStatus = "("+ unfinishedStatuses.get()
        .map(DocumentStatus::getStatus)
        .collect(Collectors.groupingBy(DocumentJobStatus::getCategory))
        .entrySet().stream()
        .map(entry -> entry.getKey() + ": " + entry.getValue().size())
        .collect(Collectors.joining(", ")) +")";

    long tot = statusDetail.getStatuses().stream().filter(s -> s.getStatus() != null).count();

    //clear terminal line to print in place
    SampleOutput.print("\33[2K\r");

    //print statuses
    SampleOutput.printF("%d/%d documents finished.", (suc+un), tot);
    if (un > 0){
      SampleOutput.printF("  %d failed.", un);
    }
    if (unfinishedStatuses.get().count() > 0){
      SampleOutput.printF("  %d processing %s.", processing, processingStatus);
    }
  }

  private void buildModel(Set<String> documentIds) throws IOException {
    // build jolter model
    LCCServiceInfo serviceInfo = new LCCServiceInfo(this);
    final StartJolterBuild startJolterBuild = new StartJolterBuild(
        serviceInfo.getURIForService("JOLTER"),
        modelName,
        modelType);
    final JobService jobService =
        RestClients.create(serviceInfo.getURIForService("JOBS_SERVICE"), JobService.class);

    Job job = startJolterBuild.run(documentIds);
    final Long startTime = Instant.now().getEpochSecond();
    SampleOutput.printF("Sent %d documents to jolter", documentIds.size());
    while (!job.getState().isFinished()){
      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
      job = jobService.getJob(job.getId());
      SampleOutput.print("\33[2K\r");
      SampleOutput.printF("%s | %.2f%% complete", job.getName(), job.getProgress());
      if (job.hasProp(JobPropertyKey.STATUS_DESCRIPTION)){
        SampleOutput.printF(" | %s ", job.getProp(JobPropertyKey.STATUS_DESCRIPTION));
      }
      SampleOutput.printF(" | %d s", Instant.now().getEpochSecond() - startTime);
    }
    SampleOutput.println("");
    if (JobState.FAILED.equals(job.getState()) || JobState.COMPLETED_WITH_FAILURES.equals(job.getState())) {
      SampleOutput.println("Jolter failed to build model.");
      SampleOutput.printF("Failure reason: %s", job.getProp(JobPropertyKey.STATUS_DESCRIPTION));
    } else {
      SampleOutput.println("Jolter completed building model.");
    }
    SampleOutput.println("");
  }

  public static void main(String[] rawArgs) {
    CommandLineUtils.parseArgs(new IngestJolter(), rawArgs).run();
  }
}
