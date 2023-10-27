package com.languagecomputer.services.multimodal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author stuart
 */
public enum MultiModalBlockType {
 @JsonProperty("SeparatorRegion")
  SEPARATOR_REGION,
  @JsonProperty("TextRegion")
  TEXT_REGION,
  @JsonProperty("TableRegion")
  TABLE_REGION,
  @JsonProperty("OtherRegion")
  OTHER_REGION,
  @JsonProperty("ImageRegion")
  IMAGE_REGION,
  @JsonProperty("MathsRegion")
  MATHS_REGION,
  @JsonProperty("CaptionRegion")
  CAPTION_REGION,
  @JsonProperty("AuthorRegion")
  AUTHOR_REGION,
  @JsonProperty("DateRegion")
  DATE_REGION,
  @JsonProperty("DeckRegion")
  DECK_REGION,
  @JsonProperty("PlaceRegion")
  PLACE_REGION,
  @JsonProperty("HeadlineRegion")
  HEADLINE_REGION,
  @JsonProperty("RelatedRegion")
  RELATED_REGION,
  @JsonProperty("SubtitleRegion")
  SUBTITLE_REGION,
  @JsonProperty("SummaryRegion")
  SUMMARY_REGION
}
