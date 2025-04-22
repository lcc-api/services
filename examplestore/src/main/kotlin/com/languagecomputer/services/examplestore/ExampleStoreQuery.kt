package com.languagecomputer.services.examplestore

import com.google.common.base.MoreObjects
import javax.annotation.Nonnegative

/**
 * Query used to request examples from an example store service
 *
 * @author wayne
 */
class ExampleStoreQuery @JvmOverloads constructor(
    var concept: String = "",
    var recurseConcept: Boolean = false,
    var lemmatize: Boolean = true,
    var searchTerms: Set<String> = HashSet(),
    // if want to search within specific example IDs
    var exampleIds: Set<String> = HashSet(),
    var subExample1: String? = null,
    var subExample2: String? = null,
    var attributeValues: Set<String> = HashSet(),
    var extractors: Set<String> = HashSet(),
    var humanCall: Set<ExampleCall> = HashSet(),
    var documentIDs: Set<String> = HashSet(),
    var sentenceWords: Set<String> = HashSet(),
    var triggerWords: Set<String> = HashSet(),
    var pos: String? = null,
    var properties: MutableMap<String, String> = HashMap(),
    var missingProperties: Set<String> = HashSet(),
    var sortMethod: SortMethods = SortMethods.RANDOM,
    var sentenceNumbers: Set<Int> = HashSet(),
    @Nonnegative var resultLimit: Int = 10000,
    var exactSearch: Boolean = false,
    var stem: Boolean = false) {

  enum class SortMethods(var sortMethod: Comparator<in Example>) {
    DOCUMENT_SORT(Comparator<Example> { a: Example, b: Example -> sortByDocument(a, b) }),
    CONCEPT_SORT(Comparator.comparing<Example, String> { it.concept ?: "zzz" }.thenComparing { a: Example, b: Example -> sortByTrigger(a, b) }),
    TRIGGER_SORT(Comparator<Example> { a: Example, b: Example -> sortByTrigger(a, b) }),
    HUMAN_KEY(Comparator<Example> { a: Example, b: Example -> sortByHumanKey(a, b) }),
    EXT_CONF(Comparator<Example> { a: Example, b: Example ->
      -a.getProperties().getOrDefault(":conf", "1")!!.toDouble().compareTo(b.getProperties().getOrDefault(":conf", "1")!!.toDouble())
    }),
    RANDOM(Comparator<Example> { _: Example, _: Example -> 1});
  }

  fun sortMethod(): Comparator<in Example>? {
    return sortMethod.sortMethod
  }


  fun unlimited() : ExampleStoreQuery {
    resultLimit = Int.MAX_VALUE
    return this
  }


  fun addProperty(propertyName: String, propertyValue: String): ExampleStoreQuery {
    properties[propertyName] = propertyValue
    missingProperties -= propertyName
    return this
  }


  fun noProperty(property: String) : ExampleStoreQuery {
    missingProperties += property
    properties.minusAssign(property)
    return this
  }

  fun noTrigger() : ExampleStoreQuery {
    missingProperties += Example.TRIGGER
    triggerWords = HashSet();
    return this
  }

  fun concept(concept:String) : ExampleStoreQuery {
    this.concept = concept
    return this
  }

  fun concept(concept: String, recurseConcept: Boolean) : ExampleStoreQuery {
    this.concept = concept
    this.recurseConcept = recurseConcept;
    return this
  }

  fun addConcept(concept: String): ExampleStoreQuery {
    this.concept = this.concept + ", " + concept
    return this
  }

  fun humanCall(call: ExampleCall) : ExampleStoreQuery {
    this.humanCall += call
    return this
  }

  fun docid(docId: String?) : ExampleStoreQuery {
    if (docId == null) {
      this.documentIDs += Example.NULL_STRING_FIELD
    } else {
      this.documentIDs += docId
    }
    return this
  }

  fun docids(docids: Collection<String?>): ExampleStoreQuery? {
    docids.forEach{ docid(it) }
    return this
  }

    fun exampleId(exampleId: String?) : ExampleStoreQuery {
        if (exampleId == null) {
            this.exampleIds += Example.NULL_STRING_FIELD
        } else {
            this.exampleIds += exampleId
        }
        return this
    }

    fun exampleIds(exampleIds: Collection<String?>): ExampleStoreQuery? {
        exampleIds.forEach{ exampleId(it) }
        return this
    }

  fun extractor(extractor: String) : ExampleStoreQuery {
    extractors += extractor
    return this
  }

  fun sortMethod(comp: SortMethods): ExampleStoreQuery {
    sortMethod = comp
    return this
  }

  fun addTriggerWords(triggerWords: String): ExampleStoreQuery {
    this.triggerWords += triggerWords.lowercase()
    missingProperties -= (Example.TRIGGER)
    return this
  }

  fun addSentenceWords(sentenceWords: String): ExampleStoreQuery {
    this.sentenceWords += sentenceWords.lowercase()
    return this
  }

  fun addExtractors(extractors: Collection<String>) : ExampleStoreQuery {
    this.extractors += extractors
    return this
  }


  fun addSentenceNumber(number: Int): ExampleStoreQuery {
    sentenceNumbers += number
    return this
  }

  fun subExample1(subExample1: String?): ExampleStoreQuery {
    this.subExample1 = subExample1
    return this
  }

  fun subExample2(subExample2: String?): ExampleStoreQuery {
    this.subExample2 = subExample2
    return this
  }

  fun resultLimit(resultLimit: Int): ExampleStoreQuery {
    this.resultLimit = resultLimit
    return this
  }

  fun unlemmatized(): ExampleStoreQuery {
    this.lemmatize = false
    return this
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(this)
            .add("concept", concept)
            .add("recurseConcept", recurseConcept)
            .add("lemmatize", lemmatize)
            .add("pos", pos)
            .add("searchTerms", searchTerms)
            .add("exampleIds", exampleIds)
            .add("resultLimit", resultLimit)
            .add("extractors", extractors)
            .add("humanCall", humanCall)
            .add("documentIDs", documentIDs)
            .add("sentenceNumbers", sentenceNumbers)
            .add("sentence words", sentenceWords)
            .add("trigger words", triggerWords)
            .add("properties", properties)
            .add("missingProperties", missingProperties)
            .add("sortMethod", sortMethod)
            .add("subExample1", subExample1)
            .add("subExample2", subExample2)
            .add("attributeValues", attributeValues)
            .add("exactSearch", exactSearch)
            .add("stem", stem)
            .toString()
  }

  companion object {
    private const val serialVersionUID = -305856142817757013L
  }

}

