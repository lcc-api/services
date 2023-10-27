package com.languagecomputer.services.vectorstore

/**
 * Wrapper class for a vector with an ID.
 *
 */
data class VectorMessage(val id: String, val vector: DoubleArray) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
      other as VectorMessage
    if (id != other.id) return false
    if (!vector.contentEquals(other.vector)) return false
    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + vector.contentHashCode()
    return result
  }
}