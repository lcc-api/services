package com.languagecomputer.services.multimodal.v2;

/**
 * To ensure embeddable media for docs (e.g. image/video) have a standard way to
 * indicate where their payload is.
 */
public interface EmbeddableMedia {
    String getMediaURL();
    String getCaption();
}
