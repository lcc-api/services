package com.languagecomputer.services.docprocess.multimodal;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author smonahan
 */
public interface MMDocProcessService {

  @POST
  @Path("/submitVideo")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  public MultiModalJobResponse submitVideo(SubmitVideoRequest request);

  @POST
  @Path("/submitTranscript")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  public MultiModalJobResponse submitTranscript(SubmitTranscriptRequest request);

  @POST
  @Path("/process")
  @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
  MultiModalJobResponse processDocument(MultiModalJobPayload job);
}
