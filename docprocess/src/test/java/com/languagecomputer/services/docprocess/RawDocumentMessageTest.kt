package com.languagecomputer.services.docprocess

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawDocumentMessageTest {
    @Test
    internal fun testDateTimeAsString() {
        val documentType = DocumentType.DOCID
        val documentID = "EMPTY_DOCUMENT"
        val dateTimeString = "2020-05-15"
        val payload = ByteArray(0)
        val rawDocumentMessage = RawDocumentMessage(documentType, documentID, dateTimeString, payload)
        Assertions.assertNotNull(rawDocumentMessage)
        Assertions.assertEquals(documentType, rawDocumentMessage.type)
        Assertions.assertEquals(documentID, rawDocumentMessage.documentID)
        Assertions.assertEquals(dateTimeString, rawDocumentMessage.dateTimeString)
        Assertions.assertEquals(payload, rawDocumentMessage.payload)

        val rawDocumentMessageCopy = RawDocumentMessage.Builder(rawDocumentMessage).build()
        Assertions.assertNotNull(rawDocumentMessageCopy)
        Assertions.assertEquals(documentType, rawDocumentMessageCopy.type)
        Assertions.assertEquals(documentID, rawDocumentMessageCopy.documentID)
        Assertions.assertEquals(dateTimeString, rawDocumentMessageCopy.dateTimeString)
        Assertions.assertEquals(payload, rawDocumentMessageCopy.payload)
    }

    @Test
    internal fun testDateTimeAsInstant() {
        val documentType = DocumentType.DOCID
        val documentID = "EMPTY_DOCUMENT"
        val dateTimeInstant = Instant.now()
        val payload = ByteArray(0)
        val rawDocumentMessage = RawDocumentMessage(documentType, documentID, dateTimeInstant, payload)
        Assertions.assertNotNull(rawDocumentMessage)
        Assertions.assertEquals(documentType, rawDocumentMessage.type)
        Assertions.assertEquals(documentID, rawDocumentMessage.documentID)
        //TODO: I would have expected this to work.
        //Assertions.assertEquals(dateTimeInstant, rawDocumentMessage.dateTimeAsInstant())
        Assertions.assertEquals(payload, rawDocumentMessage.payload)

        val rawDocumentMessageCopy = RawDocumentMessage.Builder(rawDocumentMessage).build()
        Assertions.assertNotNull(rawDocumentMessageCopy)
        Assertions.assertEquals(documentType, rawDocumentMessageCopy.type)
        Assertions.assertEquals(documentID, rawDocumentMessageCopy.documentID)
        //TODO: I would have expected this to work.
        //Assertions.assertEquals(dateTimeInstant, rawDocumentMessageCopy.dateTimeAsInstant())
        Assertions.assertEquals(payload, rawDocumentMessageCopy.payload)
    }
}