package com.languagecomputer.services.docprocess;

import com.google.common.collect.Lists;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static com.languagecomputer.services.docprocess.DocumentProcessingUtils.getDocumentJob;
import static com.languagecomputer.services.docprocess.DocumentProcessingUtils.getFilesRecursive;

/**
 * Similar to AddDocumentToAtessa, except this blocks and waits for the system to return output for that document.
 * The outputter controls how the information is processed.
 *
 * This method is generally discouraged as the ATESSA pipeline can process multiple documents at once and a more
 * asynchronous approach using job tracking should be used.
 * @author smonahan
 */
public class AddDocumentToAtessaBlocking extends AddDocumentToAtessa {

  private static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Parameters(index = "0", description = {"Type of document being uploaded.", "Options: ${COMPLETION-CANDIDATES}"}, arity = "1")
    DocumentType type = null;

    @CommandLine.Option(names = "--updateTime", description = {"Change the times to integers"})
    boolean updateTime = false;

    @CommandLine.Parameters(index = "1", arity = "1", description = {"Type of outputter", "Options: ${COMPLETION-CANDIDATES}"})
    String outputterType;

    @CommandLine.Parameters(index = "2..*", arity = "1..*", paramLabel = "file", description = "files to upload")
    LinkedList<String> files = new LinkedList<>();

    @CommandLine.Option(names = "--jobID", description = {"the name of the job we are creating"})
    String jobID = null;
  }

  private final String outputterType;

  public AddDocumentToAtessaBlocking(URI uri, DocumentType type, String outputterType, String token) {
    super(uri, type, DocumentProcessingLevel.Level.HEAVY, token);
    this.outputterType = outputterType;
  }

  @Override
  protected List<Long> submitJobs(List<File> files, String jobID, List<String> stages) throws IOException {
    for(File file : files) {
      DocumentJob job = getDocumentJob(file, jobID, stages, getDriver().getType(), getDriver().getLevel());
      SampleOutput.println("Submitting " + job.getMessage().getDocumentID());
      String result = getDriver().createOrUpdateDocument(job, outputterType, true);
      SampleOutput.println("Got result of length " + result.length());
      SampleOutput.println(result);
    }
    return Lists.newArrayList();
  }


  public static void main(String[] rawArgs) throws IOException {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    System.err.println("Adding documents of type " + args.type + " to " + args.configURL + " with output " + args.outputterType + " and job name " + args.jobID);
    AddDocumentToAtessaBlocking adder = new AddDocumentToAtessaBlocking(args.configURL, args.type, args.outputterType, args.token);
    List<File> files = getFilesRecursive(args.files);
    adder.submitJobs(files, args.jobID, null);
  }
}
