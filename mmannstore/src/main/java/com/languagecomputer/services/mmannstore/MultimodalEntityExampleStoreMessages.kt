package com.languagecomputer.services.mmannstore

import com.languagecomputer.services.multimodal.BoundingBox
import com.languagecomputer.services.multimodal.SimpleBoundingBox
import com.languagecomputer.services.multimodal.Timed
import javax.ws.rs.QueryParam

/**
 * @author stuart
 */
data class CreateEntity @JvmOverloads constructor(
  val altName: String? = null,// this need not be unique
)

const val RUBY_RED_GRAPEFRUIT = "#e85b51"

data class EntityBoundingBox(
  val id: String,
  val entityId: String,
  override val x1: Int = 0,
  override val x2: Int = 0,
  override val y1: Int = 0,
  override val y2: Int = 0,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
  val color: String =  RUBY_RED_GRAPEFRUIT,
  val conceptName: String? = null,
  val score: Double = 0.0,
): Timed, SimpleBoundingBox

data class CreateEntityBoundingBox @JvmOverloads constructor(
  override val x1: Int = 0,
  override val x2: Int = 0,
  override val y1: Int = 0,
  override val y2: Int = 0,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
): Timed, SimpleBoundingBox {

  constructor(mmBox: BoundingBox): this(
    mmBox.x1,
    mmBox.x2,
    mmBox.y1,
    mmBox.y2,
    mmBox.startTimeMillis,
    mmBox.endTimeMillis
  )

  constructor(mmBox: EntityBoundingBox): this(
    mmBox.x1,
    mmBox.x2,
    mmBox.y1,
    mmBox.y2,
    mmBox.startTimeMillis,
    mmBox.endTimeMillis
  )

  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(startTimeMillis <= endTimeMillis)
    // note: bounding boxes are inclusive for their bounds,
    // so if x1 == x2 and y1 == y2 it is a 1 pixel bounding box.
    check(x1 <= x2)
    check(y1 <= y2)
  }
}



interface EntityExample: Timed, AnalyticProvenance {
  val id: String
  //maybe this should be a SOR - but...
  //conceptType is not a one-one mapping to conceptClass, for properties we want something lower level
  //for entities possibly as well - arm vs. shirt
  val conceptName: String
  val conceptLabel: String
  val conceptType: String
  val fileUUID: String
  // Value is 0 for non-temporal data like images.
  // the time is relative to the start of the media NOT the real-world time.
  override val startTimeMillis: Int
  // Value is 0 for non-temporal data like images.
  // the time is relative to the start of the media NOT the real-world time.
  override val endTimeMillis: Int
  val confidence: Double
}

/**
 * Message used to associate an ontology concept with a file in the file store.
 *
 * The [conceptName] is the ontology concept,
 * [fileUUID] the file associated with the concept
 */
data class CreateEntityAttributeExample @JvmOverloads constructor(
  val conceptName: String,
  val confidence: Double,
  override val analytic: String, // what created this entity attribute example
  override val startTimeMillis: Int = 0,// null for non-temporal data like images
  override val endTimeMillis: Int = 0,// null for non-temporal data like images
): Timed, AnalyticProvenance {
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(endTimeMillis >= startTimeMillis)
  }
}

data class EntityAttributeExample(
  val entityId: String,
  override val id: String,
  override val conceptName: String,
  override val conceptLabel: String,
  override val conceptType: String,
  override val fileUUID: String,
  override val confidence: Double,
  override val analytic: String,
  override val startTimeMillis: Int = 0,// 0 for non-temporal data like images
  override val endTimeMillis: Int = 0,// 0 for non-temporal data like images
  val color: String =  RUBY_RED_GRAPEFRUIT,
): EntityExample {
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(endTimeMillis >= startTimeMillis)
  }
}

fun EntityAttributeExample.getVectorID(): String {
  return id.toString();
}

/**
 * Message used to associate an ontology concept with a file in the file store.
 *
 * The [conceptName] is the ontology concept,
 */
data class CreateEntityRelationExample(
  val headEntityID: String,
  val tailEntityID: String,
  val conceptName: String,
  val confidence: Double,
  override val analytic: String,
  override val startTimeMillis: Int = 0,// 0 for non-temporal data like images
  override val endTimeMillis: Int = 0,// 0 for non-temporal data like images
): Timed, AnalyticProvenance {
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(endTimeMillis >= startTimeMillis)
  }
}

