package com.languagecomputer.services.docprocess

import com.languagecomputer.services.job.JobState

class DocumentJobStatus(
  val category: StatusCategory,
  val description: String? = null
) : Comparable<DocumentJobStatus?> {

  // Note: the ordering of the enum values matter since the compareTo depends upon it and any use of the compareTo function like Collections.min of this class.
  enum class StatusCategory {
    FAILED, UNPROCESSED, RETRIEVED, LIGHT, CORE, HEAVY, EXAMPLE, COMPLETED, WIKITOLOGY;

    companion object {
      @JvmStatic
      fun toJobState(documentStatus: StatusCategory): JobState {
        return when (documentStatus) {
          DocumentJobStatus.StatusCategory.COMPLETED -> JobState.COMPLETED
          DocumentJobStatus.StatusCategory.FAILED -> JobState.FAILED
          DocumentJobStatus.StatusCategory.UNPROCESSED -> JobState.WAITING
          else -> JobState.ONGOING
        }
      }
    }
  }

  constructor(category: StatusCategory) : this(category, "NONE")

  fun isUnsuccessfullyFinished(): Boolean {
    return this.category == StatusCategory.FAILED
  }

  fun isSuccessfullyFinished(): Boolean {
    return this.category == StatusCategory.COMPLETED
  }

  fun isFinished(): Boolean {
    return isSuccessfullyFinished() || isUnsuccessfullyFinished()
  }

  override operator fun compareTo(other: DocumentJobStatus?): Int {
    return this.category.compareTo(other!!.category)
  }

  override fun toString(): String {
    return "DocumentJobStatus(category=$category, description=$description)"
  }

  companion object {
    fun failed(reason: String?): DocumentJobStatus {
      return DocumentJobStatus(StatusCategory.FAILED, reason)
    }
  }
}
