package com.languagecomputer.services.docprocess

import java.time.Instant
import java.util.*


/**
 * This file tells the DocumentProcessing service how to perform an update job.
 */
class DocumentProcessingConfig(val updateTo: DocumentProcessingLevel, val isSkippedCheck: Boolean) {
  var updateTime: Instant? = null
  var docids: List<String>? = null
  var numDocs: Int? = null
  var docIdPattern: String? = null

  /**
   * @param updateTo - Update documents in the current indexes up to this level
   * @param skippedCheck - check if they are already in the higher index before processing
   * @param updateTime - only process documents that have were last updated before this time
   */
  constructor(updateTo: DocumentProcessingLevel, skippedCheck: Boolean, updateTime: Instant?) : this(updateTo, skippedCheck) {
    this.updateTime = updateTime
    require(!(updateTime != null && skippedCheck)) { "updateTime is not null and skippedCheck is true" }
    docids = null
    numDocs = null
  }

  constructor(updateTo: DocumentProcessingLevel, skippedCheck: Boolean, numDocs: Int?) : this(updateTo, skippedCheck) {
    this.numDocs = numDocs
    docids = null
    updateTime = null
  }

  /**
   * @param updateTo - Update documents in the current indexes up to this level
   * @param docids - A list of document ids to update.
   */
  constructor(updateTo: DocumentProcessingLevel, docids: Collection<String>?) : this(updateTo, false, docids)
  constructor(updateTo: DocumentProcessingLevel, skippedCheck: Boolean, docids: Collection<String>?) : this(updateTo, skippedCheck) {
    updateTime = null
    this.docids = ArrayList(docids)
    numDocs = null
  }

  /**
   * @param updateTo - Update documents in the current indexes up to this level
   * @param docIdPattern - A pattern string
   */
  constructor(updateTo: DocumentProcessingLevel, docIdPattern: String?) : this(updateTo, false, docIdPattern)
  constructor(updateTo: DocumentProcessingLevel, skippedCheck: Boolean, docIdPattern: String?) : this(updateTo, skippedCheck) {
    updateTime = null
    this.docIdPattern = docIdPattern
    numDocs = null
  }

  override fun toString(): String {
    return "DocumentProcessingConfig(updateTo=$updateTo, isSkippedCheck=$isSkippedCheck, updateTime=$updateTime, docids=$docids, numDocs=$numDocs, docIdPattern=$docIdPattern)"
  }
}
