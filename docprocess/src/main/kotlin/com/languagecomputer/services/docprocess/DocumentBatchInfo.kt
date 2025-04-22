package com.languagecomputer.services.docprocess

import com.google.common.base.MoreObjects
import java.util.function.Predicate


/**
 * Data associated with a tracked group of document jobs.
 *
 * Being used to specify a subset of document jobs about which a subscriber wants to receive updates.
 */
open class DocumentBatchInfo @JvmOverloads constructor(
  var jobId: Long?,
  var jobName: String?,
  var userId: String? = null
) {

  override fun toString(): String {
    return MoreObjects.toStringHelper(this.javaClass).add("jobId", jobId).add("userId", userId)
            .add("jobName", jobName).toString()
  }

  companion object {
    @JvmStatic
    fun <T,U: BaseDocumentMessage<T>> fromDocumentJob(documentJob: BaseDocumentJob<T, U>): DocumentBatchInfo {
      return DocumentBatchInfo(documentJob.jobId,
                               documentJob.jobName,
                               documentJob.getProp(BaseDocumentJob.USER_ID))
    }
  }
}
