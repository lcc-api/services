package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Serialization format for a text document.
 *
 * @author stuart
 */
public class RichText {
  /** the raw text of the document */
  @Nonnull
  private final String body;
  /**
   * 2-letter ISO-639-1 language code (e.g. en for English, zh for Chinese) or 'unk'/null for unknown
   * It is assumed that each RichText object contains only one language.
   */
  @Nullable
  private final String language;
  /**
   * A unique identifier for the document.
   * This may correspond to an LCC-XML document in the DocumentStore.
   */
  @Nullable
  private final String docId;
  /**
   * map from SpanId => Span.
   * Spans are used for basically everything (tokens, sentences, entities, phrases, etc.).
   */
  @Nonnull
  protected final Map<Integer, RichTextSpan> spans;
  protected int nextSpanId = 0; // NOSONAR fine to have the `= 0` I think.

  public RichText(
    String body,
    @Nullable String language,
    String docId
  ) {
    this(body, language, new TreeMap<>(), docId);
  }

  @JsonCreator
  public RichText(
    @JsonProperty(value = "body", required = true) @Nonnull String body,
    @JsonProperty(value = "language") @Nullable String language,
    @JsonProperty(value = "spans", required = true) @Nonnull Map<Integer, RichTextSpan> spans,
    @JsonProperty(value = "docId") @Nullable String docId
  ) {
    this.body = body;
    this.language = language;
    this.docId = docId;
    // adding codes here for sanity checking
    this.spans = new TreeMap<>();
    for (Map.Entry<Integer, RichTextSpan> entry : spans.entrySet()) {
      if (entry.getValue().validSpan) {
        this.spans.put(entry.getKey(), entry.getValue());
      }
    }
    Preconditions.checkNotNull(spans);
    Preconditions.checkNotNull(body);
    nextSpanId = spans.values().stream()
      .map(RichTextSpan::getSpanId)
      .reduce(0, Math::max) + 1;
  }

  @NotNull
  public String getBody() {
    return body;
  }

  @Nullable
  public String getLanguage() {
    return language;
  }

  @Nullable
  public String getDocId() {
    return docId;
  }

  @NotNull
  public Map<Integer, RichTextSpan> getSpans() {
    return spans;
  }
}
