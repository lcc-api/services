package com.languagecomputer.services.job;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;

import java.net.URI;
import java.util.Objects;

/**
 * Utility to get job status for a job
 * @author smonahan
 */
public class GetJobStatus {
  public static final String JOBS_SERVICE = "JOBS_SERVICE";

  private static class Arguments extends JobArguments {
  }

  private final URI jobServiceURI;
  private final JobService client;

  public GetJobStatus(Arguments args) {
    LCCServiceInfo serviceInfo = new LCCServiceInfo(args);
    jobServiceURI = serviceInfo.getURIForService(JOBS_SERVICE);
    client = serviceInfo.getService(JOBS_SERVICE, JobService.class);
  }

  public static void main(String[] rawArgs) {
    Arguments args = CommandLineUtils.parseArgs(new GetJobStatus.Arguments(), rawArgs);
    GetJobStatus gjs = new GetJobStatus(args);
    SampleOutput.outputErr("connecting to: " + gjs.jobServiceURI);
    args.handleJob(gjs.client);
  }
}
