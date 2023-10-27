package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.tables.Table;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A block in a multi-modal document.
 * For multi-media documents like html documents, there will be many blocks (for paragraphs, images, videos, etc.)
 * For single media documents (e.g. text only, audio file, etc.) there may still be many blocks if processors decide to segment them.
 *
 * @author stuart
 */
public class MultiModalBlock {
    private final int id;
    @Nonnull
    private final MultiModalBlockType type;
    /**
     * The payload should only be non-null if annotations and other objects in the block
     * are not expressed in terms of the parent.
     * For example, if an html page has an image and the block is for that image.
     * In this case,
     * - the boundingBox field will refer to the character range embedding the image (if possible. null otherwise.),
     * - the payload will be a reference to the image (i.e. URL) or a BLOB for the image (base64 string), and
     * - any annotations will be expressed in terms of coordinates within the image (not the page it came from).
     */
    @Nullable
    private final MultiModalPayload payload;
    /**
     * Region in the parent document/block this block is derived from.
     * Can be null when it is not possible, or not practical, to indicate where the block came from.
     * For example, if the character offsets in an html file can not be easily determined
     * (html parsers can delete and insert characters to clean up the document meaning the offsets would not map to the original).
     */
    @Nonnull
    private final BoundingBox boundingBox;
    @Nullable
    private final RichText text;
    @Nonnull
    private final List<MultiModalAnnotation> annotations;
    @Nullable
    private final Integer parentId;
    /**
     * Ids for any blocks derived from this one.
     */
    @Nonnull
    private final List<Integer> children;
    // 2-letter ISO-639-1 language code (e.g. en for English, zh for Chinese) or 'unk' for unknown
    @Nullable
    private final String language;
    @Nonnull
    private final Map<String, String> metadata;
    @Nonnull
    private final List<MultiModalRelation> relations;
    /**
     * Tables automatically extracted from the block.
     * Map is from table-id -> table.
     * The table-id has no requirements, but should prefer that lower ids appear earlier in the document.
     * (e.g. table 3 appears earlier than table 17 in the document).
     */
    @Nonnull
    private final Map<Integer, Table> tables;

    @JsonCreator
    public MultiModalBlock(// NOSONAR - need to have > 7 arguments here
      @JsonProperty(value = "id", required = true) int id,
      @JsonProperty(value = "type", required = true) @Nonnull MultiModalBlockType type,
      @JsonProperty(value = "language") @Nullable String language,
      @JsonProperty(value = "payload") @Nullable MultiModalPayload payload,
      @JsonProperty(value = "boundingBox", required = true) @Nonnull BoundingBox boundingBox,
      @JsonProperty(value = "text") @Nullable RichText text,
      @JsonProperty(value = "annotations") @Nullable List<MultiModalAnnotation> annotations,
      @JsonProperty(value = "parentId") @Nullable Integer parentId,
      @JsonProperty(value = "children") @Nullable List<Integer> children,
      @JsonProperty(value = "metadata") @Nullable Map<String, String> metadata,
      @JsonProperty(value = "relations") @Nullable List<MultiModalRelation> relations,
      @JsonProperty(value = "tables") @Nonnull Map<Integer, Table> tables
    ) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        Preconditions.checkNotNull(boundingBox);
        this.boundingBox = boundingBox;
        this.text = text;
        this.annotations = annotations == null ? new ArrayList<>() : annotations;
        this.parentId = parentId;
        this.children = children == null ? new ArrayList<>() : children;
        this.metadata = metadata == null ? new TreeMap<>() : metadata;
        this.tables = tables;
        Preconditions.checkNotNull(type);
        this.language = language;
        this.relations = relations == null ? new ArrayList<>() : relations;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public MultiModalBlockType getType() {
        return type;
    }

    @Nullable
    public MultiModalPayload getPayload() {
        return payload;
    }

    @Nonnull
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Nullable
    public RichText getText() {
        return text;
    }

    @NotNull
    public List<MultiModalAnnotation> getAnnotations() {
        return annotations;
    }

    @Nullable
    public Integer getParentId() {
        return parentId;
    }

    @NotNull
    public List<Integer> getChildren() {
        return children;
    }

    @Nonnull
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    @Nonnull
    public List<MultiModalRelation> getRelations() {
        return relations;
    }

    @Nonnull
    public Map<Integer, Table> getTables() {
        return tables;
    }
}
