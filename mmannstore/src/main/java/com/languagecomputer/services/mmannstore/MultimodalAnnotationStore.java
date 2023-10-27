package com.languagecomputer.services.mmannstore;

import io.swagger.v3.oas.annotations.Operation;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author smonahan
 */
public interface MultimodalAnnotationStore extends MultimodalEntityExampleStore {

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
  @Path("/query/{normalize}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  AttributeStoreMessageList queryAttributeStoreMessage(AttributeStoreMessageQuery asmq, @PathParam("normalize") boolean normalize);

  @Operation(description ="Deletes a an Attribute Store message")
  @DELETE
  @Path("/deleteAttributeStoreMessage")
  void deleteAttributeStoreMessage(AttributeStoreMessage asm);

  @Operation(description = "Deletes the MMAnnotationStore tables")
  @DELETE
  @Path("/delete")
  void delete();

}
