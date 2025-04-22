package com.languagecomputer.services.docprocess;

import com.google.common.util.concurrent.Uninterruptibles;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.languagecomputer.services.docprocess.DocumentProcessingDriver.DEFAULT_JOB_NAME;
import static com.languagecomputer.services.docprocess.DocumentProcessingUtils.getDocumentJob;

/**
 * Simple case of uploading a single file to Document processing.
 * If track is on shows a very simple track workflow loop.
 * @author smonahan
 */
public class SimpleUpload {

  public static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Parameters(index = "0", description = {"Type of document being uploaded.", "Options: ${COMPLETION-CANDIDATES}"}, arity = "1")
    DocumentType type = null;

    @CommandLine.Parameters(index = "1", arity = "1", paramLabel = "file/dir", description = { "file to upload"})
    String filename = null;

    @CommandLine.Option(names = "--level", description = {"level of document processing to do", "Default: ${DEFAULT-VALUE}", "Options: ${COMPLETION-CANDIDATES}"})
    DocumentProcessingLevel.Level processLevel = DocumentProcessingLevel.Level.EXAMPLE;

    @CommandLine.Option(names = "--trackFiles", description = {"track the results of the each file, querying every 5 seconds"})
    boolean trackFiles = false;

   @CommandLine.Option(names = "--jobName", description = {"the name of the job we are creating"})
    String jobName = DEFAULT_JOB_NAME;
  }

  public static void track(Job job, DocumentProcessingService docprocess) {
    DocumentProcessingStatusDetail statusDetail = null;
    while (statusDetail == null || statusDetail.stillWorking()) {
      statusDetail = docprocess.getDetailedStatus(job.getId());
      SampleOutput.println("status is " + statusDetail);
      Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
    }
    SampleOutput.println("finished status is " + statusDetail);
  }

  public static void main(String[] rawArgs) throws IOException {
    SimpleUpload.Arguments args = CommandLineUtils.parseArgs(new SimpleUpload.Arguments(), rawArgs);
    SampleOutput.println("Adding " + args.filename + " of type " + args.type + " to " + args.configURL);
    // Change out this line if you are not using cls-client to utilize your own client creation
    final DocumentProcessingService docprocess = RestClients.createWithAuth(args.configURL, DocumentProcessingService.class, args.token, 20, TimeUnit.MINUTES);
    final DocumentProcessingLevel level = DocumentProcessingLevel.fromLevel(args.processLevel);
    File f = new File(args.filename);
    // Create document job
    DocumentJob documentJob = getDocumentJob(f, args.jobName, null, args.type, level);
    // Process document
    Job job = docprocess.createNewDocument(documentJob);
    // Optionally wait for job to complete
    if(args.trackFiles) {
      track(job, docprocess);
    }
  }
}
