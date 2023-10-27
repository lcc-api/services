package com.languagecomputer.services.docprocess;

import com.languagecomputer.services.job.JobBlocker;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.job.JobService;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.RestUtils;
import com.languagecomputer.services.job.JobArguments;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * This script updates documents already in the core index to heavy, overwriting the existing heavy.
 * @author smonahan
 */
public class UpdateDocuments extends JobArguments {
  public static final String DOCUMENT_PROCESSING_SERVICE = "DOCUMENT_PROCESSING";

  private Job run() throws IOException {
    URI docprocessor = new LCCServiceInfo(this).getURIForService(DOCUMENT_PROCESSING_SERVICE);
    URL update = new URL(docprocessor.toString() + "/update/all");
    SampleOutput.println(update);
    final Job job = RestUtils.get(update, Job.class);
    SampleOutput.println("job: name " + job.getName() );
    if(block) {
      LCCServiceInfo serviceInfo = new LCCServiceInfo(this);
      URI jobServiceURI = serviceInfo.getURIForService("JOBS_SERVICE");
      SampleOutput.println("blocking on job id:\"" + job.getId() + "\" name: \"" + job.getName() + "\" at " + jobServiceURI);
      JobService client = serviceInfo.getService("JOBS_SERVICE", JobService.class);
      final boolean success = handleJob(client);
      if(!success) {
        throw new IllegalStateException("Failed to process documents");
      }
    }
    return job;
  }

  public static void main(String[] args) throws IOException {
    UpdateDocuments updater = CommandLineUtils.parseArgs(new UpdateDocuments(), args);
    updater.run();
    SampleOutput.println("Build request sent. Verify the build has started properly by checking the Job Status");
  }
}
