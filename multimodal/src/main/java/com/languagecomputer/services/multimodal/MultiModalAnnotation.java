package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

/**
 * Annotation operating on video/audio/images.
 * For text-only blocks, using {@link RichTextSpan} is preferred.
 *
 * @author stuart
 */
public class MultiModalAnnotation {
  @NotNull
  private final String objectType;
  @NotNull
  private final BoundingBox boundingBox;
  @NotNull
  private final Map<String, String> attributes;

  @JsonCreator
  public MultiModalAnnotation(
    @JsonProperty(value = "objectType", required = true) @NotNull String objectType,
    @JsonProperty(value = "boundingBox", required = true) @NotNull BoundingBox boundingBox,
    @JsonProperty("attributes") @Nullable Map<String, String> attributes
  ) {
    this.objectType = objectType;
    this.boundingBox = boundingBox;
    this.attributes = attributes == null ? new TreeMap<>() : attributes;
    Preconditions.checkNotNull(objectType);
    Preconditions.checkNotNull(boundingBox);
  }

  public String getObjectType() {
    return objectType;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }
}
