package com.languagecomputer.services.docprocess

import com.google.common.collect.Lists
import com.languagecomputer.services.docprocess.DocumentBatchInfo.Companion.fromDocumentJob
import com.languagecomputer.services.docprocess.DocumentJobStatus.StatusCategory
import java.time.Instant
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors


class DocumentStatus private constructor(val docID: String?,
                                         var submissionName: String?,
                                         val batchInfo: DocumentBatchInfo?,
                                         var startTime: Long = Instant.now().toEpochMilli(),
                                         var progress: Double = 0.0,
                                         val submissionId: String? = docID,
                                         val pieceStatuses: List<DocumentJobStatus>) {
  init {
    submissionName = submissionName ?: submissionId
    setStatus()
  }

  var status: DocumentJobStatus? = null
  var endTime: Long = -1

  private fun setStatus() {
    status = if(pieceStatuses.size == 0) DocumentJobStatus(StatusCategory.UNPROCESSED) else Collections.min(pieceStatuses)
  }

  private fun <T> groupBy(groupFn: Function<DocumentJobStatus, T>): String {
    return pieceStatuses.stream().collect(Collectors.groupingBy(groupFn))
            .entries.stream().map { entry: Map.Entry<T, List<DocumentJobStatus?>> -> entry.key.toString() + ": " + entry.value.size }
            .collect(Collectors.joining(", "))
  }

  override fun toString(): String {
    return groupBy<StatusCategory>(Function{it.category})
  }

  fun toPrettyString(): String {
    return groupBy(Function { documentJobStatus: DocumentJobStatus -> if(documentJobStatus.isUnsuccessfullyFinished()) "Failed" else if(documentJobStatus.isSuccessfullyFinished()) "Complete" else "Ongoing" })
  }

  class Builder {
    private var docID: String? = null
    private var batchInfo: DocumentBatchInfo? = null
    private var submissionName: String? = null
    private var submissionId: String? = null
    private var progress: Double = 0.0
    private var startTime: Long = 0
    private var pieceStatuses: MutableList<DocumentJobStatus> = Lists.newArrayList()

    constructor(status: DocumentStatus) {
      copy(status)
    }

    constructor(job: DocumentJob) {
      fromJob(job)
    }

    constructor(docID: String?, batchInfo: DocumentBatchInfo?) {
      this.docID = docID
      this.batchInfo = batchInfo
      startTime = Date().time
    }

    fun docID(docID: String?): Builder {
      this.docID = docID
      return this
    }

    fun progress(progress: Double): Builder {
      this.progress = progress
      return this
    }

    fun submissionId(submissionId: String?): Builder {
      this.submissionId = submissionId
      return this
    }

    fun submissionName(submissionName: String?): Builder {
      this.submissionName = submissionName
      return this
    }

    fun startTime(startTime: Long): Builder {
      this.startTime = startTime
      return this
    }

    fun copy(status: DocumentStatus): Builder {
      docID = status.docID
      submissionName = status.submissionName
      batchInfo = status.batchInfo
      startTime = status.startTime
      progress = status.progress
      submissionId = status.submissionId
      pieceStatuses.clear()
      pieceStatuses.addAll(status.pieceStatuses)
      return this
    }

    fun fromJob(job: DocumentJob): Builder {
      docID = job.message!!.documentID
      submissionName = job.getProp(BaseDocumentJob.SUBMISSION_NAME)
      batchInfo = fromDocumentJob(job)
      startTime = job.getProp(BaseDocumentJob.PROCESSING_START_TIME)!!.toLong()
      submissionId = job.getProp(BaseDocumentJob.SUBMISSION_ID)
      progress = 0.0
      pieceStatuses = Lists.newArrayList()
      return this
    }

    private fun ensureSize(size: Int) {
      while(pieceStatuses.size < size) {
        pieceStatuses.add(DocumentJobStatus(StatusCategory.UNPROCESSED))
      }
    }

    fun setPieceStatus(pieceNumber: Int, pieceStatus: DocumentJobStatus) : Builder {
      ensureSize(pieceNumber + 1)
      pieceStatuses[pieceNumber] = pieceStatus
      return this
    }

    fun build(): DocumentStatus {
      val status = DocumentStatus(docID, submissionName, batchInfo, startTime, progress, submissionId, pieceStatuses)
      status.setStatus()

      if(status.status!!.isFinished()) {
        status.endTime = Instant.now().toEpochMilli()
      }
      return status
    }
  }
}

