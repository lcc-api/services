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

data class EntityBoundingBox(
  val id: Long,
  override val x1: Int = 0,
  override val x2: Int = 0,
  override val y1: Int = 0,
  override val y2: Int = 0,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
  val subFileName: String? = null,
): Timed, SimpleBoundingBox

data class CreateEntityBoundingBox(
  override val x1: Int = 0,
  override val x2: Int = 0,
  override val y1: Int = 0,
  override val y2: Int = 0,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
  val subFileName: String? = null,
): Timed, SimpleBoundingBox {

  constructor(mmBox: BoundingBox, subFileName: String? = null): this(
    mmBox.x1,
    mmBox.x2,
    mmBox.y1,
    mmBox.y2,
    mmBox.startTimeMillis,
    mmBox.endTimeMillis,
    subFileName
  )
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(startTimeMillis >= endTimeMillis)
  }

}



interface EntityExample: Timed, AnalyticProvenance {
  val id: Long
  val conceptName: String
  val fileUUID: String
  // Value is 0 for non-temporal data like images.
  // the time is relative to the start of the media NOT the real-world time.
  override val startTimeMillis: Int
  // Value is 0 for non-temporal data like images.
  // the time is relative to the start of the media NOT the real-world time.
  override val endTimeMillis: Int
  val subFileName: String?
  val confidence: Double
}

/**
 * Message used to associate an ontology concept with a file in the file store.
 *
 * The [conceptName] is the ontology concept,
 * [fileUUID] the file associated with the concept
 */
data class CreateEntityAttributeExample(
  val conceptName: String,
  val confidence: Double,
  override val analytic: String,
  val subFileName: String? = null,
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
  val entityId: Long,
  override val id: Long,
  override val conceptName: String,
  override val fileUUID: String,
  override val confidence: Double,
  override val analytic: String,
  override val subFileName: String? = null,
  override val startTimeMillis: Int = 0,// 0 for non-temporal data like images
  override val endTimeMillis: Int = 0,// 0 for non-temporal data like images
): EntityExample {
  init {
    check(startTimeMillis >= 0)
    check(endTimeMillis >= 0)
    check(endTimeMillis >= startTimeMillis)
  }
}

/**
 * Message used to associate an ontology concept with a file in the file store.
 *
 * The [conceptName] is the ontology concept,
 * [fileUUID] the file associated with the concept
 */
data class CreateEntityRelationExample(
  val headEntityID: Long,
  val tailEntityID: Long,
  val conceptName: String,
  val confidence: Double,
  override val analytic: String,
  val subFileName: String? = null,
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
  val headEntityID: Long,
  val tailEntityID: Long,
  override val id: Long,
  override val conceptName: String,
  override val fileUUID: String,
  override val confidence: Double,
  override val analytic: String,
  override val subFileName: String? = null,
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
  // Entity ID not supported for this query since entity IDs are local to a file.
)

data class MMDocumentAttributeExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("subFileName") var subFileName: String? = null,
  @field:QueryParam("entityID") var entityID: Long? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
)

data class MMRelationExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("fileUUID") var fileUUID: String? = null,
  @field:QueryParam("subFileName") var subFileName: String? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
  // Entity ID not supported for this query since entity IDs are local to a file.
)

data class MMDocumentRelationExampleQuery @JvmOverloads constructor(
  @field:QueryParam("conceptName") var conceptName: String? = null,
  @field:QueryParam("subFileName") var subFileName: String? = null,
  @field:QueryParam("headEntityID") var headEntityID: Long? = null,
  @field:QueryParam("tailEntityID") var tailEntityID: Long? = null,
  // if entityID is given, the headEntityID and tailEntityID params are ignored.
  @field:QueryParam("entityID") var entityID: Long? = null,
  @field:QueryParam("minConfidence") var minConfidence: Double? = null,
  @field:QueryParam("analytic") var analytic: String? = null,
)

