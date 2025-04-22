package com.languagecomputer.services.job

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

// Various test cases for job builder and constructors.
class JobTest {
    val jobNameDefault = DEFAULT_JOB_NAME
    val jobPropsDefault = mutableMapOf<JobPropertyKey, String>()
    val parentJobSetDefault = null
    val jobSizeDefault = 1L
    val jobAutoCloseDefault = true
    val jobStateDefault = JobState.WAITING
    val jobProgressDefault = 0.0
    val jobStartTimestampDefault = null
    val jobLastUpdatedTimestampDefault = null
    val jobEndTimestampDefault = null
    val jobIdDefault = null

    @Test
    fun testJobBuilder_emptyConstructor() {
        val job1 = JobBuilder().build()

        Assertions.assertNotNull(job1.name)
        Assertions.assertEquals(jobPropsDefault.size, job1.props.size)
        Assertions.assertEquals(jobPropsDefault, job1.props)
        Assertions.assertEquals(parentJobSetDefault, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_jobName() {
        val jobName = "JOB_NAME"
        val job1 = JobBuilder().name(jobName).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(jobPropsDefault.size, job1.props.size)
        Assertions.assertEquals(jobPropsDefault, job1.props)
        Assertions.assertEquals(parentJobSetDefault, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_jobName_jobProps() {
        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobSetDefault, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_jobName_jobProps_userId() {
        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobSetDefault, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_jobName_jobProps_userId_parentJobs() {
        var parentJob1 = 101L;
        var parentJob2 = 102L;
        var parentJobsSet1 = mutableSetOf(parentJob1)
        var parentJobsSet2 = mutableSetOf(parentJob1, parentJob2)

        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).addParentJob(parentJob1).build()
        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)


        val job2 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).setParentJobs(parentJobsSet1).addParentJob(parentJob2).build()

        Assertions.assertEquals(jobName, job2.name)
        Assertions.assertEquals(1, job2.props.size)
        Assertions.assertTrue(job2.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job2.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet2, job2.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job2.size)
        Assertions.assertEquals(jobAutoCloseDefault, job2.autoclose)
        Assertions.assertEquals(jobStateDefault, job2.state)
        Assertions.assertEquals(jobProgressDefault, job2.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job2.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job2.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job2.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job2.id)
    }

    @Test
    fun testJobBuilder_jobName_jobProps_userId_parentJobs_size() {
        var parentJob1 = 99L;
        var parentJob2 = 100L;
        var parentJobsSet1 = mutableSetOf(parentJob1)
        var parentJobsSet2 = mutableSetOf(parentJob1, parentJob2)

        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val jobSize = 1L
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).addParentJob(parentJob1).size(jobSize).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job1.parentJobs)
        Assertions.assertEquals(jobSize, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)

        val job2 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).setParentJobs(parentJobsSet2).size(jobSize).build()

        Assertions.assertEquals(jobName, job2.name)
        Assertions.assertEquals(1, job2.props.size)
        Assertions.assertTrue(job2.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job2.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet2, job2.parentJobs)
        Assertions.assertEquals(jobSize, job2.size)
        Assertions.assertEquals(jobAutoCloseDefault, job2.autoclose)
        Assertions.assertEquals(jobStateDefault, job2.state)
        Assertions.assertEquals(jobProgressDefault, job2.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job2.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job2.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job2.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job2.id)
    }

