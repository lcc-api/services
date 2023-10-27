package com.languagecomputer.services.multimodal.tables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A value from a table cell.
 * The value may contain links, and if so, may contain multiple links.
 * The text field should be the cleaned up text (without any html or other markup).
 * @author stuart
 */
public class TableCellValue {
  @Nonnull
  private final String text;
  @Nonnull
  private final List<LinkTuple> links;

  @JsonCreator
  public TableCellValue(
      @JsonProperty(value = "text", required = true) @Nonnull String text,
      @JsonProperty(value = "links") @Nullable List<LinkTuple> links
  ) {
    this.text = text;
    if (links == null) {
      this.links = new ArrayList<>();
    } else {
      this.links = links;
    }
  }

  @Nonnull
  public String getText() {
    return text;
  }

  @Nonnull
  public List<LinkTuple> getLinks() {
    return links;
  }
}
