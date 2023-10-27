package com.languagecomputer.services.messages.text

/**
 * Object that represents a span of text using LCC's internal tokenization
 * @author smonahan
 */
open class TokenSpanIdentifier (
  /**
   * Returns the document ID associated with this identifier.
   */
  val documentID: String,
  /**
   * Returns the start token offset associated with this identifier.
   */
  val startToken: Int,
  /**
   * Returns the token length associated with this identifier.
   */
  val tokenLength: Int) : Comparable<TokenSpanIdentifier?> {

  @Transient
  var associatedString: String? = null

  constructor(documentID: String, startToken: Int, tokenLength: Int, associatedString : String) : 
    this(documentID, startToken, tokenLength) {
    this.associatedString = associatedString
  }

  override fun toString(): String {
    return "$documentID:$startToken:$tokenLength"
  }

  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other !is TokenSpanIdentifier) return false
    if(startToken != other.startToken) return false
    return tokenLength == other.tokenLength && documentID == other.documentID
  }

  override fun hashCode(): Int {
    var result = documentID.hashCode()
    result = 31 * result + startToken
    result = 31 * result + tokenLength
    return result
  }

  override operator fun compareTo(other: TokenSpanIdentifier?): Int {
    if(other == null) {
      return -1
    }
    var cmp = documentID.compareTo(other.documentID)
    if(cmp == 0) {
      cmp = startToken - other.startToken
    }
    if(cmp == 0) {
      cmp = tokenLength - other.tokenLength
    }
    return cmp
  }

  /**
   * Returns whether this class is a subspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param startTokenIndex The 0-based token index of the start of the passed-in span
   * @param tokenLength The length in tokens of the passed-in span
   * @return whether this class is a subspan of the passed-in parameters
   */
  fun isSubspanOf(startTokenIndex: Int, tokenLength: Int): Boolean {
    return (startToken >= startTokenIndex
            && startToken + this.tokenLength <= startTokenIndex + tokenLength)
  }

  /**
   * Test if two span identifiers intersect
   * @param that
   * @return
   */
  fun intersects(that: TokenSpanIdentifier?): Boolean {
    return that != null && documentID == that.documentID &&
            !(startToken >= that.startToken + that.tokenLength ||
                    startToken + tokenLength <= that.startToken)
  }

  /**
   * Returns whether this class is a subspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param si SpanIdentifier to compare with
   * @return whether this class is a subspan of the passed-in parameters
   */
  fun isSubspanOf(si: TokenSpanIdentifier): Boolean {
    return (startToken >= si.startToken
            && startToken + tokenLength <= si.startToken + si.tokenLength)
  }

  /**
   * Returns whether this class is a superspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param startTokenIndex The 0-based token index of the start of the passed-in span
   * @param tokenLength The length in tokens of the passed-in span
   * @return whether this class is a superspan of the passed-in parameters
   */
  fun isSuperspanOf(startTokenIndex: Int, tokenLength: Int): Boolean {
    return (startToken <= startTokenIndex
            && startToken + this.tokenLength >= startTokenIndex + tokenLength)
  }

  /**
   * Returns whether this class is a superspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param si Span identifier to compare with
   * @return whether this class is a superspan of the passed-in parameters
   */
  fun isSuperspanOf(si: TokenSpanIdentifier): Boolean {
    return (startToken <= si.startToken
            && startToken + tokenLength >= si.startToken + si.tokenLength)
  }

  fun toLongString(): String {
    val startTokenPadded = String.format("%07d", startToken)
    val tokenLengthPadded = String.format("%02d", tokenLength)
    return "$documentID:$startTokenPadded:$tokenLengthPadded"
  }

  /**
   * Create a span identifier from the toString representation, used for serialization
   */
  companion object {
    fun from(string: String?): TokenSpanIdentifier? {
      if(string == null) {
        return null
      }
      // There may be cases where the docid contains a ':'
      val splits = string.split(":".toRegex())
      val len = splits.size
      if(len < 3) {
        return null
      }
      val docid: String = splits.subList(0, len - 2).joinToString(":")
      return TokenSpanIdentifier(docid, Integer.valueOf(splits[len - 2]), Integer.valueOf(splits[len - 1]))
    }
  }
}
