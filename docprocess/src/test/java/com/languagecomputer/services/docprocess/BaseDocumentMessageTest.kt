package com.languagecomputer.services.docprocess

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.time.format.DateTimeParseException

internal class BaseDocumentMessageTest {
    @Test
    internal fun testDateParseError1_1() {
        Assertions.assertThrows(DateTimeParseException::class.java) {
            // no hours, minutes, or seconds provided
            BaseDocumentMessage.dateParse("2021-07-15")
        }
    }

    @Test
    internal fun testDateParseError1_2() {
        Assertions.assertThrows(DateTimeParseException::class.java) {
            // no hours, minutes, or seconds provided
            BaseDocumentMessage.dateParse("2021-07-15T")
        }
    }

    @Test
    internal fun testDateParseError1_3() {
        Assertions.assertThrows(DateTimeParseException::class.java) {
            // no seconds provided
            BaseDocumentMessage.dateParse("2021-07-15T10:04")
        }
    }

    @Test
    internal fun testDateParseError1_4() {
        Assertions.assertThrows(DateTimeParseException::class.java) {
            // no 2 digit seconds provided
            BaseDocumentMessage.dateParse("2021-07-15T08:13:3")
        }
    }

    @Test
    internal fun testDateParseError2() {
        Assertions.assertThrows(DateTimeParseException::class.java) {
            BaseDocumentMessage.dateParse("07/15/2021")
        }
    }

    @Test
    internal fun testDateParse() {
        val date = BaseDocumentMessage.dateParse("2021-07-15T07:11:00")
        Assertions.assertNotNull(date)
    }
}