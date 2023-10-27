package com.languagecomputer.services.docprocess

import java.time.Instant

/**
 * Document message with a byte array payload that can't be null
 * See @{link BaseDocumentMessage} for details.
 */
class RawDocumentMessage @JvmOverloads constructor(type: DocumentType, documentID: String, dateTimeString: String, payload: ByteArray,
                                                   url: String? = null, errorMessage: String? = null, user: String? = null) :
        BaseDocumentMessage<ByteArray>(type, documentID, dateTimeString, payload, url, errorMessage, user){
    @JvmOverloads
    constructor(type: DocumentType, documentID: String, dateTime: Instant?, payload: ByteArray, url: String? = null, errorMessage: String? = null, user:String? = null) :
            this(type, documentID,dateFormat.format(dateTime),payload, url, errorMessage, user)

    class Builder: BaseDocumentMessage.Builder<ByteArray, RawDocumentMessage, Builder>{
        //Note: not using constructor(type: DocumentType, documentID: String) since want payload to not be null

        //Note: uses BaseDocumentMessage<*> instead of BaseDocumentMessage<ByteArray> since might use different derived class of BaseDocumentMessage
        constructor(that: BaseDocumentMessage<*>, payload: ByteArray): super(that, payload)
        constructor(that: RawDocumentMessage): super(that)
        override fun build(): RawDocumentMessage {
            val payload: ByteArray = payload!!
            return RawDocumentMessage(type, documentID, dateTimeString!!, payload, url, errorMessage, user)
        }
        override fun getThis(): Builder = this
    }
}
