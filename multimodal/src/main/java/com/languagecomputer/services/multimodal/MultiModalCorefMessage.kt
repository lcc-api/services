package com.languagecomputer.services.multimodal

import com.languagecomputer.services.multimodal.BoundingBox

data class MultiModalCorefMessage (
  val id: String?, //a unique identifier, //not sure how these are managed
  val name: String? //a non unique identifier
)