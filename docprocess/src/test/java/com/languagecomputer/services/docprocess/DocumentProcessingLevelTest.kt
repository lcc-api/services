package com.languagecomputer.services.docprocess

import com.languagecomputer.services.examplestore.ExampleUpdateStrategy
import com.languagecomputer.services.docprocess.DocumentProcessingLevel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DocumentProcessingLevelTest {
    @Test
    internal fun light() {
        val level = DocumentProcessingLevel.Level.LIGHT;
        val exampleUpdateStrategy = null;
        val dpl = DocumentProcessingLevel(level, exampleUpdateStrategy)
        Assertions.assertEquals(level, dpl.level);
        Assertions.assertEquals(exampleUpdateStrategy, dpl.exampleUpdateStrategy);
    }

    @Test
    internal fun core() {
        val level = DocumentProcessingLevel.Level.CORE;
        val exampleUpdateStrategy = null;
        val dpl = DocumentProcessingLevel(level, exampleUpdateStrategy)
        Assertions.assertEquals(level, dpl.level);
        Assertions.assertEquals(exampleUpdateStrategy, dpl.exampleUpdateStrategy);
    }

    @Test
    internal fun heavy() {
        val level = DocumentProcessingLevel.Level.HEAVY;
        val exampleUpdateStrategy = null;
        val dpl = DocumentProcessingLevel(level, exampleUpdateStrategy)
        Assertions.assertEquals(level, dpl.level);
        Assertions.assertEquals(exampleUpdateStrategy, dpl.exampleUpdateStrategy);
    }

    @Test
    internal fun example() {
        val level = DocumentProcessingLevel.Level.EXAMPLE;
        val exampleUpdateStrategy = ExampleUpdateStrategy.ADD_OR_UPDATE_ALL;
        val dpl = DocumentProcessingLevel(level, exampleUpdateStrategy)
        Assertions.assertEquals(level, dpl.level);
        Assertions.assertEquals(exampleUpdateStrategy, dpl.exampleUpdateStrategy);
    }
}