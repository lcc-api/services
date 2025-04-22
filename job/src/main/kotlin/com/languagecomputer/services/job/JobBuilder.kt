package com.languagecomputer.services.job

import java.util.*

/**
 * Builder object to help construct a Job since many of the fields are required.
 * Builder to limit constructors to empty, all construction parameters, and copy constructor.
 * Builder class variables private to force use of other functions to follow builder pattern.
 */
class JobBuilder (
  private var id: Long? = null,
  private var name: String,
  private var props: MutableMap<JobPropertyKey, String>,
  private var parentJobs: MutableSet<Long>?,
  private var size: Long,
  private var autoclose: Boolean,
  private var state: JobState = JobState.WAITING,
  private var progress: Double = 0.0,
  private var startTimestamp: Long? = null,
  private var lastUpdatedTimestamp: Long? = null,
  private var endTimestamp: Long? = null
) {
  // Note: all other Job class variables are not used during construction.
  // Therefore, they are not in the Job.Builder class.

  constructor() : this(null, DEFAULT_JOB_NAME, HashMap<JobPropertyKey, String>(), null, 1, true)

  constructor(that: Job) : this(that.id, that.name, that.props, that.parentJobs, that.size,
      that.autoclose, that.state, that.progress, that.startTimestamp,
      that.lastUpdatedTimestamp, that.endTimestamp)

  fun id(id: Long) = apply { this.id = id }
  fun name(name: String) = apply { this.name = name }
  //Note: props set to private to avoid ambiguous duplicate setProps function
  fun setProps(props: Map<JobPropertyKey, String>) = apply { this.props.putAll(props) }
  fun addProp(p: JobPropertyKey, s: String) = apply { this.props += mapOf(p to s) }
  fun setParentJobs(parentJobs: Set<Long>) = apply { this.parentJobs = parentJobs.toMutableSet() }
  fun addParentJob(parentJob: Long) = apply {
      if (this.parentJobs == null) {
          this.parentJobs = mutableSetOf()
      }
      this.parentJobs?.plusAssign(mutableSetOf<Long>(parentJob))
  }
  fun size(size: Long) = apply { this.size = size }
  fun autoclose(autoclose: Boolean) = apply { this.autoclose = autoclose }
  fun startTimestamp(date: Long?) = apply { this.startTimestamp = date }
  fun getStartTimestamp() : Long? = startTimestamp
  fun lastUpdatedTimestamp(date: Long?) = apply { this.lastUpdatedTimestamp = date }
  fun endTimestamp(date: Long?) = apply { this.endTimestamp = date }
  fun state(state: JobState) = apply { this.state = state }
  fun progress(progress: Double) = apply { this.progress = progress }

  fun build() = Job(id, name, props, parentJobs, size, autoclose, state, progress, startTimestamp, lastUpdatedTimestamp, endTimestamp)

  companion object {
      @JvmStatic
      fun namedJob(name: String) = JobBuilder().name(name).build()
  }
}
