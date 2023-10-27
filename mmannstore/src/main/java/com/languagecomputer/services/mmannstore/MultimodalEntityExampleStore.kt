package com.languagecomputer.services.mmannstore

import io.swagger.v3.oas.annotations.Operation
import javax.annotation.Nonnull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Used to store examples for multimodal entities.
 * <br></br>
 * Entities are local to their documents, so the same ID used across multiple documents does not refer to the same entity.
 * <br></br>
 *
 * @author stuart
 */
interface MultimodalEntityExampleStore {
  @Operation(description = "")
  @GET
  @Path("/entities/attributes")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityAttributeExamples(
    @BeanParam query: MMAttributeExampleQuery
  ): List<EntityAttributeExample>

  @Operation(description = "")
  @POST
  @Path("/file/{fileUUID}/entity")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun createEntity(
    @PathParam("fileUUID") fileUUID: String, createEntity: CreateEntity
  ): Long

  @Operation(description = "")
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/bbox")
  @Consumes(
    MediaType.APPLICATION_JSON
  )
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityBoundingBox(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: Long,
    @Nonnull box: CreateEntityBoundingBox
  ): Long

  @Operation(description = "")
  @GET
  @Path("/file/{fileUUID}/entity/{entityID}/bboxes")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityBoundingBoxes(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: Long
  ): List<EntityBoundingBox>

  @Operation(description = "")
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/attribute")
  @Consumes(
    MediaType.APPLICATION_JSON
  )
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityAttributeExample(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: Long,
    @Nonnull example: CreateEntityAttributeExample
  ): Long

  @Operation(description = "")
  @GET
  @Path("/entities/attribute/{id}")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityAttributeExample(
    @PathParam("id") exampleId: Long
  ): EntityAttributeExample

  @Operation(description = "")
  @GET
  @Path("/file/{fileUUID}/entities/attributes")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityAttributeExamplesForFile(
    @PathParam("fileUUID") fileUUID: String,
    @BeanParam query: MMDocumentAttributeExampleQuery
  ): List<EntityAttributeExample>

  // Relations
  @Operation(description = "")
  @GET
  @Path("/entities/relations")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityRelationExamples(
    @BeanParam query: MMRelationExampleQuery
  ): List<EntityRelationExample>

  @Operation(description = "")
  @POST
  @Path("/file/{fileUUID}/entities/relation")
  @Consumes(
    MediaType.APPLICATION_JSON
  )
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityRelationExample(
    @PathParam("fileUUID") fileUUID: String,
    example: CreateEntityRelationExample,
  ): Long

  @Operation(description = "")
  @GET
  @Path("/entities/relation/{id}")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityRelationExample(
    @PathParam("id") exampleId: Long
  ): EntityRelationExample

  @Operation(description = "")
  @GET
  @Path("/file/{fileUUID}/entities/relations")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityRelationExamplesForFile(
    @PathParam("fileUUID") fileUUID: String,
    @BeanParam query: MMDocumentRelationExampleQuery
  ): List<EntityRelationExample>
}
