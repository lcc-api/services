package com.languagecomputer.services.docstore

/**
 * Map of metadata to its value
 */
data class MetadataMap(var metadataMap: MutableMap<String, Collection<String>> = HashMap()) {
  fun put(key: String, value: Collection<String>) {
    metadataMap[key] = value
  }

  operator fun set(key: String, value: Collection<String>) {
    metadataMap[key] = value
  }

  operator fun get(key: String?): Collection<String>? {
    return metadataMap[key]
  }

  override fun toString(): String {
    return metadataMap.toString()
  }
}