data class EntityRelationExample(
  val headEntityID: String,
  val tailEntityID: String,
  override val id: String,
  override val conceptName: String,
  override val conceptLabel: String,
  override val conceptType: String,
  override val fileUUID: String,
  override val confidence: Double,
  override val analytic: String,
  override val startTimeMillis: Int = 0,// 0 for non-temporal data like images
  override val endTimeMillis: Int = 0,// 0 for non-temporal data like images
): EntityExample {
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(endTimeMillis >= startTimeMillis)
  }
}

data class MMAttributeExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("entityId") var entityId: String? = null,
  // judgement is a string corresponding to MMJudgementValue, or the string "NULL" if wanting un-annotated stuff
  @field:QueryParam("judgement") var judgement: String? = null,
  @field:QueryParam("limit") var limit: Int? = null,
)

data class MMEntityExampleDocumentQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("altName") var altName: String? = null,
  @field:QueryParam("entityId") var entityId: String? = null,
  @field:QueryParam("includeNeg") var includeNegs: Boolean? = null,
)

data class MMDocumentAttributeExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("entityID") var entityID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("includeNegs") var includeNegs: Boolean? = null,
)

data class MMDocumentBoundingBoxQuery @JvmOverloads constructor(
  @field:QueryParam("entityID") var entityID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("includeIncorrects") var includeIncorrects: Boolean = false,
)

data class MMRelationExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  // Entity ID not supported for this query since entity IDs are local to a file.
)

data class MMDocumentRelationExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("headEntityID") var headEntityID: String? = null,
  @field:QueryParam("tailEntityID") var tailEntityID: String? = null,
  // if entityID is given, the headEntityID and tailEntityID params are ignored.
  @field:QueryParam("entityID") var entityID: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("limit") var limit: Int? = null,
)

enum class MMJudgementValue {
  CORRECT,
  INCORRECT,
}

data class CreateMMJudgement(
  val judgement: MMJudgementValue,
)

data class MMJudgementQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  // if false, gets only judgements from the request user.
  // if true,  gets all  judgements the request user has permissions to view
  @field:QueryParam("includeAllUsers") var includeAllUsers: Boolean = false,
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("judgement") var judgement: MMJudgementValue? = null,
  @field:QueryParam("attributeId") var attributeId: String? = null,
)

data class MMJudgement(
  val id: String,
  val exampleID: String,
  val fileUUID: String,
  val user: String,
  val judgement: MMJudgementValue,
  // for Relations entityID is the head entity ID
  val entityID: String,
)

data class MMEntity(
  val id: String,
  val altName: String?
)

data class JudgmentsContainer(
  val attributes: List<MMJudgement>,
  val relations: List<MMJudgement>,
  val boundingBoxes: List<MMJudgement>,
)

data class SceneGraph(
  val fileUUID: String,
  val entities: List<MMEntity>,
  val attributes: List<EntityAttributeExample>,
  val relations: List<EntityRelationExample>,
  val boundingBoxes: List<EntityBoundingBox>,
  val judgements: JudgmentsContainer,
)

data class SceneGraphOptions @JvmOverloads constructor(
  @field:QueryParam("includeIncorrects") var includeIncorrects: Boolean = false,
  @field:QueryParam("includeInferences") var includeInferences: Boolean = false,
)

data class SceneGraphRequest(
  val fileIDs: List<String>,
  val options: SceneGraphOptions,
)

/**
 * Classes associated with Creating, Retrieving, and Query-ing for OCR extractions.
 * OCR = Optical Character Recognition.
 * <br>
 * It is assumed that this will just be used for images/video as
 * Transcripts and Text Documents are stored in their own (bespoke) way.
 */
data class CreateOCR @JvmOverloads constructor(
  val entityID: String,
  val text: String,
  override val analytic: String,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
): Timed, AnalyticProvenance

data class MMOCR(
  val id: String,
  val text: String,
  override val startTimeMillis: Int,
  override val endTimeMillis: Int,
  val entityID: String,
  val docID: String,
  override val analytic: String,
): Timed, AnalyticProvenance

data class MMOCRQuery @JvmOverloads constructor(
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  @field:QueryParam("entityId") var entityId: String? = null,
  @field:QueryParam("limit") var limit: Int? = null,
)
// End of OCR classes.
