package com.languagecomputer.services.multimodal.v2.image;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.v2.FileBasedCreateProps;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Payload for creating an image doc.
 * Image should be uploaded to the filestore before submitting a request to make the document.
 * </br>
 * Example Curl
 * </br>
 * curl -X POST \
 *      -H "Authorization: Bearer $ATESSA_TOKEN" \
 *      -H 'Content-Type: application/json' \
 *      -d '{"fileId":"14242861661","title":"example image","width":-1,"height":-1,"metaData":{},"caption":"a picture depicting an example"}' \
 *      http://lcc21:2185/api/multimodalDS/image
 */
public record CreateImageDoc(
        @Nonnull String fileId,// fileid of image in the filestore for this document.
        @Nonnull String title,// empty string if no title
        int width,// a dimension of the image. If the file store can not detect the size, this is used as a fallback. Set to -1 if you don't know.
        int height,// a dimension of the image. If the file store can not detect the size, this is used as a fallback. Set to -1 if you don't know.
        @Nonnull Map<String, String> metaData,
        @Nonnull String caption// caption (if present). Empty string otherwise.
        ) implements FileBasedCreateProps {
    public CreateImageDoc {
        Preconditions.checkNotNull(fileId);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(metaData);
        Preconditions.checkNotNull(caption);
    }

    @NotNull
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
