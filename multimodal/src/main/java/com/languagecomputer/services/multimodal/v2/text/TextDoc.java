package com.languagecomputer.services.multimodal.v2.text;

import com.languagecomputer.services.multimodal.AtomicDocProps;
import com.languagecomputer.services.multimodal.CommonDocProperties;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * text stuff
 * </br>
 * DO NOT ADD ANNOTATION FIELDS TO THIS OBJECT
 * It is JUST for shipping the underlying data and some modality-specific metadata.
 * Annotations should go in one of the annotation stores.
 */
public record TextDoc(
        @Nonnull String id,
        @Nonnull String title,
        @Nonnull
        Map<String, String> metaData,
        @Nonnull
        List<String> parents,// docIds of documents this TextDoc is included in or derived from.
        @Nonnull String header,
        @Nonnull String body
) implements CommonDocProperties, AtomicDocProps {
    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    @Override
    public Map<String, String> getMetaData() {
        return metaData;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return title;
    }

    @Nonnull
    @Override
    public List<String> getParents() {
        return parents;
    }

    public String allText() {
        return String.format("%s\n%s", header, body).trim();
    }

}
