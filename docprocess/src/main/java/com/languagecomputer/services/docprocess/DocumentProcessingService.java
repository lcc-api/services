package com.languagecomputer.services.docprocess;

import com.languagecomputer.services.job.Job;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Process and Update documents with gnerly from this service.
 * @author smonahan
 */
@OpenAPIDefinition(
    info=@Info(
        title = "Document Processing Interface",
        description = " Process and Update documents with gnerly from this service.",
        version = "1.0.0"
    )
)
@Path("/api/documentProcessing")
public interface DocumentProcessingService {

  /**
   * Update the heavy index for all the documents in the core index, and update the example versions where the NLP has changed.
   *
   * This is the same as updateDocuments(CORE, EXAMPLES/ADD_OR_UPDATE_ALL)
   * @return the job status indicator for the job this starts, since this method returns fast, while the processing is slow
   */
  @Operation(description = "Update the heavy index for all the documents in the core index, and update the example versions where the NLP has changed.")
  @GET
  @Path("update/all")
  @Produces(MediaType.APPLICATION_JSON)
  Job updateAllDocuments();

  /**
   * Update the index for all the documents at the specified level, and update the example versions pending the processing level
   * @param current - This is the level of the documents that we are going to update
   * @param dpconfig - For all the documents specified, apply dpconfig to update
   * @return the job status indicator for the job this starts, since this method returns fast, while the processing is slow
   */
  @Operation(description = "Update the index for all the documents at the specified level, and update the example versions pending the processing level")
  @POST
  @Path("update/{current}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  Job updateDocuments(@PathParam("current") DocumentProcessingLevel.Level current, DocumentProcessingConfig dpconfig);

  /**
   * Add a new document to the tiered indexes.
   * @param message - The document being added.
   */
  @Operation(description = "Add a new document to the tiered indexes.")
  @POST
  @Path("create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Job createNewDocument(DocumentJob message);

  /**
   * Add a bunch of documents to the tiered indexes.
   * @param messages - The list of the documents being added.
   */
  @Operation(description = "Add a bunch of documents to the tiered indexes.")
  @POST
  @Path("createMany")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Job createNewDocuments(List<DocumentJob> messages);

  @Operation(
      description = "Add document to index from stream",
      requestBody = @RequestBody(content = {@Content(schema = @Schema(implementation = CreateDocumentRequest.class))}))
  @POST
  @Path("createMultipart")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  Job createNewDocument(RawDocumentJob job);

  /**
   * Get summary information about processing documents.
   *
   * @param jobId If provided, only information regarding documents associated with this jobId is returned.  If not
   *              provided, information regarding all documents is returned.
   * @return      The summary information being returned is the number of documents in each queue.
   */
  @Operation(description = "Get summary information about processing documents.")
  @GET
  @Path("statuses")
  @Produces(MediaType.APPLICATION_JSON)
  DocumentProcessingStatus getStatus(@Nullable @QueryParam("jobId") Long jobId);

  /**
   * Get detailed information about processing documents
   *
   * @param jobId   if provided, return status information for documents in this job.  Otherwise, return information
   *                about all processing documents
   * @return        An object containing detailed, per-document status information, i.e. which queue each document is in,
   *                and which documents failed.
   */
  @Operation(description = "Get detailed information about processing documents.")
  @GET
  @Path("statusDetails")
  @Produces(MediaType.APPLICATION_JSON)
  DocumentProcessingStatusDetail getDetailedStatus(@Nullable @QueryParam("jobId") Long jobId);

  default DocumentProcessingStatus getStatus() {
    return getStatus(null);
  }

  @POST
  @Path("createOrUpdate")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(description = "Create or update document.")
  String createOrUpdateDocument(DocumentJob job, @QueryParam("type") String type, @QueryParam("doReprocess") boolean doReprocess);
}
