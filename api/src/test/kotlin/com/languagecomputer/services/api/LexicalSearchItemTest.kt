package com.languagecomputer.services.api

import com.languagecomputer.services.lexicalstore.LexicalSearchItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LexicalSearchItemTest {

    @Test
    fun testConstructor1() {
        val word = "test"
        val pos = ""
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testNullPosConstructor1() {
        val word = "test"
        // Note: added type String? to avoid Intellij IDEA warning (from View|ToolWindows|Problems) 'assertEquals()' between objects of inconvertible types 'Void' and 'String'
        val pos:String? = null
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testConstructorEmptyStemmed() {
        val word = "test"
        val pos = ""
        val lexicalSearchItem = LexicalSearchItem(word, pos)
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(null, lexicalSearchItem.stemmed)
    }

    @Test
    fun testConstructorNoPosNoStemmed() {
        val word = "test"
        val pos = ""
        val stemmed:Boolean? = null
        val lexicalSearchItem = LexicalSearchItem(word)
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testEmptyConstructorBuilder() {
        val word = "test"
        val pos = "ANY"
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem.Builder(word, pos, stemmed).build()
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testBuilder1() {
        val word = "test"
        val pos = "ANY"
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem.Builder(
                word = word,
                pos = pos,
                stemmed = stemmed
        ).build()
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testBuilderNullPos() {
        val word = "test"
        val pos:String? = null
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem.Builder(
                word = word,
                pos = pos,
                stemmed = stemmed
        ).build()
        Assertions.assertEquals(word, lexicalSearchItem.word)
        Assertions.assertEquals(pos, lexicalSearchItem.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem.stemmed)
    }

    @Test
    fun testBuilderCopyConstructor1() {
        //Note: empty Builder constructor not allowed. val lexicalItemDefault = LexicalSearchItem.Builder().build();

        val lexicalSearchItem = LexicalSearchItem.Builder(
                word = "testing",
                pos = "NOUN",
                stemmed = true
        ).build()
        // use builder copy constructor
        val lexicalSearchItemCopy = LexicalSearchItem.Builder(lexicalSearchItem).build()
        Assertions.assertEquals(lexicalSearchItem, lexicalSearchItemCopy)

        // use builder copy constructor and then set each value
        val word = "test"
        val pos = "ANY"
        val stemmed = false
        val lexicalSearchItemCopy2 =
                LexicalSearchItem.Builder(lexicalSearchItem)
                        .word(word)
                        .pos(pos)
                        .stemmed(stemmed)
                        .build()
        Assertions.assertEquals(word, lexicalSearchItemCopy2.word)
        Assertions.assertEquals(pos, lexicalSearchItemCopy2.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItemCopy2.stemmed)
    }

    @Test
    fun testBuilderEmptyPosCopyConstructor1() {
        //Note: empty Builder constructor not allowed. val lexicalItemDefault = LexicalSearchItem.Builder().build();

        val lexicalSearchItem = LexicalSearchItem.Builder(
                word = "testing",
                pos = null,
                stemmed = true
        ).build()
        // use builder copy constructor
        val lexicalSearchItemCopy = LexicalSearchItem.Builder(lexicalSearchItem).build()
        Assertions.assertEquals(lexicalSearchItem, lexicalSearchItemCopy)
    }

    @Test
    fun testFromSearchLexical() {
        val word = "test"
        val posDefault = ""
        val stemmedDefault:Boolean? = null

        val lexical1 = "test"
        val lexicalSearchItem1 = LexicalSearchItem.fromSearchLexical(lexical1)
        Assertions.assertEquals(word, lexicalSearchItem1.word)
        Assertions.assertEquals(posDefault, lexicalSearchItem1.pos)
        Assertions.assertEquals(stemmedDefault, lexicalSearchItem1.stemmed)

        val pos = "NOUN"
        val lexical2 = "$word:$pos"
        val lexicalSearchItem2 = LexicalSearchItem.fromSearchLexical(lexical2)
        Assertions.assertEquals(word, lexicalSearchItem2.word)
        Assertions.assertEquals(pos, lexicalSearchItem2.pos)
        Assertions.assertEquals(stemmedDefault, lexicalSearchItem2.stemmed)

        val stemmed = true
        val lexical3 = "$word:$pos:$stemmed"
        val lexicalSearchItem3 = LexicalSearchItem.fromSearchLexical(lexical3)
        Assertions.assertEquals(word, lexicalSearchItem3.word)
        Assertions.assertEquals(pos, lexicalSearchItem3.pos)
        Assertions.assertEquals(stemmed, lexicalSearchItem3.stemmed)

        //Note: can not test LexicalSearchItem.fromSearchLexical with a null pos
    }

    @Test
    fun testNounPosTruePosConvertToLexicalItem() {
        // test LexicalSearchItem conversion to LexicalItem
        val word = "test"
        val pos = "NOUN"
        val stemmed = true
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItemConverted = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItemConverted.word)
        Assertions.assertEquals(pos, lexicalItemConverted.pos)
        Assertions.assertEquals(stemmed, lexicalItemConverted.stemmed)
    }

    @Test
    fun testNullPosTrueStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = null
        val stemmed = true
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        //Note: null pos converts to empty string
        Assertions.assertEquals("", lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }

    @Test
    fun testNullPosFalseStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = null
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        //Note: null pos converts to empty string
        Assertions.assertEquals("", lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }

    @Test
    fun testEmptyPosTrueStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = ""
        val stemmed = true
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }

    @Test
    fun testNullPosNullStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = null
        val stemmed = null
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        //Note: null pos converts to empty string
        Assertions.assertEquals("", lexicalItem.pos)
        //Note: null stemmed converts to false
        Assertions.assertEquals(false, lexicalItem.stemmed)
    }

    @Test
    fun testEmptyPosNullStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = ""
        val stemmed = null
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        //Note: null stemmed converts to false
        Assertions.assertEquals(false, lexicalItem.stemmed)
    }

    @Test
    fun testVerbPosNullStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = "VERB"
        val stemmed = null
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        //Note: null stemmed converts to false
        Assertions.assertEquals(false, lexicalItem.stemmed)
    }

    @Test
    fun testVerbPosFalseStemmedConvertToLexicalItem() {
        val word = "test"
        val pos = "VERB"
        val stemmed = false
        val lexicalSearchItem = LexicalSearchItem(word, pos, stemmed)
        val lexicalItem = LexicalSearchItem.Builder(lexicalSearchItem).buildLexicalItem()
        Assertions.assertEquals(word, lexicalItem.word)
        Assertions.assertEquals(pos, lexicalItem.pos)
        Assertions.assertEquals(stemmed, lexicalItem.stemmed)
    }
}
