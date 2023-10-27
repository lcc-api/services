package com.languagecomputer.services.multimodal.tables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * An object to represent tables (e.g. from html).
 * The shape for the cells list can be jagged (i.e. non-uniform lengths) as TableCells can span multiple columns.
 * The relations field contains relations extracted from rows in the table.
 * @author stuart
 */
public class Table {
    @Nonnull
    private final List<List<TableCell>> cells;
    @Nonnull
    private final List<TableRelation> relations;

    @JsonCreator
    public Table(
        @JsonProperty(value = "cells", required = true) @NotNull List<List<TableCell>> cells,
        @JsonProperty(value = "relations") @Nullable List<TableRelation> relations
    ) {
        this.cells = cells;
        if (relations == null) {
            this.relations = new ArrayList<>();
        } else {
            this.relations = relations;
        }
    }

    @Nonnull
    public List<List<TableCell>> getCells() {
        return cells;
    }

    @Nonnull
    public List<TableRelation> getRelations() {
        return relations;
    }
}
