package com.languagecomputer.services.job

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Class to hold data needed for subscribing jobs via websockets.
 *
 */
class JobSubscriptionStatus @JvmOverloads constructor(
  // Job id not needed since it is in JobRequestSubscriptionInfo.
  // Job name not needed since it is in JobRequestSubscriptionInfo. If the job name
  // associated with JobRequestSubscriptionInfo.jobId,
  // JobRequestSubscriptionInfo.jobName should be updated via setSubscriptionJobName function.
  val subscriptionInfo: JobRequestSubscriptionInfo,
  /**
   * State of the subscription job ID (JobRequestSubscriptionInfo.jobId).
   * This intentionally does not aggregate the tracking job IDs in this
   * class since JobService tells JobStatusReporter who updates this class and
   * sends the data to the frontend to be updated.
   */
  var jobStateMessage: JobStateMessage = JobStateMessage(JobState.WAITING)
) {
  // Map: tracking (child) job ID -> JobStatusMessage
  var trackingJobStatesMap: MutableMap<Long, JobStateMessage> = HashMap()

  // tracking (child) job id property maps
  // Map: job ID -> (Map of String representing property key -> String representing property value)
  var trackingJobPropsMap: MutableMap<Long, Map<Job.PropertyKey, String>> = HashMap()

  // Map: tracking (child) job id -> Double progress
  var trackingJobProgressMap: MutableMap<Long, Double> = HashMap()

  // Map: tracking (child) job id -> Long size
  var trackingJobSizeMap: MutableMap<Long, Long> = HashMap()

  /**
   * The job properties of subscription job ID (JobRequestSubscriptionInfo.jobId).
   */
  var props: Map<Job.PropertyKey, String> = HashMap()

  /**
   * The job progress of subscription job ID (JobRequestSubscriptionInfo.jobId).
   * Progress as a percentage (0-100).
   */
  var progress: Double = 0.0

  @JsonIgnore
  fun getTrackingJobIds(): Collection<Long> {
    return trackingJobStatesMap.keys
  }

  fun setSubscriptionJobName(jobName: String) {
    subscriptionInfo.jobName = jobName
  }

  /**
   * Returns true if removed tracking (child) job id from @trackingJobStatesMap, @trackingJobPropsMap, and @trackingJobProgressMap.
   */
  fun removeTrackingJobId(childJobId: Long): Boolean {
    trackingJobStatesMap.remove(childJobId)
    trackingJobPropsMap.remove(childJobId)
    trackingJobProgressMap.remove(childJobId)
    trackingJobSizeMap.remove(childJobId)
    return !trackingJobStatesMap.containsKey(childJobId) &&
            !trackingJobPropsMap.containsKey(childJobId) &&
            !trackingJobSizeMap.containsKey(childJobId) &&
            !trackingJobProgressMap.containsKey(childJobId)
    //Note: return trackingJobStatesMap.remove(childJobId) && trackingJobPropsMap.remove(childJobId) && trackingJobProgressMap.remove(childJobId) && trackingJobSizeMap.remove(childJobId); would work except when was in a bad state before calling this function
  }

  @JsonIgnore
  fun setTrackingJobState(trackingJobId: Long, trackingJobStateMsg: JobStateMessage) {
    trackingJobStatesMap[trackingJobId] = trackingJobStateMsg
    // Note: not updating jobStateMessage since updating it is done separately.
  }

  @JsonIgnore
  fun setTrackingJobProps(trackingJobId: Long, trackingJobProps: Map<Job.PropertyKey, String>) {
    trackingJobPropsMap[trackingJobId] = trackingJobProps
  }

  @JsonIgnore
  fun setTrackingJobProgress(trackingJobId: Long, trackingJobProgress: Double) {
    trackingJobProgressMap[trackingJobId] = trackingJobProgress
    // Note: not updating progress of subscription job since updating it is done separately.
  }

  @JsonIgnore
  fun setTrackingJobSize(trackingJobId: Long, trackingJobSize: Long) {
    trackingJobSizeMap[trackingJobId] = trackingJobSize
  }

  private fun <T> groupBy(groupFn: Function<JobStateMessage, T>): String {
    return trackingJobStatesMap.values.stream()
      .collect(Collectors.groupingBy(groupFn))
      .entries.stream()
      .map { entry: Map.Entry<T, List<JobStateMessage?>> -> entry.key.toString() + ": " + entry.value.size }
      .collect(Collectors.joining(", "))
  }

  override fun toString(): String {
    return subscriptionInfo.jobId.toString() + ":" + jobStateMessage.state + ":" + groupBy { it.state }
  }

  fun toPrettyString(): String {
    return groupBy { jobStateMessage: JobStateMessage ->
      if (jobStateMessage.isUnsuccessfullyFinished()) "Failed"
      else if (jobStateMessage.isSuccessfullyFinished()) "Complete"
      else "Ongoing"
    }
  }

  class Builder {
    private lateinit var subscriptionInfo: JobRequestSubscriptionInfo
    private var job: Job? = null

    constructor(status: JobSubscriptionStatus) {
      copy(status)
    }

    constructor(job: Job) {
      this.subscriptionInfo = JobRequestSubscriptionInfo.fromJob(job)!!
      this.job = job
    }

    constructor(jobRequestSubscriptionInfo: JobRequestSubscriptionInfo) {
      this.subscriptionInfo = jobRequestSubscriptionInfo
    }

    private fun copy(status: JobSubscriptionStatus) {
      subscriptionInfo = status.subscriptionInfo
    }

    fun build(): JobSubscriptionStatus {
      return JobSubscriptionStatus(subscriptionInfo, JobStateMessage(job?.state ?: JobState.WAITING))
    }
  }
}

class JobSubscriptionStatusList(
  var statuses: MutableList<JobSubscriptionStatus> = ArrayList(),
  var trimmedResults: Boolean = false
) {
  constructor(jobSubscriptionStatuses: MutableList<JobSubscriptionStatus>) : this() {
    statuses = jobSubscriptionStatuses
  }

  fun add(status: JobSubscriptionStatus) {
    statuses.add(status)
  }

  override fun toString(): String {
    return statuses.stream()
      .map { status: JobSubscriptionStatus -> status.jobStateMessage.state }
      .collect(Collectors.groupingBy { x: JobState? -> x })
      .map { entry ->  String.format("%s: %d", entry.key.toString(), entry.value.size) }
      .joinToString(", ")
  }
}

