package com.languagecomputer.services.mmannstore

import io.swagger.v3.oas.annotations.Operation
import javax.annotation.Nonnull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * @apiDefine AuthHeader
 * @apiHeader (Auth Header) {String} Authorization Value is "Bearer YOUR_TOKEN". You can get YOUR_TOKEN from the Atessa website.
 */

/**
 * Used to store examples for multimodal entities.
 * <br></br>
 * Entity IDs officially GLOBAL now. (previously, they were local to the document in principle, but not in practice).
 * <br></br>
 * Notes for python developers:
 * <br/>
 * The @BeanParam annotation allows java/kotlin to package up query parameters in a cleaner way.
 * Look at those objects, and you will see what query parameters the method expects.
 * <br/>
 * e.g. @field:QueryParam("conceptName")  var conceptName: String? = null
 * <br/>
 * means it you can optionally specify "conceptName" as a query parameter for the request.
 *
 * @author stuart
 */
interface MultimodalEntityExampleStore {
  @Operation(description = "Returns list of Attribute examples based on query criteria. Criteria is conjunctive (all criteria must be met).")
  @GET
  @Path("/entities/attributes")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityAttributeExamples(
    @BeanParam query: MMAttributeExampleQuery
  ): List<EntityAttributeExample>

  /**
   * @api {get} /api/multimodalDS/entities/documents Query for files/documents with entities matching criteria
   * @apiName GetFilesWithMatchingEntities
   * @apiGroup Files
   *
   * @apiQuery {String} [conceptName ] Name of ontology concept entity has an attribute example for.
   * @apiQuery {String} [analytic ] Name of analytic that has been run on entity and produced attributes for.
   * @apiQuery {String} [minConfidence ] Minimum confidence of attributes to consider.
   *
   * @apiSuccess {String[]} _ List of file IDs containing entities matching given criteria.
   */
  @Operation(description = "Returns list of docids containing entities with specified criteria.")
  @GET
  @Path("/entities/documents")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityDocuments(
    @BeanParam query: MMEntityExampleDocumentQuery
  ): List<String>

  /**
   * @api {get} /api/multimodalDS/entities/documents Query for files/documents with entities matching criteria
   * @apiName GetFilesWithMatchingEntities
   * @apiGroup Files
   *
   * @apiQuery {String} [conceptName ] Name of ontology concept entity has an attribute example for.
   * @apiQuery {String} [analytic ] Name of analytic that has been run on entity and produced attributes for.
   * @apiQuery {String} [minConfidence ] Minimum confidence of attributes to consider.
   *
   * @apiSuccess {String[]} _ List of file IDs containing entities matching given criteria.
   */
  @Operation(description = "Returns list of entityids containing entities with specified criteria.")
  @GET
  @Path("/entities/ids")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityIds(
    @BeanParam query: MMEntityExampleDocumentQuery
  ): List<String>

  /**
   * @api {delete} /api/multimodalDS/file/:fileUUID - Deletes a multimodal document's entities, bboxes, relations, attributes, & ocr annotations.
   * @apiName deleteEntityAnnotationsForFile
   * @apiDescription Deletes a multimodal document's entities, bboxes, relations, attributes, & ocr annotations.
   * </br>
   * Note: This method only affects MMEntityExampleTables. It does not modify the multimodal document itself, just its annotations.
   * @apiUse AuthHeader
   * @apiParam {String} fileUUID - The UUID for the file.
   * @apiSuccess {String} - None.
   */
  @DELETE
  @Path("/file/{fileUUID}")
  fun deleteEntityAnnotationsForFile(@PathParam("fileUUID") fileUUID: String)

  /**
   * @api {post} /api/multimodalDS/file/:fileUUID/entity Create Entity for given file.
   * @apiName CreateEntity
   * @apiGroup Entity
   *
   * @apiParam {String} fileUUID The UUID for the file.
   *
   * @apiDescription Creates an entity for a given file.
   * </br>
   * Note: This method will not create any bounding boxes!
   *
   * @apiUse AuthHeader
   * @apiBody {String} [altName ] Human-readable string to be used for rendering purposes.
   *
   * @apiSuccess {String} _ ID of newly created entity.
   */
  @POST
  @Path("/file/{fileUUID}/entity")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun createEntity(
    @PathParam("fileUUID") fileUUID: String, createEntity: CreateEntity
  ): String

