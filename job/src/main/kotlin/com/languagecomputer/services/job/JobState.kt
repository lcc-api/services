package com.languagecomputer.services.job

/**
 * Note: the ordering of the enum values matter since
 * JobStateMessage.compareTo depends upon it and any use
 * of the compareTo function like Collections.min of that class.
 */
enum class JobState(val isFinished: Boolean) {
    // ordering matters: see class comment

    // Job failed to achieve its objectives and is no longer being processed.
    FAILED(true),
    // Job has been started, but has not finished (COMPLETED or FAILED). It is not currently being processed (ONGOING).
    PAUSED,
    // Job has not yet begun to be processed; default beginning state
    WAITING,
    // Job is being processed and has neither completed nor failed.
    ONGOING,
    // Parent job is done, but some of the subjobs were not completed successfully.
    // For example, if this was document processing this means some docs failed but the rest succeeded
    COMPLETED_WITH_FAILURES(true),
    // Job has been fully processed and has not failed.
    COMPLETED(true),
    ;

    constructor() : this(false)

}
