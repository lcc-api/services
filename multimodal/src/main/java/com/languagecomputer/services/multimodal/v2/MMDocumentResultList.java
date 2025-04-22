package com.languagecomputer.services.multimodal.v2;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * More bespoke and leaner version of {@link lcc.services.documentstore.DocumentResultList}
 * @author stuart
 */
public record MMDocumentResultList(
  List<DocumentResult> results
) implements Iterable<MMDocumentResultList.DocumentResult> {
  /**
   * Inner object returned by a DocumentResultList.
   * DO NOT ADD METADATA to this result as that would greatly bloat this object.
   * Metadata is pretty unrestricted, so adding meta data could cause a lot of speed/size issues.
   */
  public record DocumentResult(
    @Nonnull String documentID,
    @Nonnull String title,
    @Nonnull DocumentType type,
    // creationTime is in seconds since the unix epoch
    // using double instead of long since javascript uses doubles for all numbers.
    double creationTime,
    @Nullable String mediaURL,
    @Nonnull List<String> parents
  ) {
    public DocumentResult {
      Preconditions.checkNotNull(documentID);
      Preconditions.checkNotNull(title);
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(parents);
    }
  }

  @NotNull
  @Override
  public Iterator<DocumentResult> iterator(){
    return this.results.iterator();
  }
}
