package com.languagecomputer.services.multimodal.v2.multimedia;

/**
 * blocks for multimedia documents only support a subset of the types defined
 * in {@link com.languagecomputer.services.multimodal.v2.DocumentType}
 * the names here should all exactly match one of the types in the above enum
 */
public enum MultiMediaBlockType {
  TEXT,
  IMAGE,
//  VIDEO, // for later once the image stuff seems good/stable.
//  TRANSCRIPT, - TRANSCRIPT seems like it would be weird/inappropriate here.
}
