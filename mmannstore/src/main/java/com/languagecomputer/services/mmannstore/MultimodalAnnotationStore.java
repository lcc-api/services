
package com.languagecomputer.services.mmannstore;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author smonahan
 * Service strongly tied to the MultimodalDocumentStore.
 * Stores audio, video, & transcript annotations.
 * Note: annotations are not tied to entities
 */
public interface MultimodalAnnotationStore {

  @Operation(description = "Add an Attribute Store message")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/addAttributeStoreMessage")
  void addAttributeStoreMessage(AttributeStoreMessage asm);

  @Operation(description = "Add an Attribute Store message")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/addAttributeStoreMessage/bulk")
  void addAttributeStoreMessageList(List<AttributeStoreMessage> asm);

  @Operation(description = "Returns list of Attribute Store messages matching non-null query fields")
  @POST
  @Path("/query/list")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  AttributeStoreMessageList queryAttributeStoreMessageList(AttributeStoreMessageQuery asmq);

  @Operation(description = "Returns list of Attribute Store messages + statistics if they contain attributeValue key of score")
  @POST
  @Path("/query/statistics")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  AttributeStoreMessageList queryAttributeStoreMessageStatistics(AttributeStoreMessageQuery asmq);

  @Operation(description = "Returns map<documentIDs, map<Attributes, NormalizedScores>> matching non-null query fields")
  @POST
  @Path("/query/map")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  AttributeStoreMessageMap queryAttributeStoreMessageMap(AttributeStoreMessageQuery asmq);

  @Operation(description ="Deletes a an Attribute Store message")
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/deleteAttributeStoreMessage")
  void deleteAttributeStoreMessage(AttributeStoreMessage asm);

  @Operation(description = "Deletes the MMAnnotationStore tables")
  @DELETE
  @Path("/delete")
  void delete();

}
