package com.languagecomputer.services.multimodal;

data class MultiModalEntityMessage (
        val entityType: Map<String, Double>,
          //should be SimpleEntityRecord, but am concerned about consistency, object bloat,
            // compatability with third party things that might need translation, etc...
        val entityCorefId: Map<String, Double>, //id and confidence, 0 means not correffed
        val modality: String, //MediaType?
        val boundingBox: BoundingBox,
        val analyticId: String,
        val embeddings: Map<String, DoubleArray> //change to Stuarts python array
)