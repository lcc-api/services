package com.languagecomputer.services.job;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;

/**
 * Utility to block on waiting for a single job to complete.
 *
 * In general, these methods take in a Job object and various arguments and waitfor the Job to be completed,
 * returning the JobState of the job, in case it's failed or otherwise not successful.
 *
 * @author smonahan
 */
public class JobBlocker {

  private JobBlocker() {}

  /**
   * Verbose version printing out statuses while Block on the job service completing (or failing a job)
   * @param jobId - the job to track
   * @param client - the job service to talk to
   * @param out - where to write the status
   * @param numSeconds - the interval to check the job service
   * @return the state of the job
   */
  public static JobState blockOnJobPrinting(Long jobId, JobService client, PrintStream out, Integer numSeconds) {
    return blockOnJob(jobId, client, (js,count) -> print(js, count, out), numSeconds);
  }

  public static void print(Job js, Integer count, PrintStream out) {
    if(js.getState().isFinished()) {
      out.println("\nJob finished with final state " + js.getState());
    } else {
      out.print("\r" + count + " Job status " + js.getState() + " " + js.getProgress());
    }
  }

  /**
   * Block on the job service completing (or failing a job)
   * @param jobId - the job to track
   * @param client - the job service to talk to
   * @param numSeconds - the interval to check the job service
   * @return the state of the job
   */
  public static JobState blockOnJob(Long jobId, JobService client, Integer numSeconds) {
    return blockOnJob(jobId, client, (job, count) -> {}, numSeconds);
  }

  public static JobState blockOnJob(Long jobId, JobService client, ObjIntConsumer<Job> updateConsumer, Integer numSeconds) {
    return blockOnJobs(Lists.newArrayList(jobId), client, (lst, c) -> updateConsumer.accept(lst.get(0), c), numSeconds);
  }

  public static JobState blockOnJobs(List<Long> input, JobService client, ObjIntConsumer<List<Job>> updateConsumer, Integer numSeconds) {
    int count = 0;

    List<Long> jobIds = ImmutableList.copyOf(input);
    while(true) {
      count++;
      List<Job> jobs = client.searchJobs(new JobSearchBuilder().setJobIds(jobIds).build());
      updateConsumer.accept(jobs, count);

      int succeed = 0;
      int failed = 0;
      for (Job job : jobs) {
        if(job.getState() == JobState.COMPLETED) {
          succeed++;
        } else if(job.getState() == JobState.FAILED || job.getState() == JobState.COMPLETED_WITH_FAILURES) {
          failed++;
        }
      }
      if(succeed + failed >= jobs.size()) {
        return failed > 0 ? JobState.COMPLETED_WITH_FAILURES : JobState.COMPLETED;
      }
      Uninterruptibles.sleepUninterruptibly(numSeconds, TimeUnit.SECONDS);
    }
  }

}