    @Test
    fun testJobBuilder_jobName_jobProps_parentJobs_size() {
        var parentJob1 = 103L;
        var parentJob2 = 104L;
        var parentJobsSet1 = mutableSetOf(parentJob1)
        var parentJobsSet2 = mutableSetOf(parentJob1, parentJob2)

        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val jobSize = 1L
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).addParentJob(parentJob1).size(jobSize).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job1.parentJobs)
        Assertions.assertEquals(jobSize, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)

        val job2 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).setParentJobs(parentJobsSet2).size(jobSize).build()

        Assertions.assertEquals(jobName, job2.name)
        Assertions.assertEquals(1, job2.props.size)
        Assertions.assertTrue(job2.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job2.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet2, job2.parentJobs)
        Assertions.assertEquals(jobSize, job2.size)
        Assertions.assertEquals(jobAutoCloseDefault, job2.autoclose)
        Assertions.assertEquals(jobStateDefault, job2.state)
        Assertions.assertEquals(jobProgressDefault, job2.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job2.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job2.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job2.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job2.id)
    }

    @Test
    fun testJobBuilder_jobName_userId_autoclose() {
        val jobName = "JOB_NAME"
        val userId = "USER_ID"
        val autoClose = false
        val job1 = JobBuilder().name(jobName).autoclose(autoClose).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(jobPropsDefault.size, job1.props.size)
        Assertions.assertEquals(jobPropsDefault, job1.props)
        Assertions.assertEquals(parentJobSetDefault, job1.parentJobs)
        Assertions.assertEquals(jobSizeDefault, job1.size)
        Assertions.assertEquals(autoClose, job1.autoclose)
        Assertions.assertEquals(JobState.WAITING, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_allParameters() {
        var parentJob1 = 105L;
        var parentJobsSet1 = mutableSetOf(parentJob1)

        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val jobProps = HashMap<JobPropertyKey, String>();
        jobProps.putAll(mutableMapOf(jobPropKey to jobPropValue))
        val jobSize = 15L
        val jobAutoClose = false

        val job1 = JobBuilder(null, jobName, jobProps, parentJobsSet1, jobSize, jobAutoClose).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(jobProps.size, job1.props.size)
        Assertions.assertEquals(jobProps, job1.props)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job1.parentJobs)
        Assertions.assertEquals(jobSize, job1.size)
        Assertions.assertEquals(jobAutoClose, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)
    }

    @Test
    fun testJobBuilder_copyConstructor() {
        var parentJob1 = 106L;
        var parentJobsSet1 = mutableSetOf(parentJob1)

        val jobName = "JOB_NAME"
        val jobPropKey = JobPropertyKey.TOTAL_WORK
        val jobPropValue = "12"
        val jobSize = 10L
        val job1 = JobBuilder().name(jobName).addProp(jobPropKey, jobPropValue).addParentJob(parentJob1).size(jobSize).build()

        Assertions.assertEquals(jobName, job1.name)
        Assertions.assertEquals(1, job1.props.size)
        Assertions.assertTrue(job1.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job1.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job1.parentJobs)
        Assertions.assertEquals(jobSize, job1.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job1.state)
        Assertions.assertEquals(jobProgressDefault, job1.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job1.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job1.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job1.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job1.id)

        val job2 = JobBuilder(job1).build()

        Assertions.assertEquals(jobName, job2.name)
        Assertions.assertEquals(1, job2.props.size)
        Assertions.assertTrue(job2.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job2.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job2.parentJobs)
        Assertions.assertEquals(jobSize, job2.size)
        Assertions.assertEquals(jobAutoCloseDefault, job1.autoclose)
        Assertions.assertEquals(jobStateDefault, job2.state)
        Assertions.assertEquals(jobProgressDefault, job2.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job2.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job2.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job2.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job2.id)

        val job3Size = 3L
        val job3AutoClose = false
        val job3 = JobBuilder(job1).autoclose(job3AutoClose).size(job3Size).build()

        Assertions.assertEquals(jobName, job3.name)
        Assertions.assertEquals(1, job3.props.size)
        Assertions.assertTrue(job3.props.contains(jobPropKey))
        Assertions.assertEquals(jobPropValue, job3.props.get(jobPropKey))
        Assertions.assertEquals(parentJobsSet1, job3.parentJobs)
        Assertions.assertEquals(job3Size, job3.size)
        Assertions.assertEquals(job3AutoClose, job3.autoclose)
        Assertions.assertEquals(jobStateDefault, job3.state)
        Assertions.assertEquals(jobProgressDefault, job3.progress)
        Assertions.assertEquals(jobStartTimestampDefault, job3.startTimestamp)
        Assertions.assertEquals(jobLastUpdatedTimestampDefault, job3.lastUpdatedTimestamp)
        Assertions.assertEquals(jobEndTimestampDefault, job3.endTimestamp)
        Assertions.assertEquals(jobIdDefault, job3.id)
    }

    @Test
    fun test_Builder_namedJob_constructor() {
        val jobName = "job1"
        val job1 = JobBuilder.namedJob(jobName)
        Assertions.assertEquals(jobName, job1.name)
    }

}
