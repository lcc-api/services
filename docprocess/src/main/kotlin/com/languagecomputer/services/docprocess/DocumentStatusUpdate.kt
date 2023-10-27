package com.languagecomputer.services.docprocess

class DocumentStatusUpdate(val submissionId: String?,
                           val submissionName: String?,
                           var statusDetail: DocumentProcessingStatusDetail?,
                           val status: DocumentJobStatus,
                           val pieceNumber: Int,
                           var progress: Double,
        /* This is needed if we, say, subscribe to all jobIds under a jobName (type) and want to know which jobId this update
* is coming from.  */
                           val batchInfo: DocumentBatchInfo?)
