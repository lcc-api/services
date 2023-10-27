package com.languagecomputer.services.docstore

/**
 * Message object for encoding document metadata,
 * any supplementary info about the key (e.g. object type, single-valued)
 * will be encoded in the ontology
 */
class MetadataMessage(var key: String? = null, var value: String? = null) {
  override fun toString(): String {
    return value.toString()
  }

  override fun hashCode(): Int {
    return key.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    if(other == null) {
      return false
    }
    return if(other is MetadataMessage) {
      key == other.key && value === other.value
    } else false
  }
}
