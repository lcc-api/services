package com.languagecomputer.services.job;

import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import picocli.CommandLine;

import java.util.Objects;

/**
 * Utilities for interacting with the Job Service at the cmdline.
 * @author smonahan
 */
public class JobArguments extends CommandLineUtils.ServiceArgs {
  @CommandLine.Option(names = "--jobID", description = {"the id of the job, often file name, or passed in as a parameter to the program which started it"})
  public String jobID;

  @CommandLine.Option(names = "--block", description = {"if true, block until the job is completed"})
  public boolean block;

  @CommandLine.Option(names = "--verbose", description = {"if true, print out verbose status when in block mode"})
  public boolean verbose;

  @CommandLine.Option(names = "--checkTime", description = {"how often to check the job service, default is 10s"})
  public Integer numSeconds = 10;

  public boolean handleJob(JobService client) {
    Long jobid = Long.parseLong(jobID);
    Job status = client.getJob(jobid);
    boolean success = printJobStatus(status, 0);
    if(block && status.isStillProcessing()) {
      if (verbose) {
        JobState state = JobBlocker.blockOnJob(jobid, client, JobArguments::printJobStatus, numSeconds);
        success = state == JobState.COMPLETED;
      } else {
        JobState state = JobBlocker.blockOnJobPrinting(jobid, client, System.out, numSeconds);
        success = state == JobState.COMPLETED;
      }
    }
    return success;
  }

  private static boolean printJobStatus(Job job, Integer count) {
    SampleOutput.println(count + ": status for job " + job.getId() + " is " + job.getState() + " with completion " + job.getProgress() + "%");
    if (job.getProp(JobPropertyKey.STATUS_DESCRIPTION) != null && !job.getProp(JobPropertyKey.STATUS_DESCRIPTION).isEmpty()) {
      SampleOutput.println("\terror message: " + job.getProp(JobPropertyKey.STATUS_DESCRIPTION));
    }
    return job.getState() == JobState.COMPLETED;
  }

}
