package com.languagecomputer.services.filestore

import io.swagger.v3.oas.annotations.Operation
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Interface for uploading files to the LCC system.
 */
interface FileStore {
    @Operation(description = "Upload a binary files")
    @POST
    @Path("upload/{uuid}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    fun upload( @Context request : HttpServletRequest,
                @PathParam("uuid") uuid: String
    ): Response?

    @Operation(description = "Stream an mp4 video")
    @GET
    @Path("stream/{org}/{uuid}/{filename}")
    @Produces(MediaType.TEXT_PLAIN) //passthrough for json conversion
    fun stream(
        @PathParam("uuid") uuid: String,
        @PathParam("org") org: String,
        @PathParam("filename") filename: String,
        @HeaderParam(value = "Range") httpRangeList: String,
        @QueryParam("user") user: String,
        @QueryParam("token") token: String
    ): Response?

    @Operation(description = "Stream an audio file")
    @GET
    @Path("stream_audio/{org}/{uuid}/{filename}")
    @Produces(MediaType.TEXT_PLAIN) //passthrough for json conversion
    fun streamAudio(
        @PathParam("uuid") uuid: String,
        @PathParam("org") org: String,
        @PathParam("filename") filename: String,
        @HeaderParam(value = "Range") httpRangeList: String,
        @QueryParam("user") user: String,
        @QueryParam("token") token: String
    ): Response?
}