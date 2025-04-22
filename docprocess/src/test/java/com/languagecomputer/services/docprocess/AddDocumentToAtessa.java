package com.languagecomputer.services.docprocess;

import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.examplestore.ExampleUpdateStrategy;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
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

  public AddDocumentToAtessa(URI configURI, DocumentType type, DocumentProcessingLevel.Level processLevel, String token) {
    driver = new DocumentProcessingDriver(
        type,
        RestClients.createWithAuth(configURI, DocumentProcessingService.class, token, 20, TimeUnit.MINUTES),
        DocumentProcessingLevel.fromLevel(processLevel)
    );
  }

  protected DocumentProcessingDriver getDriver() {
    return driver;
  }

  public List<Long> process(Collection<String> paths, String batchId) throws IOException {
    return process(paths, batchId, null, null);
  }

  protected List<Long> submitJobs(List<File> files, String jobName, List<String> stages) throws IOException {
    int count = 0;
    int failed = 0;
    Set<Long> jobIds = new HashSet<>();
    Long firstJobId = null;
    for (File file : files){
      SampleOutput.print("\rdocument #" + (++count) + " ");
      Long jobid = processFile(file, jobName, stages, firstJobId);
      if(jobid != null) {
        jobIds.add(jobid);
        if(firstJobId == null) {
          firstJobId = jobid;
        }
      } else {
        failed++;
        if (failed > 10 && count / 5 < failed) {
          SampleOutput.outputErr("breaking with " + failed + " out of " + count + " documents");
        }
      }
    }
    if (jobIds.size() > 1){
      SampleOutput.outputErr("got jobs: " + jobIds);
      throw new IllegalStateException("More than one JobService job created for create document requests using the same batch identifier.");
    }
    return new ArrayList<>(jobIds);
  }

  public Long processFile(File file, String jobName, List<String> stages, Long firstJobId) throws IOException {
    DocumentJob documentJob = getDocumentJob(file, jobName, stages, getDriver().getType(), getDriver().getLevel());
    // This bit of machinery will let you use the same "batch job id" for a set of documents, to make tracking status a lot simpler
    documentJob.setJobId(firstJobId);
    try {
      Job job = getDriver().process(documentJob);
      return job.getId();
    } catch (Exception e) {
      SampleOutput.outputErr("Caught error " + e.getMessage() + " processing " + file.getName());
      return null;
    }
  }

  /**
   * Starts processing files and returns job ids for started jobs.
   * @param paths - paths to files/directories of files to process.
   * @param jobName - Name to give jobs/batches created
   * @param limit - if non-null, limits the number of files processed.
   * @return -
   */
  public List<Long> process(Collection<String> paths, String jobName, @Nullable Integer limit, List<String> stages) throws IOException {
    List<File> files = getFilesRecursive(paths);
    if (limit != null && files.size() > limit) {
      files = files.subList(0, limit);
    }
    SampleOutput.println("found " + files.size() + " files to process");
    return submitJobs(files, jobName, stages);
  }

  private static void trackJobs(AddDocumentToAtessa addDocumentToAtessa, Arguments args, List<String> stageList) throws IOException {
    List<Long> documentJobs = addDocumentToAtessa.process(args.files, args.jobName, args.limit, stageList);
    SampleOutput.outputErr("waiting for " + documentJobs.size() + " jobs to complete");
    addDocumentToAtessa.getDriver().waitForJobs(documentJobs, statusDetail-> {
      List<DocumentStatus> statuses = statusDetail.getStatuses();
      long nullCount = statuses.stream().filter(s -> s.getStatus() == null).count();
      long failed = statuses.stream().filter(s -> s.getStatus()!=null && s.getStatus().isUnsuccessfullyFinished() ).count();
      long success = statuses.stream().filter(s -> s.getStatus()!=null && s.getStatus().isSuccessfullyFinished() ).count();
      long waiting = statuses.stream().filter(s -> s.getStatus()!=null && !s.getStatus().isFinished()).count();
      StringBuilder sb = new StringBuilder();
      if(nullCount > 0) {
        sb.append("NULL:").append(nullCount).append(" ");
      }
      if(failed > 0) {
        sb.append("failed:").append(failed).append(" ");
      }
      if(success > 0) {
        sb.append("completed: ").append(success).append(" ");
      }
      if(waiting > 0) {
        sb.append("in progress: ").append(waiting);
      }
      SampleOutput.outputErr(sb.toString());
    },5000L);
  }

  public static void main(String[] rawArgs) throws IOException {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    if (!args.jobName.matches("\\w+")) {
      SampleOutput.outputErr("Job Name must only contain alpha-numeric characters and underscores");
      System.exit(1);
    }

    SampleOutput.println("Adding documents of type " + args.type + " to " + args.configURL);
    AddDocumentToAtessa addDocumentToAtessa = new AddDocumentToAtessa(args.configURL, args.type, args.processLevel, args.token);
    List<String> stageList = new ArrayList<>();
    if(args.stages != null) {
      Collections.addAll(stageList, (args.stages.split(",")));
      if(!args.processLevel.toString().equals(stageList.get(stageList.size() -1))) {
        SampleOutput.outputErr("stages " + args.stages + " disagree with " + args.processLevel);
        return;
      }
    }
    if(args.trackFiles) {
      trackJobs(addDocumentToAtessa, args, stageList);
    } else {
      addDocumentToAtessa.process(new LinkedList<>(args.files), args.jobName, args.limit, stageList);
    }
  }

}
