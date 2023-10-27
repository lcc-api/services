package com.languagecomputer.services.job;

import com.google.common.util.concurrent.Uninterruptibles;

import java.io.PrintStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Utility to block on waiting for a job to complete.
 * @author smonahan
 */
public class JobBlocker {

  private JobBlocker() {}

  /**
   * Verbose version printing out statuses while Block on the job service completing (or failing a job)
   * @param jobId - the job to track
   * @param client - the job service to talk to
   * @param out - where to write the status
   * @return true if completed, false if failures
   */
  public static boolean blockOnJobPrinting(Long jobId, JobService client, PrintStream out, Integer numSeconds) {
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
   * @param updateConsumer - a callback for the status update
   * @return true if completed, false if failures
   */
  public static boolean blockOnJob(Long jobId, JobService client, BiConsumer<Job, Integer> updateConsumer, Integer numSeconds) {
    int count = 0;
    while(true) {
      count++;
      Job job = client.getJob(jobId);
      updateConsumer.accept(job, count);
      if(job.getState() == JobState.COMPLETED) {
        return true;
      } else if(job.getState() == JobState.FAILED || job.getState() == JobState.COMPLETED_WITH_FAILURES) {
        return false;
      }
      Uninterruptibles.sleepUninterruptibly(numSeconds, TimeUnit.SECONDS);
    }
  }

}
