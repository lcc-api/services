package com.languagecomputer.services.multimodal.v2.video;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.v2.FileBasedCreateProps;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Object containing information for creating a video document.
 * It is expected that the user has uploaded the backing file to the filestore before creating the video document.
 */
public record CreateVideoDoc(
   @Nonnull String fileId,// ID of file in the filestore
   @Nonnull String title,// title (if present). empty string otherwise
   @Nonnull Map<String, String> metaData,
   @Nonnull String caption// empty string if there is no caption.
) implements FileBasedCreateProps {
    public CreateVideoDoc {
        Preconditions.checkNotNull(fileId);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(caption);
    }

    @Nonnull
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    @Nonnull
    public Map<String, String> getMetaData() {
        return metaData;
    }
}
