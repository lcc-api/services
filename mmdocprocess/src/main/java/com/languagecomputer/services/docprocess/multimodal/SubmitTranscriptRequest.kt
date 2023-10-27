package com.languagecomputer.services.docprocess.multimodal

data class SubmitTranscriptRequest (
  var uuid: String,
  var transcript: String,
)