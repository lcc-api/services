package com.languagecomputer.services.multimodal;

/**
 * @author stuart
 */
public enum MultiModalPayloadType {
  BASE64BLOB,// payload is binary data encoded as a base64 string
  TEXT,// payload is text
  REFERENCE,// payload is a reference (e.g. URL or file path) to a resource (e.g. a link to an embedded video)
}
