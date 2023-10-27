package com.languagecomputer.services.lexicalstore

import com.google.common.base.MoreObjects
import com.google.common.base.Preconditions

/**
 * A search request for lexical items matching this.  The pos and stemmed parameters are optional.
 *
 */
data class LexicalSearchItem @JvmOverloads constructor(
  val word: String,
  val pos: String? = "", // null or empty string in pos means it could be any value; "ANY" still only searches the "ANY" string
  val stemmed: Boolean? = null, // null meaning not specified for searching; boolean value to specify whether the word also includes its stems
) : Comparable<LexicalSearchItem> {
  init {
    Preconditions.checkArgument(word.trim { it <= ' ' }.isNotEmpty(), "word is empty")
  }

  override fun compareTo(other: LexicalSearchItem) =
    compareValuesBy(this, other,
      { it.word }, { it.pos }, { it.stemmed })

  fun isPosSearch(): Boolean {
    return ( (pos === null) || (pos === "") )
  }

  fun isStemmedSearch(): Boolean {
    return (stemmed === null)
  }

  companion object {
    // used if it's a lexical used for searching
    // converts from <lexeme>:<pos>:<stemmed> to LexicalSearchItem
    // if the pos isn't specified, it will use an empty string which
    // means it can be any value.
    @JvmStatic
    fun fromSearchLexical(lexical: String): LexicalSearchItem {
      val lexParts = lexical.split(':')
      val word = lexParts[0]
      var pos = ""
      if (lexParts.size > 1) {
        pos = lexParts[1]
      }
      var stemmed:Boolean? = null
      if (lexParts.size > 2) {
        stemmed = lexParts[2].toBoolean()
      }
      return LexicalSearchItem(word, pos, stemmed)
    }
  }

  class Builder {
    private var word: String = ""
    private var pos: String? = ""
    private var stemmed: Boolean? = null

    // Note: private constructor to enforce using other constructor with word, pos, and stemmed
    @Suppress("unused")
    private constructor() : this("", "", false)

    constructor(word: String, pos: String?, stemmed: Boolean?) {
      this.word = word
      this.pos = pos
      this.stemmed = stemmed
    }

    constructor(that: LexicalSearchItem) : this(that.word, that.pos, that.stemmed)

    fun word(word: String) = apply { this.word = word }
    fun pos(pos: String?) = apply { this.pos = pos }
    fun stemmed(stemmed: Boolean) = apply { this.stemmed = stemmed }

    fun build() = LexicalSearchItem(word, pos, stemmed)
    fun buildLexicalItem(): LexicalItem = LexicalItem(word, pos ?: "", stemmed ?: false)
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(this)
      .add("word", word)
      .add("pos", pos)
      .add("stemmed", stemmed)
      .toString()
  }
}
