package com.languagecomputer.services.messages.text

import java.util.*

/**
 * Represents a span of text in a document.  Assuming the document type is capable of being parsed consistently,
 * these should align with 3rd party representations of the same document.  This is not necessarily the case for things like HTML or PDF, if alignment
 * with LCC characters is desired, we recommend using one of the supported document types.
 * @author smonahan
 */
open class CharSpanIdentifier : Comparable<CharSpanIdentifier?> {
  /**
   * Returns the document ID associated with this identifier.
   */
  var documentID: String = ""
    protected set
  /**
   * Returns the 0-based start character offset associated with this identifier.
   */
  var startChar: Int = 0
    protected set
  /**
   * Returns the character length associated with this identifier.
   */
  var charLength: Int = 0
    protected set

  //Note: this transient annotation may only be relevant if this class inherits from Serializable
  @Transient
  var associatedString: String? = null
    protected set

  private constructor() {}

  /**
   * Constructor for unique identifiers.
   * Note: added for Java derived classes that want to use these three arguments
   */
  protected constructor(documentID: String, startChar: Int, charLength: Int) : this() {
    this.documentID = documentID
    this.startChar = startChar
    this.charLength = charLength
  }

  protected constructor(documentID: String, startChar: Int, charLength: Int, associatedString: String? = null) : this() {
    this.documentID = documentID
    this.startChar = startChar
    this.charLength = charLength
    this.associatedString = associatedString
  }

  protected constructor(that: CharSpanIdentifier) : this(that.documentID, that.startChar, that.charLength, that.associatedString) {}

  class Builder {
    private var documentID: String? = null
    private var startChar: Int = 0
    private var charLength: Int = 0
    private var associatedString: String? = null

    // Note: private constructor to enforce inputting documentID, startChar, and charLength; a data class does not allow a private constructor to enforce this
    private constructor() : this("DUMMY", 0, 0)

    //Note: for calls from Java
    constructor(documentID: String, startChar: Int, charLength: Int) : this(documentID, startChar, charLength, null) {}

    constructor(documentID: String, startChar: Int, charLength: Int, associatedString: String?) {
      this.documentID = documentID
      this.startChar = startChar
      this.charLength = charLength
      this.associatedString = associatedString
    }

    constructor(that: CharSpanIdentifier) : this(that.documentID, that.startChar, that.charLength, that.associatedString) {}

    fun documentID(documentID: String) = apply { this.documentID = documentID }
    fun startChar(startChar: Int) = apply { this.startChar = startChar }
    fun charLength(charLength: Int) = apply { this.charLength = charLength }
    fun associatedString(associatedString: String) = apply { this.associatedString = associatedString }
    // shifts start character left in the document or down in value
    fun shiftLeft(shift: Int) = apply { this.startChar -= shift }
    // shifts start character right in the document or up in value
    fun shiftRight(shift: Int) = apply { this.startChar += shift }

    fun build() = CharSpanIdentifier(documentID!!, startChar, charLength, associatedString)
  }

  override fun toString(): String {
    return if (associatedString == null) {
      "$documentID:$startChar:$charLength:C"
    } else {
      "$documentID:$startChar:$charLength:$associatedString:C"
    }
  }

