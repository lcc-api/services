package com.languagecomputer.services.docprocess

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

internal class DocumentMessageTest {
    @Test
    internal fun test1() {
        // Note: DocumentMessage class currently has public constructors although has partial internal Builder classes
        val documentID = "testDocId"
        val dateTimeString = "07/15/2021"
        val documentMessage1_1 = DocumentMessage(type = DocumentType.RAW_TEXT, documentID = "testDocId", dateTimeString = dateTimeString)
        Assertions.assertNotNull(documentMessage1_1)
        Assertions.assertEquals(documentID, documentMessage1_1.documentID)
        Assertions.assertEquals(dateTimeString, documentMessage1_1.dateTimeString)

        val documentMessage1_2 = DocumentMessage(DocumentType.RAW_TEXT, documentID, dateTimeString)
        Assertions.assertNotNull(documentMessage1_2)
        Assertions.assertEquals(documentID, documentMessage1_2.documentID)
        Assertions.assertEquals(dateTimeString, documentMessage1_2.dateTimeString)

        val instantNow = Instant.now()
        val documentMessage2_1 = DocumentMessage(type = DocumentType.RAW_TEXT, documentID = documentID, documentDate = instantNow)
        Assertions.assertNotNull(documentMessage2_1)
        Assertions.assertEquals(documentID, documentMessage2_1.documentID)
        //TODO: I would have expected this to work.
        //Assertions.assertEquals(instantNow, documentMessage2_1.dateTimeAsInstant())

        val documentMessage2_2 = DocumentMessage(DocumentType.RAW_TEXT, documentID, instantNow)
        Assertions.assertNotNull(documentMessage2_2)
        Assertions.assertEquals(documentID, documentMessage2_2.documentID)
        //TODO: I would have expected this to work.
        //Assertions.assertEquals(instantNow, documentMessage2_2.dateTimeAsInstant())
    }

    @Test
    internal fun testBuilder1() {
        // Note: DocumentMessage class currently has public constructors although has partial internal Builder classes
        val documentID = "testDocId"
        val dateTimeString = "07/15/2021"
        val documentMessage1_1 = DocumentMessage(type = DocumentType.RAW_TEXT, documentID = "testDocId", dateTimeString = dateTimeString)
        Assertions.assertNotNull(documentMessage1_1)
        Assertions.assertEquals(documentID, documentMessage1_1.documentID)
        Assertions.assertEquals(dateTimeString, documentMessage1_1.dateTimeString)
        Assertions.assertNull(documentMessage1_1.payload)

        val documentMessageCopy1_1 = DocumentMessage.Builder(documentMessage1_1).build()
        Assertions.assertNotNull(documentMessageCopy1_1)
        Assertions.assertEquals(documentID, documentMessageCopy1_1.documentID)
        Assertions.assertEquals(dateTimeString, documentMessageCopy1_1.dateTimeString)
        Assertions.assertNull(documentMessageCopy1_1.payload)
    }
}