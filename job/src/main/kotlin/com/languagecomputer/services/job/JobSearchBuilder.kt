package com.languagecomputer.services.job

import java.util.*

/**
 * Builder object to help construct a Job since many of the fields are required.
 * Builder to limit constructors to empty, all construction parameters, and copy constructor.
 * Builder class variables private to force use of other functions to follow builder pattern.
 */
class JobSearchBuilder (
  private var name: String?,
  private var props: MutableMap<JobPropertyKey, String>,
  private var parentJobId: Long? = null,
  private var jobIds: MutableCollection<Long>? = null,
  private var state: JobState?,
) {
  // Note: all other Job class variables are not used during construction.
  // Therefore, they are not in the Job.Builder class.

  constructor() : this(null, HashMap<JobPropertyKey, String>(), null, null, null)

  constructor(that: JobSearch) : this(that.name, that.props.toMutableMap(), that.parentJobId, that.jobIds?.toMutableList(), that.state)

  fun name(name: String) = apply { this.name = name }
  //Note: props set to private to avoid ambiguous duplicate setProps function
  fun setProps(props: Map<JobPropertyKey, String>) = apply { this.props.putAll(props) }
  fun addProp(p: JobPropertyKey, s: String) = apply { this.props += mapOf(p to s) }

    fun setParentJobId(parentJobId: Long) = apply { this.parentJobId = parentJobId }

  fun setJobIds(jobIds: Collection<Long>) = apply { this.jobIds = jobIds.toMutableSet() }
  fun addJobId(jobId: Long) = apply {
      if (this.jobIds == null) {
          this.jobIds = mutableSetOf()
      }
      this.jobIds?.plusAssign(mutableSetOf<Long>(jobId))
  }

  fun state(state: JobState) = apply { this.state = state }

  fun build() = JobSearch(name, props, parentJobId, jobIds, state)

  companion object {
      @JvmStatic
      fun namedJobSearch(name: String) = JobSearchBuilder().name(name).build()
  }
}
