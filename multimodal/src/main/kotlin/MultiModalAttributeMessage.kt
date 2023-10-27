package com.languagecomputer.services.multimodal;

data class MultiModalAttributeMessage (
        val values: Map<String, Double>,
        val boundingBox: BoundingBox,
        val attributeType: String,
        val analyticId: String,
)
