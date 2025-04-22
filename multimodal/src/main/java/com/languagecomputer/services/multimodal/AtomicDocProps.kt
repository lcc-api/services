package com.languagecomputer.services.multimodal

/**
 * Some properties that should be common to atomic (i.e., not MultiMedia) documents
 */
interface AtomicDocProps {
    val parents: List<String>
}