  /**
   * @api {post} /api/multimodalDS/file/:fileUUID/entity/:entityID/bbox Add Bounding Box to entity.
   * @apiName AddBoundingBox
   * @apiGroup Entity
   *
   * @apiDescription Associates BoundingBox with specified entity from specified file.
   * </br>
   * Note: If you want to get more fine-grained (e.g. the window of a car) you may need to create a new entity!
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} fileUUID The UUID for the file.
   * @apiParam {String} entityID The ID for the entity.
   *
   * @apiBody {Integer} [x1=0] X Coordinate of top left corner (for visual data).
   * @apiBody {Integer} [y1=0] Y Coordinate of top left corner (for visual data).
   * @apiBody {Integer} [x2=0] X Coordinate of bottom right corner (for visual data).
   * @apiBody {Integer} [y2=0] Y Coordinate of bottom right corner (for visual data).
   * @apiBody {Integer} [startTimeMillis=0] start time (for temporal data). 0 if non-temporal (e.g. an image).
   * @apiBody {Integer} [endTimeMillis=0] end time (for temporal data). 0 if non-temporal (e.g. an image).
   *
   * @apiSuccess {String} _ ID of the newly created BoundingBox
   */
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/bbox")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityBoundingBox(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
    @Nonnull box: CreateEntityBoundingBox
  ): String

  /**
   * @api {post} /api/multimodalDS/file/:fileUUID/entity/:entityID/bboxes Add Multiple Bounding Boxes to a single entity.
   * @apiName AddBoundingBoxes
   * @apiGroup Entity
   *
   * @apiDescription Associates Multiple BoundingBoxes with specified entity from specified file.
   * Prevents multiple API calls to AddBoundingBox.
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} fileUUID The UUID for the file.
   * @apiParam {String} entityID The ID for the entity.
   *
   * @apiBody {List} Bounding Box
   * @apiSuccess {String} _ ID of the last newly created BoundingBox of the list
   */
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/bboxes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityBoundingBoxes(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
    @QueryParam("updateVectors") updateVectors: Boolean,
    @Nonnull boxes: List<CreateEntityBoundingBox>
  ): String

  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/bbox/{bboxID}/judgements")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addJudgementToEntityBoundingBox(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
    @PathParam("bboxID") bboxId: String,
    @Nonnull judgement: CreateMMJudgement
  ): String

  @Operation(description = "Returns bounding boxes associated with specified entity.")
  @GET
  @Path("/file/{fileUUID}/entity/{entityID}/bboxes")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityBoundingBoxes(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String
  ): List<EntityBoundingBox>

  @Operation(description = "Returns bounding boxes for all entities in a file.")
  @GET
  @Path("/file/{fileUUID}/bboxes")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityBoundingBoxesForFile(
    @PathParam("fileUUID") fileUUID: String,
    @BeanParam query: MMDocumentBoundingBoxQuery,
  ): Map<String, List<EntityBoundingBox>>

  @Operation(description = "Returns altName for given entity. Will return null if altName was specified as null when entity was created.")
  @GET
  @Path("/file/{fileUUID}/entity/{entityID}/altName")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityAltName(
    // fileUUID is now defunct due to entity IDs officially being global
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
  ): String?

  @Operation(description = "Returns altName for given entity. Will return null if altName was specified as null when entity was created.")
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/altName/{entityConceptName}")
  @Produces(MediaType.APPLICATION_JSON)
  fun updateEntityAltName(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
    @PathParam("entityConceptName") entityConceptName: String,
  ): String?

  @Operation(description = "Returns all altNames for entities for given file..")
  @GET
  @Path("/file/{fileUUID}/entities/altNames")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityAltNames(
    @PathParam("fileUUID") fileUUID: String,
  ): Map<String, String?>

  /**
   * @api {post} /api/multimodalDS/file/:fileUUID/entity/:entityID/attribute Add Attribute to Entity.
   * @apiName AddAttribute
   * @apiGroup Entity
   *
   * @apiUse AuthHeader
   *
   * @apiDescription Assigns Attribute to Entity.
   * </br>
   * Note: If you want to get more fine-grained with attributes (e.g. the arm is yellow, but the leg is red)
   * </br>
   * <b>you are expected to create a new Entity</b> with a Part-Whole or Whole-Part relation!
   *
   * @apiParam {String} fileUUID The UUID for the file.
   * @apiParam {String} entityID The ID for the entity.
   *
   * @apiBody {String} conceptName Name of Ontology Concept Attribute corresponds to.
   * @apiBody {Double} confidence Score of extraction from analytic. The range of this value is specific to the analytic (i.e. not globally normalized).
   * @apiBody {String} analytic Analytic that extracted the attribute.
   *
   * @apiSuccess {String} _ ID of the newly created Attribute
   */
  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/attribute")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityAttributeExample(
    // fileUUID field will be ignored if supplied with "IGNORED".
    // if field is a different value it will still check to make sure the entity is in the file.
    @PathParam("fileUUID") fileUUIDIGNORED: String,
    @PathParam("entityID") entityID: String,
    @Nonnull example: CreateEntityAttributeExample
  ): String

  @POST
  @Path("/v2/entity/{entityID}/attribute")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityAttributeExampleV2(
    @PathParam("entityID") entityID: String,
    @Nonnull example: CreateEntityAttributeExample
  ): String

  @POST
  @Path("/file/{fileUUID}/entity/{entityID}/attribute/{attributeID}/judgements")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addJudgementToEntityAttribute(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
    @PathParam("attributeID") attributeID: String,
    @Nonnull judgement: CreateMMJudgement
  ): String

  @GET
  @Path("/attributes/judgements")
  @Produces(MediaType.APPLICATION_JSON)
  fun queryAttributeJudgements(
    @BeanParam query: MMJudgementQuery,
  ): List<MMJudgement>

  @DELETE
  @Path("/judgement/{judgementID}")
  fun deleteJudgementToEntityAttribute(
    @PathParam("judgementID") judgementID: String,
  ): String

  @GET
  @Path("/file/{fileUUID}/attributes/judgements")
  @Produces(MediaType.APPLICATION_JSON)
  fun getAttributeJudgementsForFile(
    @PathParam("fileUUID") fileUUID: String,
    @QueryParam("includeIncorrect") includeIncorrect: Boolean,
  ): List<MMJudgement>

  @GET
  @Path("/file/{fileUUID}/relations/judgements")
  @Produces(MediaType.APPLICATION_JSON)
  fun getRelationJudgementsForFile(
    @PathParam("fileUUID") fileUUID: String,
    @QueryParam("includeIncorrect") includeIncorrect: Boolean,
  ): List<MMJudgement>

  @GET
  @Path("/file/{fileUUID}/bboxes/judgements")
  @Produces(MediaType.APPLICATION_JSON)
  fun getBoundingBoxJudgementsForFile(
    @PathParam("fileUUID") fileUUID: String,
    @QueryParam("includeIncorrect") includeIncorrect: Boolean,
  ): List<MMJudgement>

  /**
   * @apiDefine EntityAttributeResponse
   *
   * @apiSuccess {String} id The Attribute's ID
   * @apiSuccess {String} entityId ID of the Entity this Attribute is attached to.
   * @apiSuccess {String} conceptName Name of Ontology Concept this Attribute corresponds to.
   * @apiSuccess {Double} confidence Confidence of extraction.
   * @apiSuccess {String} analytic Analytic that extracted this Attribute.
   */

  /**
   * @api {get} /api/multimodalDS/entities/attribute/:id Get Attribute from ID.
   * @apiName GetAttribute
   * @apiGroup Attributes
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} id The ID for the attribute.
   *
   * @apiUse EntityAttributeResponse
   */
  @GET
  @Path("/entities/attribute/{id}")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityAttributeExample(
    @PathParam("id") exampleId: String
  ): EntityAttributeExample

  /**
   * @api {get} /api/multimodalDS/file/:fileUUID/entities/attributes Query for Attributes in File.
   * @apiName QueryFileAttributes
   * @apiGroup Attributes
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} fileUUID The UUID for the file.
   *
   * @apiQuery {String} [conceptName ] Ontology Concept name.
   * @apiQuery {String} [entityID ] ID of Entity that Attribute must be attached to.
   * @apiQuery {Double} [minConfidence ] Minimum Confidence of Attribute. Note: confidences are not all on the same scale.
   * @apiQuery {String} [analytic ] Analytic that extracted the attribute(s).
   *
   * @apiSuccess {EntityAttribute[]} _ Array of EntityAttribute objects.
   */
  @GET
  @Path("/file/{fileUUID}/entities/attributes")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityAttributeExamplesForFile(
    @PathParam("fileUUID") fileUUID: String,
    @BeanParam query: MMDocumentAttributeExampleQuery
  ): List<EntityAttributeExample>

  // Relations
  @Operation(description = "Returns relation examples based on query criteria. Criteria is conjunctive (all criteria must be met).")
  @GET
  @Path("/entities/relations")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityRelationExamples(
    @BeanParam query: MMRelationExampleQuery
  ): List<EntityRelationExample>

  /**
   * @api {post} /api/multimodalDS/file/:fileUUID/entities/relation Add Relation between Entities
   * @apiName AddRelation
   * @apiGroup Entity
   *
   * @apiUse AuthHeader
   *
   * @apiDescription Adds relationship between two entities.
   * </br>
   * The <b>Head</b> is where the relatinship is "<b>from</b>".
   * </br>
   * The <b>Tail</b> is where the relationship is "<b>to</b>".
   * </br>
   * For example, with the relation "<b>Wheel attachedTo Truck</b>"
   * </br>
   * <b>Wheel</b> is the <b>Head</b> </br>
   * <b>attachedTo</b> is the <b>conceptName</b> of the relation type </br>
   * <b>Truck</b> is the <b>Tail</b>
   *
   * @apiParam {String} fileUUID The UUID for the file.
   *
   * @apiBody {String} headEntityID Head EntityID of the relationship.
   * @apiBody {String} tailEntityID Tail EntityID of the relationship.
   * @apiBody {String} conceptName Name of Ontology Concept relation corresponds to.
   * @apiBody {Double} confidence Name of Ontology Concept relation corresponds to.
   * @apiBody {String} analytic Name of what extracted the relation.
   *
   * @apiSuccess {String} _ ID of the newly created Relation
   */
  @POST
  @Path("/file/{fileUUID}/entities/relation")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addEntityRelationExample(
    @PathParam("fileUUID") fileUUID: String,
    example: CreateEntityRelationExample,
  ): String

  @POST
  @Path("/file/{fileUUID}/relation/{relationID}/judgements")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun addJudgementToEntityRelation(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("relationID") relationID: String,
    @Nonnull judgement: CreateMMJudgement
  ): String

  /**
   * @api {get} /api/multimodalDS/entities/relation/:id Get Relation given ID
   * @apiName GetRelation
   * @apiGroup Relation
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} id ID of the relation.
   *
   * @apiSuccess {String} id ID of the Relation.
   * @apiSuccess {String} headEntityID Head EntityID of the relationship.
   * @apiSuccess {String} tailEntityID Tail EntityID of the relationship.
   * @apiSuccess {String} conceptName Name of Ontology Concept relation corresponds to.
   * @apiSuccess {Double} confidence Name of Ontology Concept relation corresponds to.
   * @apiSuccess {String} analytic Name of what extracted the relation.
   */
  @GET
  @Path("/entities/relation/{id}")
  @Produces(
    MediaType.APPLICATION_JSON
  )
  fun getEntityRelationExample(
    @PathParam("id") exampleId: String
  ): EntityRelationExample

  /**
   * @api {get} /api/multimodalDS/file/:fileUUID/entities/relations Query for Relations from a File.
   * @apiName QueryFileRelations
   * @apiGroup Relation
   *
   * @apiUse AuthHeader
   *
   * @apiParam {String} fileUUID UUID of the file to search for Relations.
   *
   * @apiQuery {String} [conceptName ]
   * @apiQuery {String} [headEntityID ]
   * @apiQuery {String} [tailEntityID ]
   *
   * @apiSuccess {Relation[]} _ List of Relations from file matching query criteria.
   */
  @GET
  @Path("/file/{fileUUID}/entities/relations")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityRelationExamplesForFile(
    @PathParam("fileUUID") fileUUID: String,
    @BeanParam query: MMDocumentRelationExampleQuery
  ): List<EntityRelationExample>

  @Operation(description = "Returns all entities connected to a specified entity via a Whole-Part EntityExample Relationship.")
  @GET
  @Path("/file/{fileUUID}/entity/{entityID}/subEntities")
  @Produces(MediaType.APPLICATION_JSON)
  fun getSubEntities(
    @PathParam("fileUUID") fileUUID: String,
    @PathParam("entityID") entityID: String,
  ): Map<String, String?>

  @GET
  @Path("/entity/{entityID}/fileID")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntityFileID(@PathParam("entityID") entityID: String): String?

  @GET
  @Path("/file/{fileUUID}/entities")
  @Produces(MediaType.APPLICATION_JSON)
  fun getEntitiesForFile(@PathParam("fileUUID") fileUUID: String): List<MMEntity>

  // ** OCR Stuff **
  // saves OCR extraction to the database. Returns the ID of the extraction.
  @POST
  @Path("/file/{fileUUID}/ocrs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  fun createOCR(@PathParam("fileUUID") fileUUID: String, createOCR: CreateOCR): String

  // returns OCR extraction with the specified ID
  @GET
  @Path("/ocr/{ocrID}")
  @Produces(MediaType.APPLICATION_JSON)
  fun getOCR(@PathParam("ocrID") ocrID: String): MMOCR

  // Queries for OCR extractions. Query criteria is conjunctive (all non-null fields must match).
  @GET
  @Path("/ocrs")
  @Produces(MediaType.APPLICATION_JSON)
  fun queryOCR(@BeanParam query: MMOCRQuery): List<MMOCR>
  // end of OCR stuff
}
