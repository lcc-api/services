package com.languagecomputer.services.docprocess

import java.nio.charset.Charset
import java.time.Instant

class RawDocumentJob(
        message: RawDocumentMessage?,
        updateTo: DocumentProcessingLevel?,
        props: Map<String, String?> = HashMap(),
        modelMessage: ModelMessage? = null,
        jobName: String? = null,
        jobId: Long? = null
) :
        BaseDocumentJob<ByteArray, RawDocumentMessage>(message, updateTo, props, modelMessage, jobName, jobId ){

    class Builder: BaseBuilder<ByteArray, RawDocumentMessage, RawDocumentJob, Builder> {
        constructor()
        @JvmOverloads constructor(message: RawDocumentMessage, level: DocumentProcessingLevel?=null) : super(message, level)
        override fun build(): RawDocumentJob  = RawDocumentJob(message, updateTo, props, modelMessage, jobName, jobId)
        override fun getThis(): Builder = this
        override fun document(document: RawDocumentMessage) = getThis().apply { this.message = document }

        companion object {
            @JvmStatic
            fun empty() = RawDocumentJob(RawDocumentMessage(DocumentType.DOCID, "EMPTY_DOCUMENT", Instant.now(), payload=ByteArray(0)), updateTo =null)
        }
    }

    fun toDocumentJob() =
        DocumentJob.Builder()
                .document(DocumentMessage.Builder(
                        this.message!!,
                        String(this.message!!.payload!!, Charset.forName("UTF-8")))
                        .build())
                .copyMetadata(this)
                .build()

}
