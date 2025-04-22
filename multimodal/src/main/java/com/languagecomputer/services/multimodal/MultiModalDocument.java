package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.tables.Table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The {@link MultiModalDocument#data} is the primary data associated with the document.
 * For image-based documents, this is the picture of the document.
 * For html or text documents, it is the plain-text.
 *
 * @author stuart
 */
public class MultiModalDocument implements CommonDocProperties {
  @Nonnull
  private final String id;
  @Nonnull
  private final MultiModalPayload data;
  @Nonnull
  private final Map<Integer, MultiModalBlock> blocks;
  @Nonnull
  private final Map<String, String> metadata;

  @Nonnull
  private final Map<Integer, Table> tables;

  private int nextBlockId = 0;

  @Nonnull
  @Override
  public String getTitle() {
    String title = metadata.get("TITLE");
    if (title != null) {
      return title;
    }
    return id;
  }

  public static MultiModalDocument fromPlainText(String id, String plainText) {
    return fromPlainText(id, plainText, new HashMap<>());
  }

  public static MultiModalDocument fromPlainText(String id, String plainText, Map<String, String> metadata) {
    return new MultiModalDocument(
        id,
        MultiModalPayload.fromPlainText(plainText),
        Map.of(0, new MultiModalBlock(0, MultiModalBlockType.TEXT_REGION, null, null, new BoundingBox(0, 0, plainText.length(), 0), null, null, null, null, null, null, new HashMap<>())),
        metadata,
        null,
        1
    );
  }

  public static MultiModalDocument fromPayload(String id, MultiModalPayload payload) {
    return fromPayload(id, payload, new HashMap<>());
  }
  public static MultiModalDocument fromPayload(String id, MultiModalPayload payload, Map<String, String> metadata) {
    return new MultiModalDocument(
        id,
        payload,
        new HashMap<>(),
        metadata,
        null,
        0
    );
  }

  @JsonCreator
  public MultiModalDocument(
      @JsonProperty(value = "id", required = true) @Nonnull String id,
      @JsonProperty(value = "data", required = true) @Nonnull MultiModalPayload data,
      @JsonProperty(value = "blocks", required = true) @Nonnull Map<Integer, MultiModalBlock> blocks,
      @JsonProperty(value = "metadata") @Nullable Map<String, String> metadata,
      @JsonProperty(value = "tables", defaultValue = "{}") @Nullable Map<Integer, Table> tables,
      @JsonProperty(value = "next_id", defaultValue = "0") int nextBlockId
  ) {
    this.id = id;
    this.data = data;
    this.blocks = blocks;
    this.tables = tables == null ? new HashMap<>() : tables;
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(blocks);
    Preconditions.checkNotNull(this.tables);
    this.metadata = metadata == null ? new TreeMap<>() : metadata;
    this.nextBlockId = nextBlockId;
  }

  @Nonnull
  public String getId() {
    return id;
  }

  @Nonnull
  public Map<Integer, MultiModalBlock> getBlocks() {
    return blocks;
  }

  @Nonnull
  public MultiModalPayload getData() {
    return data;
  }

  @JsonIgnore
  public int getAndIncrementNextBlockId() {
    return nextBlockId++;
  }

  @Nonnull
  public Map<String, String> getMetadata() {
    return metadata;
  }

  @Nonnull
  public Map<Integer, Table> getTables() {
    return tables;
  }

  @JsonProperty("next_id")
  public int getNextBlockId() {
    return nextBlockId;
  }

  @Nonnull
  @Override
  @JsonIgnore
  public Map<String, String> getMetaData() {
    return getMetadata();
  }
}