  /**
   * Note: This function did not check for associatedString in
   * the unlikely case it was accidentally wrong.
   */
  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other !is CharSpanIdentifier) return false
    val that = other
    if(startChar != that.startChar) return false
    return charLength == that.charLength && documentID == that.documentID
  }

  override fun hashCode(): Int {
    var result = documentID.hashCode()
    result = 31 * result + startChar
    result = 31 * result + charLength
    return result
  }

  override operator fun compareTo(other: CharSpanIdentifier?): Int {
    if(other == null) {
      return -1
    }
    var cmp = documentID.compareTo(other.documentID)
    if(cmp == 0) {
      cmp = Integer.compare(startChar, other.startChar)
    }
    if(cmp == 0) {
      cmp = Integer.compare(charLength, other.charLength)
    }
    return cmp
  }

  /**
   * Returns whether this class is a subspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param startCharIndex The 0-based char index of the start of the passed-in span
   * @param charLength The length in chars of the passed-in span
   * @return whether this class is a subspan of the passed-in parameters
   */
  fun isSubspanOf(startCharIndex: Int, charLength: Int): Boolean {
    return (startChar >= startCharIndex
            && startChar + this.charLength <= startCharIndex + charLength)
  }

  /**
   * Test if two char span identifiers intersect
   */
  fun intersects(that: CharSpanIdentifier): Boolean {
    return documentID == that.documentID &&
            !(startChar >= that.startChar + that.charLength ||
                    startChar + charLength <= that.startChar)
  }

  /**
   * Finds intersection of two span identifiers (or null if they do not overlap at all).
   */
  fun intersection(that: CharSpanIdentifier): CharSpanIdentifier? {
    return if (!intersects(that)) {
      null
    } else {
      val start = Math.max(startChar, that.startChar)
      val end = Math.min(startChar + charLength, that.startChar + that.charLength)
      Builder(documentID, start, end - start).build()
    }
  }

  /**
   * Finds intersection of two span identifiers (or null if they are not the same document).
   */
  fun union(that: CharSpanIdentifier): CharSpanIdentifier? {
    return if (documentID != that.documentID) {
      null
    } else {
      val start = Math.min(startChar, that.startChar)
      val end = Math.max(startChar + charLength, that.startChar + that.charLength)
      Builder(documentID, start, end - start).build()
    }
  }


  /**
   * Returns whether this class is a subspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param si SpanIdentifier to compare with
   * @return whether this class is a subspan of the passed-in parameters
   */
  fun isSubspanOf(si: CharSpanIdentifier): Boolean {
    return (startChar >= si.startChar
            && startChar + charLength <= si.startChar + si.charLength)
  }

  /**
   * Returns whether this class is a superspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param startCharIndex The 0-based character index of the start of the passed-in span
   * @param charLength The length in characters of the passed-in span
   * @return whether this class is a superspan of the passed-in parameters
   */
  fun isSuperspanOf(startCharIndex: Int, charLength: Int): Boolean {
    return (startChar <= startCharIndex
            && startChar + this.charLength >= startCharIndex + charLength)
  }

  /**
   * Returns whether this class is a superspan of the passed-in parameters.
   * If this class has the same span as the passed-in parameters, it will return true.
   * @param si Span identifier to compare with
   * @return whether this class is a superspan of the passed-in parameters
   */
  fun isSuperspanOf(si: CharSpanIdentifier): Boolean {
    return (startChar <= si.startChar
            && startChar + charLength >= si.startChar + si.charLength)
  }

  /**
   * Returns whether this class spans the 0-based input char offset.
   */
  fun contains(offset: Int): Boolean {
    return (startChar <= offset
            && startChar + charLength > offset)
  }

  /**
   * Validating @startChar and @charLength
   */
  fun isValid(): Boolean {
    return (startChar >= 0 && charLength > 0)
  }

  fun toLongString(): String {
    val startCharPadded = String.format("%07d", startChar)
    val charLengthPadded = String.format("%02d", charLength)
    return "$documentID:$startCharPadded:$charLengthPadded:C"
  }

  companion object {
    /**
     * Create a span identifier from the toString representation, used for serialization
     */
    fun from(string: String?): CharSpanIdentifier? {
      if(string == null) {
        return null
      }
      val splits = string.split(":".toRegex()).toTypedArray()
      return if ((splits.size == 3) // has form $documentID:$startChar:$charLength
              || ( (splits.size == 4) && (splits[splits.size-1] == "C") ) // has form $documentID:$startChar:$charLength:C" at end
      ) {
        CharSpanIdentifier(splits[0], Integer.valueOf(splits[1]), Integer.valueOf(splits[2]))
      } else if (splits.size > 4) { // has form $documentID:$startChar:$charLength:$associatedString:C" at end
        var lastIndex = splits.size - 1
        if (splits[splits.size - 1] == "C") {
          lastIndex = splits.size - 2
        }
        val associatedStringBuilder = StringBuilder()
        var i = 3
        while (i <= lastIndex) {
          associatedStringBuilder.append(splits[i])
          i++
          if (i <= lastIndex) {
            associatedStringBuilder.append(":")
          }
        }
        CharSpanIdentifier(splits[0], Integer.valueOf(splits[1]), Integer.valueOf(splits[2]), associatedStringBuilder.toString())
      } else {
        null
      }
    }

    @JvmStatic val EARLIEST_THEN_LARGEST = Comparator<CharSpanIdentifier> { a, b ->
      var cmp = a.documentID.compareTo(b.documentID)
      if (cmp == 0) {
        cmp = Integer.compare(a.startChar, b.startChar)
      }
      if (cmp == 0) {
        cmp = Integer.compare(b.charLength, a.charLength)
      }
      cmp
    }
  }
}
