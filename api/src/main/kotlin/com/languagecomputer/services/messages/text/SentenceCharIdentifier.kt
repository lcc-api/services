package com.languagecomputer.services.messages.text

/**
 * An identifier for where a text of interest occurs within a sentence.
 * Represents the document, the sentence number, and the offset within that sentence.
 * @author smonahan
 */
open class SentenceCharIdentifier(val documentID: String, val firstSentenceNum: Int, val lastSentenceNum: Int, val charStartOffsetFromFirstSentence: Int, val charEndOffsetFromFirstSentence: Int) {
  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other !is SentenceCharIdentifier) return false
    if(firstSentenceNum != other.firstSentenceNum) return false
    if(lastSentenceNum != other.lastSentenceNum) return false
    return charStartOffsetFromFirstSentence == other.charStartOffsetFromFirstSentence && charEndOffsetFromFirstSentence == other.charEndOffsetFromFirstSentence
  }

  override fun hashCode(): Int {
    var result = firstSentenceNum
    result = 31 * result + lastSentenceNum
    result = 31 * result + charStartOffsetFromFirstSentence
    result = 31 * result + charEndOffsetFromFirstSentence
    return result
  }

  override fun toString(): String {
    return "SentenceCharIdentifier{" +
            "charEndOffsetFromFirstSentence=" + charEndOffsetFromFirstSentence +
            ", firstSentenceNum=" + firstSentenceNum +
            ", lastSentenceNum=" + lastSentenceNum +
            ", charStartOffsetFromFirstSentence=" + charStartOffsetFromFirstSentence +
            '}'
  }
}
