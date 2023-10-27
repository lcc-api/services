package com.languagecomputer.services.multimodal.tables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

/**
 * Object containing link information.
 * e.g. from an <a> tag in html.
 * @author stuart
 */
public class LinkTuple {
    private final String url;
    private final String altText;

    @JsonCreator
    public LinkTuple(
        @JsonProperty(value = "url", required = true) @Nonnull String url,
        @JsonProperty(value = "alt_text", required = true) @Nonnull String altText
    ) {
        this.url = url;
        this.altText = altText;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("alt_text")
    public String getAltText() {
        return altText;
    }
}
