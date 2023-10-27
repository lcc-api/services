package com.languagecomputer.services.docprocess

import com.languagecomputer.services.examplestore.ExampleUpdateStrategy


/**
 * A wrapper about a DocumentMessage which tells the DocumentProcessing service how to process this content.
 *
 * The various fields either give metadata about the job, or provide some information about how to track the job once submitted.
 * @author smonahan
 */
open class BaseDocumentJob<T, U: BaseDocumentMessage<T>> @JvmOverloads internal constructor(
  // The contents of the text to run the job over
  var message: U?,
  // The final level of processing to perform
  val updateTo: DocumentProcessingLevel?,
  var props: Map<String, String?> = mutableMapOf(),
  var modelMessage: ModelMessage? = null,
  var jobName: String? = null,
  var jobId: Long? = null,
  var stages: List<String>? = null // The stages of the pipeline to run (ending with level)
) {

    fun allPropKeys(): Set<String> {
        return props.keys
    }

    fun hasProp(propName: String): Boolean {
        return props[propName] != null
    }

    fun getProp(propName: String): String? {
        return props[propName]
    }

    abstract class BaseBuilder<W, X: BaseDocumentMessage<W>, Y: BaseDocumentJob<W, X>, Z: BaseBuilder<W, X, Y, Z>> {
        var message: X? = null
        var updateTo: DocumentProcessingLevel? = null
        var props: MutableMap<String, String?> = HashMap()
        var modelMessage: ModelMessage? = null
        var jobName: String? = null
        var jobId: Long? = null
        constructor()

        internal constructor(message: X, level: DocumentProcessingLevel?=null) {
            this.message = message
            this.updateTo = level
        }

        fun updateTo(updateTo: DocumentProcessingLevel?): Z = getThis().apply { this.updateTo = updateTo }
        fun light(): Z = getThis().apply { updateTo = DocumentProcessingLevel(DocumentProcessingLevel.Level.LIGHT, ExampleUpdateStrategy.NONE) }
        fun core(): Z = getThis().apply { updateTo = DocumentProcessingLevel(DocumentProcessingLevel.Level.CORE, ExampleUpdateStrategy.NONE) }
        fun heavy(): Z = getThis().apply { updateTo = DocumentProcessingLevel(DocumentProcessingLevel.Level.HEAVY, ExampleUpdateStrategy.NONE) }
        fun all(): Z = getThis().apply { updateTo = DocumentProcessingLevel(DocumentProcessingLevel.Level.EXAMPLE, ExampleUpdateStrategy.ADD_OR_UPDATE_ALL) }
        fun modelMessage(modelMessage: ModelMessage?): Z = getThis().apply { this.modelMessage = modelMessage }

        /**
         * Add a key/value property to the job. Duplicate keys will be overwritten.
         */
        fun addProp(key: String, value: String): Z = getThis().apply { props[key] = value }

        /**
         * Add key/value properties to the job. Duplicate keys will be overwritten.
         */
        fun addProps(keyValues: Map<String, String>): Z = getThis().apply { props.putAll(keyValues) }

        fun jobName(jobName: String): Z = getThis().apply { this.jobName = jobName }
        fun jobId(jobId: Long?): Z = getThis().apply { this.jobId = jobId }

        /**
         * @param job - Copy an old job, typically used for serial jobs where you copy the old job and update the message with the next level of processed document.
         */
        fun copy(job: BaseDocumentJob<W, X>): Z = getThis().apply { copyMetadata (job) } .apply { message = job.message }
        /**
         * @param job - Copy an old job, typically used for serial jobs where you copy the old job and update the message with the next level of processed document.
         */
        fun copyMetadata(job: BaseDocumentJob<*, *>): Z {
            updateTo = job.updateTo
            props = HashMap(job.props)
            modelMessage = job.modelMessage
            jobName = job.jobName
            jobId = job.jobId
            return getThis()
        }

        abstract fun document(document: X): Z

        abstract fun build(): Y

        protected abstract fun getThis(): Z
    }

    companion object {
        const val VERSION = "VERSION"
        const val LANGUAGE = "LANGUAGE"

        // Unique identifier for the service user
        const val USER_ID = "USER_ID"

        // For cases where we want the client to have a handle on a document processing job that is not the document id.
        // This is needed if the document id must be set on the server or is changed at all.
        const val SUBMISSION_ID = "SUBMISSION_ID"

        // This is the human-readable name of the document job for use by the client.
        const val SUBMISSION_NAME = "SUBMISSION_NAME"

        // File name of document being sent in.  Can be set by client to help in DocumentType detection
        const val FILE_NAME = "FILE_NAME"
        const val PROCESSING_START_TIME = "PROCESSING_START_TIME"

        // send directly to heavy, don't go through the earlier stages
        const val MINIMAL = "MINIMAL"

        // String specifying the total number of splits in the document and this split's order in the document of format
        // {order}/{total}
        const val SPLIT_FIELD = "SPLIT_FIELD"
        const val FORCE_EXAMPLE_STORE_ADD = "FORCE_EXAMPLE_STORE_ADD"
    }
}
