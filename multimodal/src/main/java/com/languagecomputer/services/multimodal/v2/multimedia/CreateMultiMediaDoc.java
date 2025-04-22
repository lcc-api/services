package com.languagecomputer.services.multimodal.v2.multimedia;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * A Multi-Media document.
 * Main use-case is to support representing HTML pages, but could also be used for PDFs or other documents with
 * multiple types of media inside them.
 * </br>
 * Basic structure is a sequence of blocks.
 * blockDocIds is a list of already-created text chunks, images, or other media contained in this document.
 * </br>
 * Example Curl
 * </br>
 * curl -X POST \
 *      -H "Authorization: Bearer $ATESSA_TOKEN" \
 *      -H 'Content-Type: application/json' \
 *      -d '{"title":"A multimedia doc to tie things together.","metaData":{},"blockDocIds":["4863523623487019838", "14242861661"]}' \
 *      http://lcc21:2185/api/multimodalDS/multimedia
 * // 4863523623487019838 was the ID of the text doc in the {@link com.languagecomputer.services.multimodal.v2.text.CreateTextDoc} example
 * // 14242861661 was the ID of the image doc in the {@link com.languagecomputer.services.multimodal.v2.image.CreateImageDoc} example
 * // 5035343240673420872 was returned as the ID of the MultiMediaDoc
 */
public record CreateMultiMediaDoc(
  @Nonnull String title,
  @Nonnull Map<String, String> metaData,
  @Nonnull List<String> blockDocIds
) {
  public CreateMultiMediaDoc {
    Preconditions.checkNotNull(metaData);
    Preconditions.checkNotNull(blockDocIds);
    Preconditions.checkNotNull(title);
  }
}
