package com.languagecomputer.services.docprocess.multimodal

import javax.ws.rs.QueryParam

data class MMDPJobQuery(
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("status") var status: MMJobStatusEnum? = null,
  @field:QueryParam("errorType") var errorType: ErrorType? = null,
)


enum class MMJobStatusEnum {
  SUCCEEDED,
  ERRORED,
  QUEUED,
}