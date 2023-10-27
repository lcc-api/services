package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Relations discovered between entities in the document outside of tables.
 * For table relations, see {@link com.languagecomputer.messages.multimodal.tables.TableRelation}.
 * @author stuart
 */
public class MultiModalRelation {
  @Nonnull
  private final String e1;
  @Nonnull
  private final String e2;
  @Nonnull
  private final String relation;
  private final double confidence;
  /**
   * Optional field indicating which extractor or system created the relation.
   */
  @Nonnull
  private final String source;

  @JsonCreator
  public MultiModalRelation(
      @JsonProperty(value = "e1", required = true) @Nonnull String e1,
      @JsonProperty(value = "e2", required = true) @Nonnull String e2,
      @JsonProperty(value = "relation", required = true) @Nonnull String relation,
      @JsonProperty(value = "confidence", defaultValue = "0.5") double confidence,
      @JsonProperty(value = "source") @Nullable String source
  ) {
    this.e1 = e1;
    this.e2 = e2;
    this.relation = relation;
    this.confidence = confidence;
    if (source == null) {
      this.source = "UNK";
    } else {
      this.source = source;
    }
    Preconditions.checkNotNull(e1);
    Preconditions.checkNotNull(relation);
    Preconditions.checkNotNull(e2);
  }

  @NotNull
  public String getE1() {
    return e1;
  }

  @NotNull
  public String getE2() {
    return e2;
  }

  @NotNull
  public String getRelation() {
    return relation;
  }

  public double getConfidence() {
    return confidence;
  }

  @Nonnull
  public String getSource() {
    return source;
  }
}
