package com.languagecomputer.services.docprocess

import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.stream.StreamSupport

class DocumentResult(val documentID: String, val title: String, val properties: MutableMap<String, String?> = HashMap()) {
  fun getProp(name: String?): String? {
    return properties[name]
  }

  fun hasProp(name: String?): Boolean {
    return properties.containsKey(name)
  }

  override fun toString(): String {
    return "DocumentResult{" +
            "documentID='" + documentID + '\'' +
            ", title='" + title + '\'' +
            '}'
  }

  companion object {
    const val DATE_INDEXED = "DATE_INDEXED"
    const val HEADLINE = "headline"
  }
}

class DocumentResultList(val requestID: String? = null,
                         val results: MutableList<DocumentResult> = ArrayList(),
                         val error : Boolean = false,
                         val errorMessage: String? = null ) : Iterable<DocumentResult> {

  /**
   * @param requestID unique ID for the request
   * @param results collection of Document Results
   */
  constructor(requestID: String?, results: MutableCollection<DocumentResult>) :
          this(requestID, ArrayList(results), false, null)

  override fun iterator(): Iterator<DocumentResult> {
    return this.results.iterator()
  }

  /**
   * Get the document ID for each result and return them as a list.
   * @return A List of String document IDs.
   */
  fun getDocIDs(): List<String> {
    return stream().map(DocumentResult::documentID).collect(Collectors.toList<String>())
  }

  /**
   * @return a stream of the Document Results
   */
  fun stream(): Stream<DocumentResult> {
    return StreamSupport.stream(spliterator(), false)
  }
}
