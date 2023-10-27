package com.languagecomputer.services.docprocess

import java.time.Instant

/**
 * Document message with a String payload that can be null
 * See @{link BaseDocumentMessage} for details.
 */
class DocumentMessage @JvmOverloads constructor(type: DocumentType, documentID: String,
                                                dateTimeString: String, payload: String? = null,
                                                url: String? = null, errorMessage: String? = null, user: String? = null) :
    BaseDocumentMessage<String>(type, documentID, dateTimeString, payload, url, errorMessage, user) {

    @JvmOverloads
    constructor(type: DocumentType, documentID: String,
                documentDate: Instant? = null, payload: String? =null,
                url: String? = null, errorMessage: String? = null, user: String? = null) :
            this(type, documentID, dateFormat.format(documentDate ?: Instant.now()), payload,
               url, errorMessage, user)

    fun getPayloadSize(): Int {
      if(hasPayload()) {
        return payload!!.length
      }
      return 0
    }
    class Builder : BaseDocumentMessage.Builder<String, DocumentMessage, Builder> {
        constructor(type: DocumentType, documentID: String) : super(type, documentID)
        //Note: uses BaseDocumentMessage<*> instead of BaseDocumentMessage<String> since might use different derived class of BaseDocumentMessage
        constructor(that: BaseDocumentMessage<*>, payload: String) : super(that, payload)
        //Note: if payload is null, it will continue to be null
        constructor(that: DocumentMessage) : super(that)
        override fun build(): DocumentMessage {
            return if (dateTimeString == null){
                DocumentMessage(type, documentID, Instant.now(),
                        payload, url, errorMessage, user)
            } else {
                DocumentMessage(type, documentID, dateTimeString!!,
                        payload, url, errorMessage, user)
            }
        }

        override fun getThis(): Builder {
            return this
        }
    }

    fun toBuilder(): Builder {
        return Builder(this)
    }

    companion object {
        @JvmStatic
        fun simpleMessage(
                type: DocumentType,
                docId: String,
                payload: String
        ) = DocumentMessage(type = type, documentID = docId, payload = payload)
    }
}