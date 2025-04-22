package com.languagecomputer.services.multimodal.v2;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.CommonDocProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

// OtherDoc is for LEGACY_V1_VIDEO, MULTI_MEDIA, and TRANSCRIPT currently.
public record OtherDoc(
        @Nonnull String id,
        @Nonnull String title,
        @Nonnull Map<String, String> metaData,
        @Nullable String mediaURL
) implements CommonDocProperties {
    public OtherDoc {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(metaData);
    }

    @Nonnull
    @Override
    public Map<String, String> getMetaData() {
        return metaData;
    }

    @Nonnull
    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return title;
    }
}
