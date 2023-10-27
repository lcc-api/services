package com.languagecomputer.services.docprocess.multimodal;

data class SubmitVideoRequest (
  var uuid: String,
  var title: String,
  var url: String,
  var metadata: Map<String, String>,
)
// Add metadata field