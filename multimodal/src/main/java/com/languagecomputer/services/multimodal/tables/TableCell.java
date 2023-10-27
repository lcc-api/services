package com.languagecomputer.services.multimodal.tables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Object to represent table cells (e.g. from html tables).
 * Sometimes, a table cell will contain lists.
 * This is why there is a list of values.
 * @author stuart
 */
public class TableCell {
  @Nonnull
  private final String cellType;
  private final boolean headerCell;
  private final boolean empty;
  @Nonnull
  private final List<TableCellValue> values;
  private final boolean bolded;
  private final int numColumns;

  @JsonCreator
  public TableCell(
      @JsonProperty(value = "cell_type", required = true) @NotNull String cellType,
      @JsonProperty(value = "header_cell", required = true) boolean headerCell,
      @JsonProperty(value = "empty", required = true) boolean empty,
      @JsonProperty(value = "values", required = true) @NotNull List<TableCellValue> values,
      @JsonProperty(value = "bolded", required = true) boolean bolded,
      @JsonProperty(value = "num_columns", required = true) int numColumns
  ) {
      this.cellType = cellType;
      this.headerCell = headerCell;
      this.empty = empty;
      this.values = values;
      this.bolded = bolded;
      this.numColumns = numColumns;
  }

    @Nonnull
    @JsonProperty("cell_type")
    public String getCellType() {
        return cellType;
    }

    @JsonProperty("header_cell")
    public boolean isHeaderCell() {
        return headerCell;
    }

    @JsonProperty("empty")
    public boolean isEmpty() {
        return empty;
    }

    @Nonnull
    public List<TableCellValue> getValues() {
        return values;
    }

    @JsonProperty("bolded")
    public boolean isBolded() {
        return bolded;
    }

    @JsonProperty("num_columns")
    public int getNumColumns() {
        return numColumns;
    }
}
