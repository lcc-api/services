package com.languagecomputer.services.multimodal.v2.multimedia;

import com.google.common.base.Preconditions;
import com.languagecomputer.services.multimodal.CommonDocProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public record MultiMediaDoc(
  @Nonnull String id,
  @Nonnull String title,
  @Nonnull Map<String, String> metaData,
  @Nonnull List<Block> blocks
) implements CommonDocProperties {
  public MultiMediaDoc {
    Preconditions.checkNotNull(metaData);
    Preconditions.checkNotNull(blocks);
  }

  @Nonnull
  @Override
  public String getId() {
    return id;
  }

  @Nonnull
  @Override
  public String getTitle() {
    return title;
  }

  @Nonnull
  @Override
  public Map<String, String> getMetaData() {
    return metaData;
  }

  public record Block(
          @Nonnull MultiMediaBlockType type,
          // if type is TEXT, the backing document should be found in lucene doc store.
          // if type is IMAGE, the backing document should be found in mmds.
          @Nonnull String docId,
          @Nullable String mediaURL,// if nonnull, points to a URL containing backing media.
          @Nonnull Map<String, String> metaData,
          @Nullable TextData textData,
          @Nullable ImageData imageData
  ) {
    public static Block forImage(String id, String mediaURL, Map<String, String> metaData, ImageData imageData) {
      return new Block(
              MultiMediaBlockType.IMAGE,
              id,
              mediaURL,
              metaData,
              null,
              imageData
      );
    }
    public static Block forText(String id, Map<String, String> metaData, TextData textData) {
      return new Block(
              MultiMediaBlockType.TEXT,
              id,
              null,
              metaData,
              textData,
              null
      );
    }

    public Block {
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(docId);
      int nonNullCounts = 0;
      if (textData != null) {
        nonNullCounts++;
        Preconditions.checkArgument(type == MultiMediaBlockType.TEXT);
      }
      if (imageData != null) {
        nonNullCounts++;
        Preconditions.checkArgument(type == MultiMediaBlockType.IMAGE);
      }
      Preconditions.checkArgument(nonNullCounts == 1);
    }

    // field object types
    public record TextData(
            @Nonnull String sectionHeader,// should be an empty string if no text present.
            @Nonnull String body// should be an empty string if no text present.
    ) {
      public TextData {
        Preconditions.checkNotNull(body);
        Preconditions.checkNotNull(sectionHeader);
      }
    }

    public record ImageData(
            @Nonnull String caption,// should be an empty string if no caption present.
            // width and height are returned so that space on the page can be pre-allocated when rendering.
            // can also help speed up loading by lazy-loading images
            // e.g. <img loading="lazy"
            // will make the initial load much faster/better in documents with lots of images.
            // (though if you don't pre-allocate space you can trigger the lazy-load of way more images than intended).
            int width,
            int height
    ) {
      public ImageData {
        Preconditions.checkNotNull(caption);
      }
    }
  }
}
