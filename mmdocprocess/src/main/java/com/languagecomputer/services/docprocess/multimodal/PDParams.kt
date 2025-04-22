package com.languagecomputer.services.docprocess.multimodal

import javax.ws.rs.QueryParam

/**
 * @author stuart
 */
data class PDParams(
  // The title to give the document. If unspecified, will attempt to use the filename as the title (or TEMPORARY TITLE if not found).
  @field:QueryParam("title") var title: String? = null,
)
