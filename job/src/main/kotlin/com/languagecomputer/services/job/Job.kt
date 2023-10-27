package com.languagecomputer.services.job

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.validation.constraints.Min

const val DEFAULT_JOB_NAME = "NO_NAME_GIVEN"

/**
 * Represents a unit of work whose progress we want to track
 *
 * @property name       Human-readable name of job.  It needn't be globally unique.
 * @property props      String-valued properties associated with job
 * @property userId     The user that created this job
 * @property parentJobs Set of job objects that represents a unit of work that subsumes this one.
 *                      A child job should be subsumed by its parent, but a parent can be more than the
 *                      sum of its children jobs.
 * @property state      Machine-readable description of job's current status, the possible values of which
 *                      are strictly ordered.
 * @property progress   Numerical representation of how much of the job has been completed.  This must be a
 *                      real number between 0 and 1, inclusive.
 */
open class Job @JvmOverloads constructor(
        var name: String = DEFAULT_JOB_NAME,
        var props: Map<PropertyKey, String> = mutableMapOf(),
        var userId: String? = null,
        var parentJobs: MutableSet<Job>? = null,
        // This represents the estimated number of sub-jobs associated with this job (e.g., number of batches, documents in
        // a batch, splits in a document). It is used for computing percentage complete and for deriving state based upon
        // the state of the sub-jobs.
        var size: Long = 1,
        // indicates whether the job should close automatically when all children are closed.
        var autoclose: Boolean = true,
) {
    var state: JobState = JobState.WAITING
  // progress as a percentage (0-100)
    @field:Min(value = 0, message = "The value cannot be negative")
    var progress: Double = 0.0
    // start time in the number of milliseconds since the epoch
    var startTimestamp: Long? = null
    // last time anything?? was updated in the number of milliseconds since the epoch
    var lastUpdatedTimestamp: Long? = null
    // end time in the number of milliseconds since the epoch
    var endTimestamp: Long? = null
    // identifier associated with the job, usually set on the backend.
    var id: Long? = null

    constructor(name: String? = null, props: Map<PropertyKey, String> = mutableMapOf(), userId: String? = null, parentJob: Job) :
            this(name ?: DEFAULT_JOB_NAME, props, userId, mutableSetOf(parentJob))

    constructor(name: String? = null, props: Map<PropertyKey, String> = mutableMapOf(), userId: String? = null, parentJob: Job, size: Long) :
            this(name ?: DEFAULT_JOB_NAME, props, userId, mutableSetOf(parentJob), size)

    constructor(name: String? = null, props: Map<PropertyKey, String> = mutableMapOf(), parentJobs: MutableSet<Job> = mutableSetOf(), size: Long) :
        this(name ?: DEFAULT_JOB_NAME, props, null, parentJobs, size)

    constructor(job: Job) : this(job.name, job.props, job.userId, job.parentJobs, job.size, job.autoclose) {
        endTimestamp = job.endTimestamp
        id = job.id
        size = job.size
        autoclose = job.autoclose
        lastUpdatedTimestamp = job.lastUpdatedTimestamp
        progress = job.progress
        startTimestamp = job.startTimestamp
        state = job.state
    }

    fun addParentJob(parentJob: Job) : Boolean {
        if (parentJobs == null) {
            parentJobs = mutableSetOf()
        }
        return parentJobs!!.add(parentJob)
    }

    fun removeParentJob(parentJob: Job) : Boolean? = parentJobs?.remove(parentJob)

    enum class PropertyKey {
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
        ;
    }

    /**
     * Convenience functions for updating props
     *
     * @param p - property to be updated
     */
    fun hasProp(p: PropertyKey): Boolean  = props.contains(p)
    fun setProp(p: PropertyKey, s: String){ props = props + mutableMapOf(p to s) }
    fun deleteProp(p: PropertyKey){ props = props - p }
    fun getProp(p: PropertyKey): String? = props[p]

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
        return "Job(name=$name, props=$props, userId=$userId, parentJobs=$parentJobs, state=$state, progress=$progress, size=$size, autoclose=$autoclose, startTimestamp=$startTimestamp, lastUpdatedTimestamp=$lastUpdatedTimestamp, endTimestamp=$endTimestamp, id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Job

        if (name != other.name) return false
        if (props != other.props) return false
        if (userId != other.userId) return false
        if (parentJobs != other.parentJobs) return false
        if (size != other.size) return false
        //Note: no need to check autoclose??
        if (state != other.state) return false
        if (progress != other.progress) return false
        if (startTimestamp != other.startTimestamp) return false
        // Note: last updated does not need to be in equals
        if (endTimestamp != other.endTimestamp) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + props.hashCode()
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (parentJobs?.hashCode() ?: 0)
        result = 31 * result + state.hashCode()
        result = 31 * result + autoclose.hashCode()
        result = 31 * result + progress.hashCode()
        result = 31 * result + (startTimestamp?.hashCode() ?: 0)
        // Note: last updated does not need to be in equals
        //result = 31 * result + (lastUpdatedTimestamp?.hashCode() ?: 0)
        result = 31 * result + (endTimestamp?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss").withZone(ZoneId.systemDefault())

        @JvmStatic
        fun getDateVersion(): String = formatter.format(Instant.now())
    }
}
