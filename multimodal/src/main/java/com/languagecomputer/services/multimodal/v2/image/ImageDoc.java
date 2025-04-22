package com.languagecomputer.services.multimodal.v2.image;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.CommonDocProperties;
import com.languagecomputer.services.multimodal.AtomicDocProps;
import com.languagecomputer.services.multimodal.v2.EmbeddableMedia;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * used as a stand-in for image in multi-media doc as the bulk of their relevant info is stored as annotations
 * in one (or both) of the annotation stores.
 * </br>
 * DO NOT ADD ANNOTATION FIELDS TO THIS OBJECT
 * It is JUST for shipping the underlying data and some modality-specific metadata.
 * Annotations should go in one of the annotation stores.
 */
public record ImageDoc(
  @Nonnull String id,
  @Nonnull String title,
  @Nonnull Map<String, String> metaData,
  @Nonnull List<String> parents,// docIds of documents this image is included in or derived from.
  int width,// if this was not given/detected at doc creation time it will be -1
  int height,// if this was not given/detected at doc creation time it will be -1
  @Nonnull String mediaURL,
  @Nonnull String caption// empty string if no caption present.
) implements CommonDocProperties, AtomicDocProps, EmbeddableMedia {
    public ImageDoc {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(metaData);
        Preconditions.checkNotNull(parents);
        Preconditions.checkNotNull(mediaURL);
        Preconditions.checkNotNull(caption);
    }

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
