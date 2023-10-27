package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Base64;

/**
 * Object for storing (or referencing) the payloads of a multi-modal document.
 *
 * @author stuart
 */
public class MultiModalPayload {
  /** The text, base64 string, or reference to the data. */
  @Nonnull
  private final String data;
  @Nonnull
  private final MultiModalPayloadType type;
  /**
   * prefer using the mime-type if available.
   * for base64 encoded binary data this is the underlying type of data. e.g. image/png for an image.
   * for text this is optional, but should likely be either text/plain or text/html.
   * */
  @Nullable
  private final String blobType;
  /** the original file name for the payload (if applicable). */
  @Nullable
  private final String fileName;

  @JsonCreator
  public MultiModalPayload(
    @JsonProperty(value = "data", required = true) @Nonnull String data,
    @JsonProperty(value = "type", required = true) @Nonnull MultiModalPayloadType type,
    @JsonProperty("blobType") @Nullable String blobType,
    @JsonProperty("fileName") @Nullable String fileName
  ) {
    this.data = data;
    this.type = type;
    this.blobType = blobType;
    this.fileName = fileName;
    Preconditions.checkNotNull(data);
    Preconditions.checkNotNull(type);
  }

  public static MultiModalPayload fromPlainText(String text) {
    return new MultiModalPayload(text, MultiModalPayloadType.TEXT, "text/plain", null);
  }

  public static MultiModalPayload fromReferent(String reference) {
    return new MultiModalPayload(reference, MultiModalPayloadType.REFERENCE, null, null);
  }

  public static MultiModalPayload fromBlob(byte[] blob, @Nullable String blobType, @Nullable String fileName) {
    return new MultiModalPayload(Base64.getEncoder().encodeToString(blob), MultiModalPayloadType.BASE64BLOB, blobType, fileName);
  }

  public byte[] toBytes() {
    Preconditions.checkArgument(type == MultiModalPayloadType.BASE64BLOB, "Payload type must be %s to decode, but actual type is %s", MultiModalPayloadType.BASE64BLOB, type);
    return Base64.getDecoder().decode(data);
  }

  @NotNull
  public String getData() {
    return data;
  }

  @NotNull
  public MultiModalPayloadType getType() {
    return type;
  }

  @Nullable
  public String getBlobType() {
    return blobType;
  }

  @Nullable
  public String getFileName() {
    return fileName;
  }
}
