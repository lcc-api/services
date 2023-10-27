package com.languagecomputer.services.docprocess

/**
 * message: The document message to be processed
 * updateTo: the processing level to process the document through
 * jobName/jobId: optional parameters to pre-name the job associated with the document.  If null, the document processing service will generate a job id and return it to you.
 */
class DocumentJob constructor(
  message: DocumentMessage?,
  updateTo: DocumentProcessingLevel?,
  props: Map<String, String?> = HashMap(),
  jobName: String? = null,
  jobId: Long? = null,
  modelMessage: ModelMessage? = null
) : BaseDocumentJob<String, DocumentMessage>(message, updateTo, props, modelMessage, jobName, jobId){

  override fun toString(): String {
    return "DocumentJob[jobName=$jobName,jobId=$jobId,updateTo=$updateTo,props=$props]"
  }

  class Builder: BaseBuilder<String, DocumentMessage, DocumentJob, Builder> {
    constructor()
    @JvmOverloads constructor(message: DocumentMessage, updateTo: DocumentProcessingLevel?=null) : super(message,updateTo)
    override fun build(): DocumentJob = DocumentJob(message, updateTo, props, jobName, jobId, modelMessage)
    override fun getThis(): Builder = this
    override fun document(document: DocumentMessage) = getThis().apply { this.message = document }

    companion object {
    @JvmStatic
    fun empty() = DocumentJob(null, null)
    }
  }
}
