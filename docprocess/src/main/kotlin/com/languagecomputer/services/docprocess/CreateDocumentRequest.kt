@file:Suppress("ArrayInDataClass")

package com.languagecomputer.services.docprocess

import io.swagger.v3.oas.annotations.media.Schema

/**
 * This is here solely to define the request format for the multipart create
 * document request, so the correct OpenAPI definition is generated.
 * */
data class CreateDocumentRequest(val metadata: DocumentJob, @field:Schema(type="string", format="binary") val payload: ByteArray)