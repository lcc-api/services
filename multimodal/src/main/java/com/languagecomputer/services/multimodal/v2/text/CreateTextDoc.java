package com.languagecomputer.services.multimodal.v2.text;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Used to model a chunk of text in a multi-media document.
 * Most likely a heading + section text in an HTML document.
 * </br>
 * Example Curl
 * </br>
 * curl -X POST \
 *      -H "Authorization: Bearer $ATESSA_TOKEN" \
 *      -H 'Content-Type: application/json' \
 *      -d '{"title":"example text doc","header":"A Header!","metaData":{},"body":"this is the body text."}' \
 *      http://lcc21:2185/api/multimodalDS/text
 * </br>
 * // returned ID 4863523623487019838 for the text doc.
 */
public record CreateTextDoc(
        @Nonnull String title,// title (if present) or an empty string otherwise.
        @Nonnull Map<String, String> metaData,
        @Nonnull String header,// use empty string if not present.
        @Nonnull String body// use empty string if not present.
        ) {
    public CreateTextDoc {
        Preconditions.checkNotNull(header);
        Preconditions.checkNotNull(body);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(metaData);
        Preconditions.checkArgument(!body.isBlank() || !header.isBlank());
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public Map<String, String> getMetaData() {
        return metaData;
    }
}
