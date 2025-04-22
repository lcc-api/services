package com.languagecomputer.services.job

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.validation.constraints.Min

const val DEFAULT_JOB_NAME = "NO_NAME_GIVEN"

/**
 * Represents a unit of work whose progress we want to track
 * This class limit the constructors to empty, all construction parameters, and copy constructor.
 * Intentionally, the primary constructor does not use the JvmOverloads annotation in order to reduce the number of
 * constructors and support construction via the Builder class.
 *
 * @property name       Human-readable name of job.  It needn't be globally unique.
 * @property props      String-valued properties associated with job
 * @property parentJobs Set of job objects that represents a unit of work that subsumes this one.
 *                      A child job should be subsumed by its parent, but a parent can be more than the
 *                      sum of its children jobs.
 * @property state      Machine-readable description of job's current status, the possible values of which
 *                      are strictly ordered.
 * @property progress   Numerical representation of how much of the job has been completed.  This must be a
 *                      real number between 0 and 1, inclusive.
 */
data class Job @JvmOverloads constructor(
    var id: Long? = null,
    var name: String = DEFAULT_JOB_NAME,
    var props: MutableMap<JobPropertyKey, String> = mutableMapOf(),
    var parentJobs: MutableSet<Long>? = null,
        // This represents the estimated number of sub-jobs associated with this job (e.g., number of batches, documents in
        // a batch, splits in a document). It is used for computing percentage complete and for deriving state based upon
        // the state of the sub-jobs.
        //Note: will eventually be converted to TOTAL_WORK property
    var size: Long = 1,
        // indicates whether the job should close automatically when all children are closed.
        //Note: will probably be converted to a property
    var autoclose: Boolean = true,
    var state: JobState = JobState.WAITING,
    // progress as a percentage (0-100)
    @field:Min(value = 0, message = "The value cannot be negative")
    var progress: Double = 0.0,
    // start time in the number of milliseconds since the epoch
    var startTimestamp: Long? = null,
    // last time anything?? was updated in the number of milliseconds since the epoch
    var lastUpdatedTimestamp: Long? = null,
    // end time in the number of milliseconds since the epoch
    var endTimestamp: Long? = null
    // identifier associated with the job, usually set on the backend.
) {

    /**
     * Convenience functions for updating props
     *
     * @param p - property to be updated
     */
    fun hasProp(p: JobPropertyKey): Boolean  = props.contains(p)
    fun getProp(p: JobPropertyKey): String? = props[p]

    /**
     * The purpose of this function is to check whether the job
     * is still processing and making some type of progress.
     */
    fun isStillProcessing() : Boolean {
        return (
                (state == JobState.ONGOING)
                        || (state == JobState.WAITING)
                )
    }

    override fun toString(): String {
        return "Job(name=$name, props=$props, parentJobs=$parentJobs, state=$state, progress=$progress, size=$size, autoclose=$autoclose, startTimestamp=$startTimestamp, lastUpdatedTimestamp=$lastUpdatedTimestamp, endTimestamp=$endTimestamp, id=$id)"
    }

    fun setProp(k : JobPropertyKey, s: String) {
        props[k] = s;
    }

    fun deleteProp(k : JobPropertyKey) {
        props.remove(k);
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Job
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = 31 * (id?.hashCode() ?: 0)
        return result
    }


    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss").withZone(ZoneId.systemDefault())

        @JvmStatic
        fun getDateVersion(): String = formatter.format(Instant.now())
    }
}
