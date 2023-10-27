package com.languagecomputer.services.multimodal.relationships;

import com.languagecomputer.services.multimodal.BoundingBox
import com.languagecomputer.services.multimodal.MultiModalRelationshipMessage


data class MultiModalEntityAttributeRelationshipMessage (
        override val relationshipType: Map<String, Double>,
        override val boundingBox: BoundingBox, //should this just be inherited from the actual argument?
        override val analyticId: String,
        override val embeddings: Map<String, DoubleArray>, //change to Stuarts python array // val e1 : MultiModalEntityMessage,
        override val r1 : String,
        override val r2 : String,
        override val isR1ToR2: Boolean
) : MultiModalRelationshipMessage<String, String>()