package com.languagecomputer.services.docprocess

import com.languagecomputer.services.docprocess.DocumentJobStatus.StatusCategory
import java.util.stream.Collectors

class DocumentProcessingStatus(val statusMap: Map<String, Int>)

class DocumentProcessingStatusDetail(val statuses: MutableList<DocumentStatus> = ArrayList()) {
  fun add(dsm: DocumentStatus) {
    statuses.add(dsm)
  }

  override fun toString(): String {
    return statuses.stream()
            .filter { status: DocumentStatus -> status.status != null }
            .map { status: DocumentStatus -> status.status!!.category }
            .collect(Collectors.groupingBy { x: StatusCategory? -> x })
            .map { entry ->  String.format("%s: %d", entry.key.toString(), entry.value.size) }
            .joinToString(", ")
  }
}



