package com.languagecomputer.services.job

/**
 * Criteria to filter jobs, used in searchJob request
 *
 * In general if a property is specified here, in order for a Job j to be returned as a search result,
 * a property of the same name must be set on j with the same value.  A null value for any of these properties
 * means jobs will not be filtered by their values for that property.
 *
 * @property name           name property must be present and equal to the provided value
 * @property props          If k -> v is present in props, then for any returned job j,
                                j.containsKey(k) && j.get(k) == v
                            If that condition doesn't hold, j doesn't appear in the results.
                            Jobs containing props keys which are not specified in the search are not filtered out;
                            i.e. if jobSearch.props is an empty map, the result set is unaffected
 * @property parentJobId    job must be a child of the job with id parentJobId
 * @property userId         userId must be present and equal to the provided value
 * @property state          state must be present and equal to the provided value
 */
data class JobSearch @JvmOverloads constructor(
    var name: String? = null,
    var props: Map<Job.PropertyKey, String> = mutableMapOf(),
    var parentJobId: Long? = null,

    var userId: String? = null,
    var state: JobState? = null) {

    //Note: constructor if searching for job(s) not by name
    @Suppress("unused")
    @JvmOverloads constructor(props: Map<Job.PropertyKey, String>, parentJobId: Long? = null, userId: String? = null, state: JobState? = null) :
            this(null, props, parentJobId, userId, state)

    //Note: constructor if searching for job(s) not by name nor properties
    @Suppress("unused")
    @JvmOverloads constructor(parentJobId: Long?, userId: String? = null, state: JobState? = null) :
            this(null, mutableMapOf(), parentJobId, userId, state)

    //Note: constructor if searching for job(s) using only user ID and Job state
    constructor(userId: String?, state: JobState?) :
            this(null, mutableMapOf(), null, userId, state)
}

