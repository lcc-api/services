package com.languagecomputer.services.messages;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to bundle information about a lexeme.
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public class LexicalItem implements Serializable {
  private static final long serialVersionUID = -30055441875770193L;

  public final String word;
  public final String pos;
  public final Boolean stemmed;

  private transient Map<String, Object> paramMapping;

  public LexicalItem(@Nonnull final String word) {
    this(word, "ANY", false);
  }

  public LexicalItem(@Nonnull final String word, @Nonnull final String pos, @Nonnull final Boolean stemmed) {
    this.word = word;
    this.pos = pos;
    this.stemmed = stemmed;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("word", word)
        .add("pos", pos)
        .add("stemmed", stemmed)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LexicalItem that = (LexicalItem) o;

    if (!word.equals(that.word)) return false;
    if (!pos.equals(that.pos)) return false;
    return stemmed.equals(that.stemmed);

  }

  @Override
  public int hashCode() {
    int result = word.hashCode();
    result = 31 * result + pos.hashCode();
    result = 31 * result + stemmed.hashCode();
    return result;
  }

  public Map<String, Object> getParamMap() {
    if (paramMapping == null) {
      paramMapping = new HashMap<>();
      paramMapping.put("word", word);
      paramMapping.put("pos", pos);
      paramMapping.put("stemmed", stemmed);
    }

    return Collections.unmodifiableMap(paramMapping);
  }
}
