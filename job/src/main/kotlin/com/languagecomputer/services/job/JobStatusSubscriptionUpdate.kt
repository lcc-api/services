package com.languagecomputer.services.job


/**
 * This class specifies an update to a subscription ("parent") process (job) or a tracking ("child") process (job).
 * The subscription (or "parent") process (job) is specified in @{@link JobRequestSubscriptionInfo}.
 * This class is used to update the values of @{@link JobSubscriptionStatus} on the frontend.
 */
class JobStatusSubscriptionUpdate(
    // what subscription request this was originally coming from
    val subscriptionInfo: JobRequestSubscriptionInfo?,

    // job ID that is receiving the update
    // This job id could the subscribing job id or tracking (child) job id
    val jobId: Long,

    // It is used for updating the subscription job name if needed.
    // Value can be null since it is not needed for tracking (child) jobs
    // since their job name isn't currently in the JobSubscriptionStatus object.
    val jobName: String?,

    // updated status of the job id
    val status: JobStateMessage,

    // Needed for job ID's property data (e.g. document state)
    val props: Map<Job.PropertyKey, String> = mutableMapOf(),

    // updated progress of the job id
    val progress: Double,
    ) {

    // If true, will remove @jobId from the subscription @subscriptionInfo.
    // Note: This would be in a different message if we set up web sockets to send more than one type of message from the backend to the frontend.
    var removeJobId: Boolean? = null

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun fromJob(job: Job): JobStatusSubscriptionUpdate? {
            if (job.id != null) {
                return JobStatusSubscriptionUpdate(
                    JobRequestSubscriptionInfo.fromJob(job),
                    job.id!!,
                    job.name,
                    JobStateMessage(job.state),
                    job.props,
                    job.progress,
                )
            }
            return null
        }
    }

    override fun toString(): String {
        return "JobStatusSubscriptionUpdate(subscriptionInfo=$subscriptionInfo, jobId=$jobId, jobName=$jobName, status=$status, props=$props, progress=$progress)"
    }
}
