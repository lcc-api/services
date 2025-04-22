package com.languagecomputer.services.multimodal.v2;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.MultiModalDocument;
import com.languagecomputer.services.multimodal.Transcript;
import com.languagecomputer.services.multimodal.v2.image.ImageDoc;
import com.languagecomputer.services.multimodal.v2.multimedia.MultiMediaDoc;
import com.languagecomputer.services.multimodal.v2.text.TextDoc;
import com.languagecomputer.services.multimodal.v2.video.VideoDoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Container class to make just getting a document easier.
 * It may seem inefficient to have a different field for each doc type,
 * but it does solve the problem of unambiguously defining which doc types need which fields.
 * Else, we'd end up with A LOT of fields with confusing/ambiguous nullability, structure, etc.
 * (see {@link MultiModalDocument})
 * </br>
 * DO NOT ADD ANNOTATION FIELDS TO THIS OBJECT
 * It is JUST for shipping the underlying data and some modality-specific metadata.
 * Annotations should go in one of the annotation stores.
 */
public record MMDoc(
        @Nonnull String id,// probably fine to duplicate the id for convenience, but metadata might be a bit much.
        @Nonnull DocumentType type,
        // only one of the below fields should be non-null depending on the document type.
        @Nullable MultiModalDocument legacy,
        @Nullable MultiMediaDoc multiMedia,
        @Nullable Transcript transcript,
        @Nullable ImageDoc image,
        @Nullable VideoDoc video,
        @Nullable TextDoc text
) {
    public MMDoc {
        Preconditions.checkArgument(legacy == null || type == DocumentType.LEGACY_V1_VIDEO);
        Preconditions.checkArgument(multiMedia == null || type == DocumentType.MULTI_MEDIA);
        Preconditions.checkArgument(transcript == null || type == DocumentType.TRANSCRIPT);
        Preconditions.checkArgument(image == null || type == DocumentType.IMAGE);
        Preconditions.checkArgument(video == null || type == DocumentType.VIDEO);
        Preconditions.checkArgument(text == null || type == DocumentType.TEXT);
    }

    public static MMDoc fromTranscript(String id, Transcript transcript) {
        return new MMDoc(
                id,
                DocumentType.TRANSCRIPT,
                null,
                null,
                transcript,
                null,
                null,
                null
        );
    }
    public static MMDoc fromLegacyVid(String id, MultiModalDocument doc) {
        return new MMDoc(id, DocumentType.LEGACY_V1_VIDEO, doc,
                null, null, null, null, null);
    }
}
