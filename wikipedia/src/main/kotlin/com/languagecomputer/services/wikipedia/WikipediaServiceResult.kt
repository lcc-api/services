package com.languagecomputer.wikipedia

/**
 * Message class for a list of results from Wikipedia.
 */
class WikipediaServiceResult (val pageResults: List<WikipediaServicePageResult>)

/**
 * Class to bundle Wikipedia search results
 */
class WikipediaServicePageResult (val page: String, val score: Double, val aliasPageResults: List<String>, val types: Map<String, Double>,
                                  val scResults: List<String>, val listResults: List<String>, val linkResults: List<String>,
                                  val surfaceLinkResults: List<String>, val redirectResults: List<String>, val categoryResults: List<String>,
        // Top Surface forms for Link
                                  val aliases: List<String>) : Comparable<WikipediaServicePageResult> {

  override operator fun compareTo(other: WikipediaServicePageResult): Int {
    return other.score.compareTo(score)
  }

  /**
   * builder class for WikipediaPageResults
   */
  class WikipediaServicePageResultBuilder(val page: String) {
    private var score = 0.0
    private val aliasPageResults: MutableList<String> = ArrayList()
    private val types: MutableMap<String, Double> = HashMap()
    private val scResults: MutableList<String> = ArrayList()
    private val listResults: MutableList<String> = ArrayList()
    private val linkResults: MutableList<String> = ArrayList()
    private val surfaceLinkResults: MutableList<String> = ArrayList()
    private val redirectResults: MutableList<String> = ArrayList()
    private val categoryResults: List<String> = ArrayList()
    private val aliases: MutableList<String> = ArrayList() // Top Surface forms for Link

    fun aliases(aliases: Collection<String>): WikipediaServicePageResultBuilder {
      this.aliases.addAll(aliases)
      return this
    }

    fun aliasPageResults(aliasPageResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.aliasPageResults.addAll(aliasPageResults)
      return this
    }

    fun scResults(scResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.scResults.addAll(scResults)
      return this
    }

    fun listResults(listResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.listResults.addAll(listResults)
      return this
    }

    fun linkResults(linkResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.linkResults.addAll(linkResults)
      return this
    }

    fun surfaceLinkResults(surfaceLinkResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.surfaceLinkResults.addAll(surfaceLinkResults) // Have some numbers here.
      return this
    }

    fun redirectResults(redirectResults: Collection<String>): WikipediaServicePageResultBuilder {
      this.redirectResults.addAll(redirectResults)
      return this
    }

    fun types(types: Map<String, Double>?): WikipediaServicePageResultBuilder {
      this.types.putAll(types!!)
      return this
    }

    fun score(score: Double): WikipediaServicePageResultBuilder {
      this.score = score
      return this
    }

    /**
     * Use this in conjunction with the setters above to customize which Wikipedia results you want returned.
     * @return WikipediaServicePageResult
     */
    fun build(): WikipediaServicePageResult {
      return WikipediaServicePageResult(page, score, aliasPageResults, types, scResults, listResults, linkResults,
                                        surfaceLinkResults, redirectResults, categoryResults, aliases)
    }
  } //end builder
}
