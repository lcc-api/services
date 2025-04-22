package com.languagecomputer.services.docprocess.multimodal;

import com.languagecomputer.services.multimodal.CreateTranscript;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author smonahan
 */
public interface MMDocProcessService {

  @POST
  @Path("/submitVideo")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  public MultiModalJobResponse submitVideo(SubmitVideoRequest request);

  /**
   * Method is Deprecated and will be removed in the future.
   * <br/>
   * Instead, you should use {@link lcc.services.documentstore.multimodal.MultiModalDocumentStore#createTranscript(CreateTranscript)}
   * followed by {@link MMDocProcessService#processDocument(String, PDParams)}
   */
  @POST
  @Path("/submitTranscript")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Deprecated
  public MultiModalJobResponse submitTranscript(SubmitTranscriptRequest request);

  @POST
  @Path("/process")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  MultiModalJobResponse processDocument(MultiModalJobPayload job);

  @GET
  @Path("/v2/doc/{id}/job")
  @Produces(MediaType.APPLICATION_JSON)
  MMJobStatus getJobStatusV2(@PathParam("id") String uid);

  @POST
  @Operation(description = "Creates processing job for document. " +
      "If document has been processed previously, only runs new processors (if any). " +
      "If document is currently being processed, no new job is started. " +
      "Instead, returns same object that getJobStatusV2() would.")

  @Path("/v2/doc/{id}/job")
  @Produces(MediaType.APPLICATION_JSON)
  MultiModalJobResponse processDocument(@PathParam("id") String fileOrDocumentUID, @BeanParam PDParams pdParams);

  @GET
  @Operation(description = "Queries for documents matching given processing criteria. Note: Documents that have never been processed or queued will not appear in these results.")
  @Path("/v2/jobs/docids")
  @Produces(MediaType.APPLICATION_JSON)
  List<String> queryJobs(@BeanParam MMDPJobQuery jobQuery);
}
