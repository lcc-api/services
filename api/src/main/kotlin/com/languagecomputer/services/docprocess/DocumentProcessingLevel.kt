package com.languagecomputer.services.docprocess

import com.languagecomputer.services.examplestore.ExampleUpdateStrategy

/**
 * The level to run the document through.  Each of these (except for example) correspond to
 * a gnerly level and a lucene index level.
 * @author smonahan
 */
class DocumentProcessingLevel(
        val level: Level,
        val exampleUpdateStrategy: ExampleUpdateStrategy? = null
) {
  enum class Level {
    LIGHT, CORE, HEAVY, EXAMPLE
  }

  override fun toString(): String {
    return "Level[$level:$exampleUpdateStrategy]"
  }

  companion object {
    // default conversion from Level to DocumentProcessingLevel
    @JvmStatic
    fun fromLevel(level: Level): DocumentProcessingLevel {
      return when (level) {
        Level.LIGHT -> DocumentProcessingLevel(Level.LIGHT, ExampleUpdateStrategy.NONE)
        Level.CORE -> DocumentProcessingLevel(Level.CORE, ExampleUpdateStrategy.NONE)
        Level.HEAVY -> DocumentProcessingLevel(Level.HEAVY, ExampleUpdateStrategy.NONE)
        Level.EXAMPLE -> DocumentProcessingLevel(Level.EXAMPLE, ExampleUpdateStrategy.ADD_OR_UPDATE_ALL)
      }
    }
  }
}
