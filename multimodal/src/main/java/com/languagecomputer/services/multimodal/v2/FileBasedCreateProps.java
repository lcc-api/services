package com.languagecomputer.services.multimodal.v2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Some properties that should be common to file-backed (e.g. video/image) docs.
 * MultiMedia docs do not fall into this category.
 */
public interface FileBasedCreateProps {
    @Nonnull
    String getTitle();
    @Nullable
    Map<String, String> getMetaData();
}
