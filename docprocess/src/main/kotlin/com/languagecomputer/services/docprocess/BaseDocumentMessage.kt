package com.languagecomputer.services.docprocess

import com.google.common.base.MoreObjects
import java.net.URL
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * type: The type of document, e.g. text, pdf
 * documentID: a unique identifier associated with the document
 * dateTimeString: the date time the document was written (used internally by LCC for temporal normalizatino)
 * payload: The raw payload of the document, e.g. the text.  Can be null for some document types, where the url or documentID indicate how the contents are meant to be loaded.
 * url:  the url where the document came from, if applicable
 * errorMessage: an error message that can be associated with the processing of the document, or if it is requested and is failed to be retrieved
 * user: The user who uploaded the document
 */
abstract class BaseDocumentMessage<T> internal constructor(
        val type: DocumentType,
        val documentID: String,
        val dateTimeString: String="",
        val payload: T?,
        val url: String?,
        var errorMessage: String?,
        var user: String?
  ) {

  fun hasPayload(): Boolean {
    return payload != null
  }

  fun dateTimeAsInstant(): Instant? {
    return if (dateTimeString.isEmpty()) Instant.now() else dateParse(dateTimeString)
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(BaseDocumentMessage::class.java)
            .add("type", type)
            .add("documentID", documentID)
            .add("dateTimeString", dateTimeString)
            .add("payload", payload?.toString() ?: "null")
            .add("url", url)
            .add("errormessage", errorMessage)
            .add("user", user)
            .toString()
  }

  abstract class Builder<U, V: BaseDocumentMessage<U>, W: Builder<U, V, W>> {
    protected var type: DocumentType
    protected var documentID: String
    protected var url: String? = null
    protected var payload: U? = null
    protected var dateTimeString: String? = null
    protected var errorMessage: String? = null
    protected var user: String? = null

    //Note: protected since abstract (Builder and BaseDocumentMessage)
    protected constructor(type: DocumentType, documentID: String) {
      this.type = type
      this.documentID = documentID
    }

    /**
     * copy constructor
     */
    //Note: protected since abstract (Builder and BaseDocumentMessage)
    protected constructor(that: BaseDocumentMessage<U>) {
      type = that.type
      documentID = that.documentID
      url = that.url
      dateTimeString = that.dateTimeString
      errorMessage = that.errorMessage
      this.payload = that.payload
    }

    /**
     * start with an existing document message and then make any changes as necessary
     */
    //Note: protected since abstract (Builder and BaseDocumentMessage)
    //Note: uses BaseDocumentMessage<*> instead of BaseDocumentMessage<U> since might use different derived class
    protected constructor(that: BaseDocumentMessage<*>, payload: U) {
      type = that.type
      documentID = that.documentID
      url = that.url
      dateTimeString = that.dateTimeString
      errorMessage = that.errorMessage
      user = that.user
      this.payload = payload
    }

    fun setErrorMessage(errorMessage: String?): W = getThis().apply { this.errorMessage = errorMessage }

    fun type(type: DocumentType): W = getThis().apply { this.type = type }

    fun docid(docid: String): W = getThis().apply { documentID = docid }

    fun url(url: String?): W = getThis().apply { this.url = url }

    fun url(url: URL): W  = getThis().apply { this.url = url.toString() }

    fun payload(payload: U?): W = getThis().apply { this.payload = payload }

    fun datetime(instant: Instant?): W = getThis().apply { dateTimeString = if (instant == null) null else dateFormat.format(instant) }

    fun datetime(datetime: String): W = getThis().apply { dateTimeString = datetime }

    fun setUser(user: String?): W = getThis().apply {this.user = user}

    abstract fun build(): V

    abstract fun getThis(): W
  }

  companion object {
    @JvmField
    val dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss"

    @JvmField
    val dateFormat = DateTimeFormatter.ofPattern(dateFormatPattern).withZone(ZoneId.of("UTC"))

    @JvmStatic
    fun dateParse(s: String?): Instant? = if (s == null) null else LocalDateTime.parse(s, dateFormat).atZone(ZoneId.of("UTC")).toInstant()
  }
}
