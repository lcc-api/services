package com.languagecomputer.services.job

/**
 * Common properties that can be set on jobs.
 * @author smonahan
 */
enum class JobPropertyKey {
    /**
     * Convenience feature for storing the result of a long-running job
     */
    CACHED_RESULT,
    /**
     * Human-readable, short descriptor of the kind of job being run
     *  e.g. "Update Documents" "Create Documents" "Build Model"
     * Consider the user interface when setting this field for child jobs; you may want to
     * leave it blank.
     */
    TYPE,
    /**
     *  Human-readable description of what the job is doing or why it is in its current state.
     */
    STATUS_DESCRIPTION,
    /**
     * DocumentJobStatus.StatusCategory
     */
    DOCUMENT_JOB_STATUS_CATEGORY,
    /**
     * Document ID (if relevant). This may be either a master ID OR a split ID.
     */
    LINKED_DOCUMENT,
    /**
     * Millisecond representation of the start time for the job
     */
    START_TIME,
    /**
     * Accumulated work done specifically on this job.
     */
    ACCUMULATED_INTERNAL_WORK,
    /**
     * Accumulated work done on this job and its children. Sum of ACCUMULATED_INTERNAL_WORK of itself plus the ACCUMULATED_TOTAL_WORK of all the job's children.
     */
    ACCUMULATED_TOTAL_WORK,
    /**
     * Total work (in undefined units) to complete this job.
     */
    TOTAL_WORK,
    /**
     * If true or unset, Job will update its progress value based upon properties on itself and its children (precedence: work (TOTAL_WORK), doc status (DOCUMENT_JOB_STATUS_CATEGORY), progress, etc.)
     * If false, Job will update its progress value based upon its children's progress equally.
     * This property is when one wants to update the progress directly and turn off the auto updating of the job progress.
     */
    AUTO_UPDATE_PROGRESS,
    ;
}