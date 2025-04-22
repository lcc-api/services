package com.languagecomputer.services.docprocess.multimodal

/**
 * @author stuart
 */
data class MMJobStatus(
  val docId: String,
  val jobId: Long,
  val succeeded: List<String>,
  val errored: List<ErrorReport>,
  val queued: List<String>,
) {
  val progressPercent: Double
    get() = if(totalStages == 0) { 100.0 } else {(errored.size + succeeded.size) * 100.0 / (totalStages)}
  val totalStages: Int
    get() = succeeded.size + errored.size + queued.size
}


enum class ErrorType {
  NOT_READY,
  DEPENDENCY_NOT_MET,
  UNSUPPORTED_MEDIA,
  MISSING_MEDIA, // e.g. for the case when a transcript has no backing audio/video
  EXCEPTION,
  MALFORMED_DATA,
  OTHER,
  SKIPPED,// used for now to show a document was skipped due to being hidden-to-normal-processors (or just "hidden")
  // and the processor not supporting hidden documents.
  // "hidden" documents are generally synthetic documents created for use by particular downstream processors.
}

data class ErrorReport(val processorID: String, val errorMessage: MMErrorMessage)

data class MMErrorMessage(
  val errorType: ErrorType,
  val errorMessage: String,
)
