package com.languagecomputer.services.multimodal

/**
 * @author stuart
 */
data class SpeakerMapping(
  val source: String,
  val mappings: Map<String, String>,
)
