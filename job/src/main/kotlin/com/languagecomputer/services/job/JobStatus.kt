package com.languagecomputer.services.job

/**
 * A tracker to indicate the status of an ATESSA job.
 *
 * This includes the name, id, the status of the job (ongoing, completed, etc.), and an integer representing the progress.
 */
class JobStatus @JvmOverloads constructor(
    val jobName: String?,
    val jobID: String?,
    val statusType: JobState?,
    val progress: Int?,
    /** this should be null if no error occurred or contain an error message if the job failed.  */
    val errorMessage: String? = null
) {

  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other !is JobStatus) return false
    if(if(jobName != null) jobName != other.jobName else other.jobName != null) return false
    if(if(jobID != null) jobID != other.jobID else other.jobID != null) return false
    if(statusType != other.statusType) return false
    return if(progress != null) progress != other.progress
    else other.progress != null
  }

  override fun hashCode(): Int {
    var result = jobName?.hashCode() ?: 0
    result = 31 * result + (jobID?.hashCode() ?: 0)
    result = 31 * result + (statusType?.name?.hashCode() ?: 0)
    result = 31 * result + (progress?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "JobStatus{" +
            "jobID='" + jobID + '\'' +
            ", jobName='" + jobName + '\'' +
            ", statusType=" + statusType +
            ", progress=" + progress +
            ", errorMessage=" + errorMessage +
            '}'
  }
}

