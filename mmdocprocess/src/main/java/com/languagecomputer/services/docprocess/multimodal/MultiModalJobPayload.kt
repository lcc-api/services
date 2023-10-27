package com.languagecomputer.services.docprocess.multimodal

/**
 * @author smonahan
 */
class MultiModalJobPayload @JvmOverloads constructor(
  val payloadType: String,
  val textPayload: String? = null,
  val blobPayload: ByteArray? = null,
  val referencePayload: Boolean = false,
  val docId: String? = null,
  val props: Map<String, String> = mapOf(),
  val language: String? = null,
  val metaData: Map<String, String> = mapOf(),
) {
  init {
    check(textPayload != null || blobPayload != null) { "one of textPayload or blobPayload must not be null, but both are null." }
  }
}