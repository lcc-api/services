package com.languagecomputer.services.job;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

class JavaJobTest {
  /**
   * Test Job class constructors (not builder class constructors)
   */
  @Test
  void test_Job_constructors() {
    // Job class should only have one constructor:
    final String jobName = "job2";
    Job job2 = new Job(null, jobName, new EnumMap<>(JobPropertyKey.class), null, 3, false);

    Assertions.assertEquals(jobName, job2.getName());
  }

  @Test
  void test_Builder_namedJob_constructor() {
    final String jobName = "job1";
    Job job1 = JobBuilder.namedJob(jobName);

    Assertions.assertEquals(jobName, job1.getName());
  }

}
