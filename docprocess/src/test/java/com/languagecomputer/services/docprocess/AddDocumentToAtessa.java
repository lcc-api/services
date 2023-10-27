package com.languagecomputer.services.docprocess;

import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.examplestore.ExampleUpdateStrategy;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import javax.annotation.Nullable;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.languagecomputer.services.docprocess.DocumentProcessingDriver.DEFAULT_JOB_NAME;
import static com.languagecomputer.services.docprocess.DocumentProcessingUtils.getDocumentJob;
import static com.languagecomputer.services.docprocess.DocumentProcessingUtils.getFilesRecursive;

/**
 * This class shows how to add a document(s) to ATESSA.
 * There is no immediate response, but you can check the corpus stats on the ATESSA UI to see the progress.
 * @author smonahan
 */
public class AddDocumentToAtessa {
  private static final String DOCUMENT_PROCESSING_ENDPOINT = "/api/documentProcessing";
  public static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Parameters(index = "0", description = {"Type of document being uploaded.", "Options: ${COMPLETION-CANDIDATES}"}, arity = "1")
    DocumentType type = null;

    @CommandLine.Parameters(index = "1..*", arity = "1..*", paramLabel = "file/dir", description = { "files to upload",
        "if a directory is given it will be searched recursively."
    })
    List<String> files = new ArrayList<>();

    @CommandLine.Option(names = "--level", description = {"level of document processing to do",
        "Default: ${DEFAULT-VALUE}",
        "Options: ${COMPLETION-CANDIDATES}"
    })
    DocumentProcessingLevel.Level processLevel = DocumentProcessingLevel.Level.EXAMPLE;

    @CommandLine.Option(names = "--trackFiles", description = {"track the results of the each file, querying every 5 seconds"})
    boolean trackFiles = false;

    @CommandLine.Option(names = "--jobName", description = {"the name of the job we are creating"})
    String jobName = DEFAULT_JOB_NAME;

    @CommandLine.Option(names = "--limit", description = {"limit the number of documents that can be uploaded", "by default, there is no limit."})
    Integer limit = null;

    @CommandLine.Option(names = "--stages", description = {"comma separated list of stages to run"})
    String stages = null;
  }

  final DocumentProcessingDriver driver;

  public AddDocumentToAtessa(URI configURI, DocumentType type, DocumentProcessingLevel.Level processLevel) {
    driver = new DocumentProcessingDriver(
        type,
        RestClients.create(UriBuilder.fromUri(configURI).replacePath(DOCUMENT_PROCESSING_ENDPOINT).build(), DocumentProcessingService.class, 20, TimeUnit.MINUTES),
        new DocumentProcessingLevel(processLevel, processLevel == DocumentProcessingLevel.Level.EXAMPLE ? ExampleUpdateStrategy.ADD_OR_UPDATE_ALL : ExampleUpdateStrategy.NONE)
    );
  }

  protected DocumentProcessingDriver getDriver() {
    return driver;
  }

  public List<DocumentJob> process(Collection<String> paths, String batchId) throws IOException {
    return process(paths, batchId, null, null);
  }

  protected List<DocumentJob> submitJobs(List<File> files, String jobName, List<String> stages) throws IOException {
    int count = 0;
    Set<DocumentJob> jobIds = new TreeSet<>(Comparator.comparing(DocumentJob::getJobId));

    Long firstJobId = null;

    int failed = 0;
    if(stages != null && !stages.isEmpty()) {
      SampleOutput.println("using custom stages " + stages);
    }
    for (File file: files){
      SampleOutput.print("\rdocument #" + (++count) + " ");
      DocumentJob documentJob = getDocumentJob(file, jobName, stages, getDriver().getType(), getDriver().getLevel());
      if(stages != null && !stages.isEmpty()) {
        documentJob.setStages(stages);
      }
      documentJob.setJobId(firstJobId);
      Job job = null;
      try {
        job = getDriver().process(documentJob);
        if (firstJobId == null) {
          firstJobId = job.getId();
          documentJob.setJobId(firstJobId);
        }
        jobIds.add(documentJob);
      } catch(Exception e) {
        failed++;
        SampleOutput.outputErr("Caught error processing " + file.getName());
        if(failed > 10 && count / 5 < failed) {
          SampleOutput.outputErr("breaking with " + failed + " out of " + count + " documents");
          break;
        }
      }
    }
    if (jobIds.size() > 1){
      SampleOutput.outputErr("got jobs: " + jobIds);
      throw new IllegalStateException("More than one JobService job created for create document requests using the same batch identifier.");
    }
    return new ArrayList<>(jobIds);
  }

  /**
   * Starts processing files and returns job ids for started jobs.
   * @param paths - paths to files/directories of files to process.
   * @param jobName - Name to give jobs/batches created
   * @param limit - if non-null, limits the number of files processed.
   * @return -
   */
  public List<DocumentJob> process(Collection<String> paths, String jobName, @Nullable Integer limit, List<String> stages) throws IOException {
    List<File> files = getFilesRecursive(paths);
    if (limit != null && files.size() > limit) {
      files = files.subList(0, limit);
    }
    SampleOutput.println("found " + files.size() + " files to process");
    return submitJobs(files, jobName, stages);
  }

  public static void main(String[] rawArgs) throws IOException {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    if (!args.jobName.matches("\\w+")) {
      SampleOutput.outputErr("Job Name must only contain alpha-numeric characters and underscores");
      System.exit(1);
    }

    SampleOutput.println("Adding documents of type " + args.type + " to " + args.configURL);
    AddDocumentToAtessa addDocumentToAtessa = new AddDocumentToAtessa(args.configURL, args.type, args.processLevel);
    List<String> stageList = new ArrayList<>();
    if(args.stages != null) {
      Collections.addAll(stageList, (args.stages.split(",")));
      if(!args.processLevel.toString().equals(stageList.get(stageList.size() -1))) {
        SampleOutput.outputErr("stages " + args.stages + " disagree with " + args.processLevel);
        return;
      }
    }
    if(args.trackFiles) {
      List<DocumentJob> documentJobs = addDocumentToAtessa.process(args.files, args.jobName, args.limit, stageList);
      addDocumentToAtessa.getDriver().processAndWait(documentJobs, statusDetail-> {
        List<DocumentStatus> statuses = statusDetail.getStatuses();
        long nullCount = statuses.stream().filter(s -> s.getStatus() == null).count();
        long un = statuses.stream().filter(s -> s.getStatus()!=null && s.getStatus().isUnsuccessfullyFinished() ).count();
        long suc = statuses.stream().filter(s -> s.getStatus()!=null && s.getStatus().isSuccessfullyFinished() ).count();
        long waiting = statuses.stream().filter(s -> s.getStatus()!=null ).count();
        SampleOutput.outputErr("NULL:" + nullCount + " un:" + un + " suc:" + suc + " wait:" + waiting);
      },5000L);
    } else {
      addDocumentToAtessa.process(new LinkedList<>(args.files), args.jobName, args.limit, stageList);
    }
  }

  public void processAndWait(Collection<String> paths,
                             Consumer<DocumentProcessingStatusDetail> update,
                             List<String> stages) throws IOException {
    List<DocumentJob> documentJobs = process(paths, DEFAULT_JOB_NAME);
    getDriver().processAndWait(documentJobs, update, 5000L);
  }
}
