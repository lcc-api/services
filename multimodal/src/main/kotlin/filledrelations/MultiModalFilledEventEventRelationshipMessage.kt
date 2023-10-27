package com.languagecomputer.services.multimodal.filledrelations;

import com.languagecomputer.services.multimodal.BoundingBox
import com.languagecomputer.services.multimodal.MultiModalEventMessage
import com.languagecomputer.services.multimodal.MultiModalRelationshipMessage


data class MultiModalFilledEventEventRelationshipMessage (
        override val relationshipType: Map<String, Double>,
        override val boundingBox: BoundingBox, //should this just be inherited from the actual argument?
        override val analyticId: String,
        override val embeddings: Map<String, DoubleArray>, //change to Stuarts python array // val e1 : MultiModalEntityMessage,
        override val r1 : MultiModalEventMessage,
        override val r2 : MultiModalEventMessage,
        override val isR1ToR2: Boolean
) : MultiModalRelationshipMessage<MultiModalEventMessage, MultiModalEventMessage>()