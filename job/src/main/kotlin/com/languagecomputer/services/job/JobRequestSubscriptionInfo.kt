package com.languagecomputer.services.job

import com.google.common.base.MoreObjects

/**
 * Data associated with a subscription job and its tracking job(s). This class is used to request for a job to be tracked.
 *
 * This message is used when a subscriber wants to know about updates of the job.
 */
open class JobRequestSubscriptionInfo @JvmOverloads constructor(
  // the subscription job id
  // This is intentionally not null. Please create job via the job service beforehand.
  var jobId: Long,

  // subscription job name. This is needed to initialize JobSubscriptionStatus.kt which tells the frontend.
  // This is needed so that the first time the frontend has the job name in the case where you are creating the
  //  JobSubscriptionStatus for the first time. JobServiceWebSocketEvents.java receives a JobRequestSubscriptionInfo message
  //  and has access to JobStatusReporter, but not JobService to get the job name at the moment. JobServiceWebSocketEvents
  //  needs to have access to the ServicesMap (like how EventionResultsEndpoint calls EventionResultsProcessor) to call the
  //  JobService to do the subscription.
  // This value will be updated if the subscription job ID's job name changes in the job service.
  var jobName: String?,

  // user ID requesting the information
  var userId: String? = null,

  // This represents the estimated number of sub-jobs associated with this job (e.g., number of batches, documents in
  // a batch, splits in a document). It is used for computing percentage complete and for deriving state based upon
  // the state of the sub-jobs.
  var size: Long,

  // When true, automatically close @jobId when all tracked IDs are closed.
  var autoclose: Boolean,
) {
  override fun toString(): String {
    return MoreObjects.toStringHelper(this.javaClass)
      .add("jobId", jobId)
      .add("jobName", jobName)
      .add("userId", userId)
      .toString()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as JobRequestSubscriptionInfo

    return jobId == other.jobId
  }

  @Suppress("unused")
  fun everyParameterEquals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as JobRequestSubscriptionInfo

    if (jobId != other.jobId) return false
    if (jobName != other.jobName) return false
    if (userId != other.userId) return false
    return size == other.size
  }

  override fun hashCode(): Int {
    //result = 31 * result + (jobName?.hashCode() ?: 0)
    //result = 31 * result + (userId?.hashCode() ?: 0)
    return jobId.hashCode()
  }

  companion object {
    @JvmStatic
    fun fromJob(job: Job): JobRequestSubscriptionInfo? {
      if (job.id != null) {
        return JobRequestSubscriptionInfo(
          job.id!!,
          job.name,
          job.userId,
          job.size,
          job.autoclose
        )
      }
      return null
    }
  }
}
