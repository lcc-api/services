package com.languagecomputer.services.api

import com.languagecomputer.services.lexicalstore.LexicalItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LexicalItemTest {

    @Test
    fun testConstructor1() {
        val word = "test"
        val pos = ""
        val stemmed = false
        val lexicalItem = LexicalItem(word, pos, stemmed)
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }

    @Test
    fun testConstructorEmptyStemmed() {
        val word = "test"
        val pos = ""
        val lexicalItem = LexicalItem(word, pos)
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        Assertions.assertEquals(false, lexicalItem.stemmed)
    }

    @Test
    fun testBuilder1() {
        val word = "test"
        val pos = "ANY"
        val stemmed = false
        val lexicalItem = LexicalItem.Builder(
                word = word,
                pos = pos,
                stemmed = stemmed
        ).build()
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }

    @Test
    fun testBuilderCopyConstructor1() {
        //Note: empty Builder constructor not allowed. val lexicalItemDefault = LexicalItem.Builder().build();

        val lexicalItem = LexicalItem.Builder(
                word = "testing",
                pos = "NOUN",
                stemmed = true
        ).build()
        // use builder copy constructor
        val lexicalItemCopy = LexicalItem.Builder(lexicalItem).build()
        Assertions.assertEquals(lexicalItem, lexicalItemCopy)

        // use builder copy constructor and then set each value
        val word = "test"
        val pos = "ANY"
        val stemmed = false
        val lexicalItemCopy2 =
                LexicalItem.Builder(lexicalItem)
                        .word(word)
                        .pos(pos)
                        .stemmed(stemmed)
                        .build()
        Assertions.assertEquals(word, lexicalItemCopy2.word)
        Assertions.assertEquals(pos, lexicalItemCopy2.pos)
        Assertions.assertEquals(stemmed, lexicalItemCopy2.stemmed)
    }

    @Test
    fun testFromLexical() {
        val word = "test"
        val posDefault = "ANY"
        val stemmedDefault = false
        val lexicalItem1 = LexicalItem.fromLexical(word)
        Assertions.assertEquals(word, lexicalItem1.word)
        Assertions.assertEquals(posDefault, lexicalItem1.pos)
        Assertions.assertEquals(stemmedDefault, lexicalItem1.stemmed)

        val pos = "NOUN"
        val lexical2 = "$word:$pos"
        val lexicalItem2 = LexicalItem.fromLexical(lexical2)
        Assertions.assertEquals(word, lexicalItem2.word)
        Assertions.assertEquals(pos, lexicalItem2.pos)
        Assertions.assertEquals(stemmedDefault, lexicalItem2.stemmed)

        val stemmed = true
        val lexical3 = "$word:$pos:$stemmed"
        val lexicalItem3 = LexicalItem.fromLexical(lexical3)
        Assertions.assertEquals(word, lexicalItem3.word)
        Assertions.assertEquals(pos, lexicalItem3.pos)
        Assertions.assertEquals(stemmed, lexicalItem3.stemmed)
    }
}
