package com.languagecomputer.services.multimodal.tables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

/**
 * A relation automatically extracted from a table.
 * None of the fields should contain markup (e.g. html).
 * The row/column indexes are for the TableCell indexes (not the column indexes) as each TableCell can span multiple columns, so having them be indexes into the list is more useful.
 *
 * @author stuart
 */
public class TableRelation {
    @Nonnull
    private final String arg1;
    @Nonnull
    private final String relationType;
    @Nonnull
    private final String arg2;
    private final int arg1Row;
    private final int arg1Column;
    private final int arg2Row;
    private final int arg2Column;

    @JsonCreator
    public TableRelation(
        @JsonProperty(value = "arg1", required = true) @Nonnull String arg1,
        @JsonProperty(value = "relation_type", required = true) @Nonnull String relationType,
        @JsonProperty(value = "arg2", required = true) @Nonnull String arg2,
        @JsonProperty(value = "arg1_row", required = true) int arg1Row,
        @JsonProperty(value = "arg1_column", required = true) int arg1Column,
        @JsonProperty(value = "arg2_row", required = true) int arg2Row,
        @JsonProperty(value = "arg2_column", required = true) int arg2Column
    ) {
        this.arg1 = arg1;
        this.relationType = relationType;
        this.arg2 = arg2;
        this.arg1Row = arg1Row;
        this.arg1Column = arg1Column;
        this.arg2Row = arg2Row;
        this.arg2Column = arg2Column;
    }

    @Nonnull
    @JsonProperty("arg1")
    public String getArg1() {
        return arg1;
    }

    @Nonnull
    @JsonProperty("relation_type")
    public String getRelationType() {
        return relationType;
    }

    @Nonnull
    @JsonProperty("arg2")
    public String getArg2() {
        return arg2;
    }

    @JsonProperty("arg1_row")
    public int getArg1Row() {
        return arg1Row;
    }

    @JsonProperty("arg1_column")
    public int getArg1Column() {
        return arg1Column;
    }

    @JsonProperty("arg2_row")
    public int getArg2Row() {
        return arg2Row;
    }

    @JsonProperty("arg2_column")
    public int getArg2Column() {
        return arg2Column;
    }
}
