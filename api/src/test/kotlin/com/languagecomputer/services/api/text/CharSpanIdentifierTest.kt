package com.languagecomputer.services.api.text

import com.languagecomputer.services.messages.text.CharSpanIdentifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CharSpanIdentifierTest {

    @Test
    fun testSerializationDeserialization() {
        val charSpanIdentifier = CharSpanIdentifier.Builder(
                startChar = 0,
                charLength = 1,
                documentID = "testDocID"
        ).build()
        val csiString = charSpanIdentifier.toString()
        Assertions.assertFalse(csiString.contains("associatedString"))
        val resultCharSpanIdentifier = CharSpanIdentifier.from(csiString)
        Assertions.assertEquals(charSpanIdentifier, resultCharSpanIdentifier, "Expected CharSpanIdentifiers to be the same.")
        Assertions.assertEquals(charSpanIdentifier.associatedString, resultCharSpanIdentifier?.associatedString, "Expected CharSpanIdentifiers associatedString to be the same.")
    }

    @Test
    fun testSerializationDeserializationWithAssociatedString() {
        val charSpanIdentifier = CharSpanIdentifier.Builder(
                startChar = 0,
                charLength = 1,
                documentID = "testDocID",
                associatedString = "test:with:colon"
        ).build()
        val csiString = charSpanIdentifier.toString()
        Assertions.assertFalse(csiString.contains("associatedString"))
        val resultCharSpanIdentifier = CharSpanIdentifier.from(csiString)
        Assertions.assertEquals(charSpanIdentifier, resultCharSpanIdentifier, "Expected CharSpanIdentifiers to be the same.")
        Assertions.assertEquals(charSpanIdentifier.associatedString, resultCharSpanIdentifier?.associatedString, "Expected CharSpanIdentifiers associatedString to be the same.")
    }

    @Test
    fun testCopyConstructor1() {
        //Note: empty Builder constructor not allowed. val charSpanIdentifierDefault = CharSpanIdentifier.Builder().build();

        val charSpanIdentifier = CharSpanIdentifier.Builder(
                startChar = 0,
                charLength = 1,
                documentID = "testDocID"
        ).build()
        val charSpanIdentifierCopy = CharSpanIdentifier.Builder(charSpanIdentifier).build()
        Assertions.assertEquals(charSpanIdentifier, charSpanIdentifierCopy)
    }

    @Test
    fun testCopyConstructor2() {
        val charSpanIdentifier = CharSpanIdentifier.Builder(
                startChar = 0,
                charLength = 1,
                documentID = "testDocID",
                associatedString = "testAssociatedString"
        ).build()
        val charSpanIdentifierCopy = CharSpanIdentifier.Builder(charSpanIdentifier).build()
        Assertions.assertEquals(charSpanIdentifier, charSpanIdentifierCopy)
    }

}
