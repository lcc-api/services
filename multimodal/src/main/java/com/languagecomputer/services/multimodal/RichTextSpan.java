package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Object to store tokens, sentences, entities, and other annotations.
 *
 * @author stuart
 */
public class RichTextSpan {
  /**
   * A string code for the type of span.
   * Some default spanType strings can be found in {@link DefaultSpanTypes}.
   * If you need to get more specific about the type add an entry to the attributes map (key = "subType", value = "moreSpecificType")
   */
  private final String spanType;
  private final int startChar;
  private final int endChar;
  /**
   * The start/endToken fields are nullable since many modern methods do not use tokens.
   * For example, most LLMs use their own alphabet of word-pieces (multi-character sequences typically shorter than a full word/token).
   * This makes using character offsets the mandatory information as they are universal (independent of any specific tokenization system).
   */
  @Nullable
  private final Integer startToken;
  @Nullable
  private final Integer endToken;
  private final int spanId;
  @Nullable
  private final Integer parentId;// ID of the parent span when one exists (e.g. argument of a frame annotation).
  private final Map<String, String> attributes;

  public final boolean validSpan;

  @Nullable
  private final List<Integer> mentionChain;

  public static RichTextSpan addSpan(// NOSONAR - method needs all 8 parameters
    RichText document,
    String spanType,
    int startChar,
    int endChar,
    @Nullable Integer parentId,
    Map<String, String> attributes,
    @Nullable Integer startToken,
    @Nullable Integer endToken
  ) {
    RichTextSpan richTextSpan = new RichTextSpan(
      spanType,
      startChar,
      endChar,
      startToken,
      endToken,
      document.nextSpanId++,
      parentId,
      attributes,
            null
    );
    Preconditions.checkArgument(!document.spans.containsKey(richTextSpan.spanId));
    document.spans.put(richTextSpan.spanId, richTextSpan);
    return richTextSpan;
  }
  public static RichTextSpan addSpan(
    RichText document,
    String spanType,
    int startChar,
    int endChar,
    @Nullable Integer parentId,
    @Nullable Integer startToken,
    @Nullable Integer endToken
  ) {
    try {
      RichTextSpan richTextSpan = new RichTextSpan(
        spanType,
        startChar,
        endChar,
        startToken,
        endToken,
        document.nextSpanId++,
        parentId,
        new HashMap<>(),
              null
      );
      Preconditions.checkArgument(!document.spans.containsKey(richTextSpan.spanId));
      document.spans.put(richTextSpan.spanId, richTextSpan);
      return richTextSpan;
    } catch (IllegalArgumentException e) {
      System.err.println("error for span |" + document.getBody().substring(startChar) + "|");// NOSONAR - we don't have a logging framework here.
      throw e;
    }
  }

  @JsonCreator
  RichTextSpan(
    @JsonProperty(value = "spanType", required = true) @Nonnull String spanType,
    @JsonProperty(value = "startChar", required = true) int startChar,
    @JsonProperty(value = "endChar", required = true) int endChar,
    @JsonProperty("start_token") @Nullable Integer startToken,
    @JsonProperty("end_token") @Nullable Integer endToken,
    @JsonProperty(value = "spanId", required = true) int spanId,
    @JsonProperty("parentId") @Nullable Integer parentId,
    @JsonProperty("attributes") @Nullable Map<String, String> attributes,
    @JsonProperty("mention_chain") @Nullable List<Integer> mentionChain
  ) {
    this.spanType = spanType;
    this.startChar = startChar;
    this.endChar = endChar;
    this.startToken = startToken;
    this.endToken = endToken;
    this.spanId = spanId;
    this.parentId = parentId;
    this.attributes = attributes == null ? new TreeMap<>() : attributes;
    this.validSpan = startChar < endChar && (parentId == null || spanId != parentId);
    Preconditions.checkNotNull(spanType);
    Preconditions.checkArgument(parentId == null || spanId != parentId, "Parent ID can not be the same as span ID.");
    this.mentionChain = mentionChain;
  }

  public String getSpanType() {
    return spanType;
  }

  public int getStartChar() {
    return startChar;
  }

  public int getEndChar() {
    return endChar;
  }

  @Nullable
  public Integer getStartToken() {
    return startToken;
  }

  @Nullable
  public Integer getEndToken() {
    return endToken;
  }

  public int getSpanId() {
    return spanId;
  }

  @Nullable
  public Integer getParentId() {
    return parentId;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  @Nullable
  public List<Integer> getMentionChain() {
    return mentionChain;
  }
}
