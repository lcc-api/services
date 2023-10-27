package com.languagecomputer.services.lexicalstore

import com.google.common.base.MoreObjects
import com.google.common.base.Preconditions

/**
 * Used to bundle information about a lexeme.
 *
 */
data class LexicalItem @JvmOverloads constructor(
  val word: String,
  val pos: String = "ANY",
  val stemmed: Boolean = false,
) : Comparable<LexicalItem> {
  init {
    Preconditions.checkArgument(word.trim { it <= ' ' }.isNotEmpty(), "word is empty")
  }

  override fun compareTo(other: LexicalItem) =
          compareValuesBy(this, other,
                  { it.word }, { it.pos }, { it.stemmed })

  companion object {
    // used if it's specifying a lexical
    // converts from <lexeme>:<pos>:<stemmed> to LexicalItem
    // if the pos isn't specified, the value 'ANY' will be used.
    @JvmStatic
    fun fromLexical(lexical: String): LexicalItem {
      val lexParts = lexical.split(':')
      val word = lexParts[0]
      var pos = "ANY"
      if (lexParts.size > 1) {
        pos = lexParts[1]
      }
      var stemmed = false
      if (lexParts.size > 2) {
        stemmed = lexParts[2].toBoolean()
      }
      return LexicalItem(word, pos, stemmed)
    }

    @JvmStatic
    fun from(objectList: List<Any?>): LexicalItem {
      assert(objectList.size == 3) {"Expecting a list of 3 objects(word, pos, stemmed"}
      var word = ""
      if (objectList[0] !== null) {
        word = objectList[0].toString()
      }

      var pos = ""
      if (objectList[1] !== null) {
        pos = objectList[1].toString()
      }

      var stemmed = false
      if (objectList[2] !== null) {
        stemmed = objectList[2].toString().toBoolean()
      }

      return LexicalItem (word, pos, stemmed)
    }
  }

  class Builder {
    private var word: String = ""
    private var pos: String = "ANY"
    private var stemmed: Boolean = false

    // Note: private constructor to enforce using other constructors
    @Suppress("unused")
    private constructor() : this("", "", false)

    constructor(word: String) {
      this.word = word
    }

    constructor(word: String, pos: String, stemmed: Boolean) {
      this.word = word
      this.pos = pos
      this.stemmed = stemmed
    }

    constructor(that: LexicalItem) : this(that.word, that.pos, that.stemmed)

    fun word(word: String) = apply { this.word = word }
    fun pos(pos: String) = apply { this.pos = pos }
    fun stemmed(stemmed: Boolean) = apply { this.stemmed = stemmed }

    fun build() = LexicalItem(word, pos, stemmed)
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(this)
      .add("word", word)
      .add("pos", pos)
      .add("stemmed", stemmed)
      .toString()
  }
}
