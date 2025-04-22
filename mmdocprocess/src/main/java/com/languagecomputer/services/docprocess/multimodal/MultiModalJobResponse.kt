package com.languagecomputer.services.docprocess.multimodal

/**
 * Multimodal job will return jobid for tracking and docid in case the docid was automatically assigned.
 * @author stuart
 */
data class MultiModalJobResponse(val jobId: Long, val docid: String)
