package com.languagecomputer.services.mmannstore

import com.languagecomputer.services.multimodal.BoundingBox
import javax.ws.rs.QueryParam

/**
 * Messages to add attributes to the annotation store.
 */

interface AnalyticProvenance {
  val analytic: String

  companion object {
    // value used when a human is manually adding an example.
    // confidence (if applicable) should presumably be 1.0 in this case.
    const val HUMAN = "human"
  }
}

data class AttributeStoreMessage(
  val entityId: String, // the key of the entity
  val documentId: String, // the key of the document
  val attributeType: String, // key of attribute type in the ontology
  override val analytic: String, // key for the analytic
  val attributeValue: Map<String, Float>,
  val boundingBox: BoundingBox,
  val provenance: String
): AnalyticProvenance

data class AttributeStoreMessageQuery @JvmOverloads constructor(
    val entityId: String? = null, // the key of the entity
    val documentId: String? = null, // the key of the document
    val attributeType: String? = null, // key of attribute type in the ontology
    val analytic: String? = null, // key for the analytic
    val attributeValueMin: Map<String, Float>? = null,
    val attributeValueMax: Map<String, Float>? = null,
    val attributeValueName: String? = null,
    val boundingBox: BoundingBox? = null,
    val provenance: String? = null,
)

data class AttributeStoreMessageList(
  val attributeStoreMessages: List<AttributeStoreMessage>
)
