package com.languagecomputer.services.multimodal.v2;

public enum DocumentType {
    LEGACY_V1_VIDEO,
    // I think video should be the only thing with any info of consequence still in the mmdoc.
    // for images, all the important stuff is either in metadata, or one of the annotation stores.
    //    LEGACY_V1_IMAGE,
    MULTI_MEDIA,
    IMAGE,
    VIDEO,
    TRANSCRIPT,
    TEXT,
}
