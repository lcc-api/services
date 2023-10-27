package com.languagecomputer.services.multimodal;


abstract class MultiModalRelationshipMessage<A, B> {
  abstract val relationshipType: Map<String, Double>
  abstract val boundingBox: BoundingBox //should this just be inherited from the actual argument?
  abstract val analyticId: String
  abstract val embeddings: Map<String, DoubleArray> //change to Stuarts python array
  abstract val r1:A
  abstract val r2:B
  abstract val isR1ToR2: Boolean
}