// these methods had to be moved here for the bump to kotlin 1.9.20
private fun sortByHumanKey(a: Example, b: Example): Int {
  if (a.humanCall != b.humanCall) {
    return a.humanCall!!.compareTo(b.humanCall!!)
  } else {
    return a.tokenLength - b.tokenLength
  }
}

private fun sortByTrigger(a: Example, b: Example): Int {
  val sb1 = StringBuilder()
  val sb2 = StringBuilder()
  sb1.append(if (a.trigger.isNotBlank()) a.trigger.lowercase() else "zzz").append(a.tokenLength)
  sb2.append(if (b.trigger.isNotBlank()) b.trigger.lowercase() else "zzz").append(b.tokenLength)
  return sb1.toString().compareTo(sb2.toString())
}

private fun sortByDocument(a: Example, b: Example): Int {
  if (a.documentID != null && b.documentID != null) {
    val cmp = a.documentID.compareTo(b.documentID)
    if (cmp != 0) {
      return cmp
    }
    return sortWithinDocument(a, b);
  } else if (a.documentID != null) {
    return -1
  } else if (b.documentID != null) {
    return 1
  } else {
    var cmp = a.concept!!.compareTo(b.concept!!)
    if (cmp == 0) {
      cmp = a.iD!!.compareTo(b.iD!!)
    }
    return cmp
  }
}

private fun sortWithinDocument(a: Example, b: Example): Int {
  var cmp = a.sentenceNumber.compareTo(b.sentenceNumber)
  if (cmp != 0) {
    return cmp
  }
  val aStart = if (a.startToken < 0) a.startToken2 else a.startToken
  val bStart = if (b.startToken < 0) b.startToken2 else b.startToken
  cmp = Integer.compare(aStart, bStart)
  if (cmp != 0) {
    return cmp
  }
  val aLength = if (a.tokenLength <= 0) a.tokenLength2 else a.tokenLength
  val bLength = if (b.tokenLength <= 0) b.tokenLength2 else b.tokenLength
  return aLength - bLength
}