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
) : Predicate<DocumentJob> {
  /**
   * Tests if the document should be considered part of the batch.
   * @param documentJob
   * @return
   */
  override fun test(documentJob: DocumentJob): Boolean {
    return if(jobId != null) {
      jobId == documentJob.jobId
    } else if(jobName != null) {
      documentJob.jobName != null && jobName == documentJob.jobName
    } else if(userId != null) {
      (documentJob.hasProp(BaseDocumentJob.USER_ID)
              && userId == documentJob.getProp(BaseDocumentJob.USER_ID))
    } else {
      /* If neither is set, the query matches all jobs*/
      true
    }
  }

  open fun test(batchInfo: DocumentBatchInfo): Boolean {
    return if(jobId != null) {
      jobId == batchInfo.jobId
    } else if(jobName != null) {
      jobName == batchInfo.jobName
    } else if(userId != null) {
      userId == batchInfo.userId
    } else {
      /* If none are set, the query matches all batches */
      true
    }
  }

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
