package com.languagecomputer.services.filestore

import io.swagger.v3.oas.annotations.Operation
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

data class Sizing(
    val width: Int,
    val height: Int,
)

data class MetaData(
    val mimeType: String,
    val fileSize: Long,
    val durationInMillis: Int?,
    val sizing: Sizing?,
)

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

    // TODO: rename method
    // TODO: add new method without the unedded parameters
    // The range is supporting https://developer.mozilla.org/en-US/docs/Web/HTTP/Range_requests
    @Operation(description = "Stream an mp4 video")
    @GET
    @Path("stream/{org}/{uuid}/{filename}")
    @Produces(MediaType.TEXT_PLAIN) //passthrough for json conversion, tells Spring not to de-jsonify this result
    @Deprecated("deprecated since 20240627")
    fun stream(
      @PathParam("uuid") uuid: String,
      @PathParam("org") orgIgnored: String,// org is now gotten from token. Parameter is left here to avoid breaking old calls.
      @PathParam("filename") filename: String,
      @HeaderParam(value = "Range") httpRangeList: String,
      @QueryParam("user") userIgnored: String,// user is now gotten from token. Parameter is left here to avoid breaking old calls.
      @QueryParam("token") token: String
    ): Response?

    // TODO: add new method with the unneeded parameters
    // The range is supporting https://developer.mozilla.org/en-US/docs/Web/HTTP/Range_requests
    @Operation(description = "Stream an audio file")
    @GET
    @Path("stream_audio/{org}/{uuid}/{filename}")
    @Produces(MediaType.TEXT_PLAIN) //passthrough for json conversion, tells Spring not to de-jsonify this result
    @Deprecated("deprecated since 20240627")
    fun streamAudio(
      @PathParam("uuid") uuid: String,
      @PathParam("org") orgIgnored: String,// org is now gotten from token. Parameter is left here to avoid breaking old calls.
      @PathParam("filename") filename: String,
      @HeaderParam(value = "Range") httpRangeList: String,
      @QueryParam("user") userIgnored: String,// user is now gotten from token. Parameter is left here to avoid breaking old calls.
      @QueryParam("token") token: String
    ): Response?

    @Operation(description = "Stream an mp4 video")
    @GET
    @Path("/v2/stream/uid/{uuid}/")
    @Produces(MediaType.TEXT_PLAIN) //passthrough for json conversion, tells Spring not to de-jsonify this result
    fun streamMP4Video(
        @PathParam("uuid") uuid: String,
        @HeaderParam(value = "Range") httpRangeList: String,
        @QueryParam("token") token: String?
    ): Response

    // TODO: fix operation
    // TODO: fix path
    // TODO: document the behavior here
    @Operation(description = "Lists files associated with given uuid. There can be more than 1 in the case where a video was uploaded, and audio was later steamed using the streamAudio() endpoint.")
    @GET
    // TODO is this comment still valid?
    //      Yes it is still valid.
    //      stream/{org}/{uuid}/{filename}
    //      would match with
    //      stream/uid/{uuid}/files
    //      as
    //      org=uid {uuid}={uuid} {filename}=files
    // this endpoint must be prepended with /v2 since otherwise it could be mistaken for the stream() method above.
    @Path("/v2/stream/uid/{uuid}/files")
    @Produces(MediaType.APPLICATION_JSON) //passthrough for json conversion, tells Spring not to de-jsonify this result
    fun listFiles(
      @PathParam("uuid") uuid: String,
    ): List<String>

    // TODO: fix path, should be "file" presumably
    // this method and the listFiles() method above are currently the only way to check if a file exists if you only know the file ID.
    // It also returns the name of the file which is needed by the stream() method above (knowing just the file ID is not sufficient)
    // However, this method will fail if the original file was a video and has been transcoded to audio due to a bug in the implementation.
    // if that happens, you'll need use the listFiles() method, and then use one of those filenames with the stream() method above
    // to retrieve the file.
    @Operation(description = "Gets the filename at the associated uuid")
    @GET
    // this endpoint must be prepended with /v2 since otherwise it could be mistaken for the stream() method above.
    @Path("/v2/stream/uid/{uuid}/filename")
    @Produces(MediaType.APPLICATION_JSON) //passthrough for json conversion, tells Spring not to de-jsonify this result
    fun getFileName(
      @PathParam("uuid") uuid: String,
    ): String


    // useful for getting the file type since none of the streaming methods can return the mime type in the http headers.
    @Operation(description = "Get the mime type of a file.")
    @GET
    @Path("/v2/file/{uuid}/mime_type")
    @Produces(MediaType.APPLICATION_JSON)
    fun getMimeType(
        @PathParam("uuid") uuid: String,
    ): String

    @Operation(description = "Get metadata stored in the file.")
    @GET
    @Path("/v2/file/{uuid}/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    fun getFileMetaData(
        @PathParam("uuid") uuid: String,
    ): MetaData

    @Operation(description = "Get attributes stored in the file.")
    @GET
    @Path("/v2/file/{uuid}/creationTime")
    @Produces(MediaType.APPLICATION_JSON)
    fun getCreationTime(@PathParam("uuid") uuid: String): String
}
