package com.languagecomputer.services.multimodal;

data class MultiModalEventMessage (
        val eventType:Map<String, Double>, //should be a json map that gets stored
        //val eventCorefID: Map<String, Double>, coreferring events could be roleFillers
        val roleFillers: List<MultiModalRelationshipMessage<Any, Any>>,
        val embeddings: Map<String, DoubleArray>, //change to Stuarts python array instead of doubleArray
        val source: BoundingBox, //document id/modality, Bounding box in that document
        val analyticId: String,
)
