package com.languagecomputer.services.multimodal.v2.video;

import com.languagecomputer.services.multimodal.CommonDocProperties;
import com.languagecomputer.services.multimodal.AtomicDocProps;
import com.languagecomputer.services.multimodal.v2.EmbeddableMedia;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * used as a stand-in for video as the bulk of their relevant info is stored as annotations
 * in one (or both) of the annotation stores.
 * </br>
 * DO NOT ADD ANNOTATION FIELDS TO THIS OBJECT
 * It is JUST for shipping the underlying data and some modality-specific metadata.
 * Annotations should go in one of the annotation stores.
 */
public record VideoDoc(
  @Nonnull String id,
  @Nonnull String title,
  @Nonnull Map<String, String> metaData,
  @Nonnull List<String> parents,// docIds of documents this video is included in or derived from.
  int width,// will be -1 if not detected or provided as part of doc creation.
  int height,// will be -1 if not detected or provided as part of doc creation.
  int durationMillis,// will be -1 if not detected or provided as part of doc creation.
  @Nonnull String mediaURL,
  @Nonnull String caption
) implements CommonDocProperties, AtomicDocProps, EmbeddableMedia {
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

    @Override
    public String getMediaURL() {
        return mediaURL;
    }

    @Override
    public String getCaption() {
        return caption;
    }
}
