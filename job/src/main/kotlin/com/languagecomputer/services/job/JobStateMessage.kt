package com.languagecomputer.services.job

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Holds the JobState of a Job.
 */
class JobStateMessage(
    val state: JobState, 
    val description: String? = null
) : Comparable<JobStateMessage?> {

  constructor(status: JobState) : this(status, "NONE")

  @JsonIgnore
  fun isUnsuccessfullyFinished(): Boolean {
    return this.state == JobState.FAILED || this.state == JobState.COMPLETED_WITH_FAILURES
  }

  @JsonIgnore
  fun isSuccessfullyFinished(): Boolean {
    return this.state == JobState.COMPLETED
  }

  @JsonIgnore
  fun isFinished(): Boolean {
    return isSuccessfullyFinished() || isUnsuccessfullyFinished()
  }


  override operator fun compareTo(other: JobStateMessage?): Int {
    return this.state.compareTo(other!!.state)
  }

  override fun toString(): String {
    return "JobStateMessage(state=$state, description=$description)"
  }

  companion object {
    fun failed(reason: String?): JobStateMessage {
      return JobStateMessage(JobState.FAILED, reason)
    }
  }
}
