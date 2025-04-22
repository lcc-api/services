package com.languagecomputer.services.docprocess

import com.languagecomputer.services.examplestore.ExampleStoreQuery
import com.languagecomputer.services.ontology.ConceptIdentifier

/**
 * Message used to communicate with model service how to train a new machine learning model.
 * Also used in feeding information to the document processor to limit or expand which annotation types (e.g., entities,
 * coreference, full inference) should be run.
 *
 * @author Marc
 */
class ModelMessage @JvmOverloads constructor(var query: ExampleStoreQuery? = null,
                                             val concept: ConceptIdentifier? = null,
                                             val negativeConcept: String? = null,
                                             val update: Boolean = false,
                                             val extractorID: String? = null,
                                             var exampleIDs: MutableSet<String> = HashSet(),
                                             val annotationTypes: MutableSet<String> = HashSet(),
                                             val echo: Boolean = false) {

  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other == null || javaClass != other.javaClass) return false
    val that = other as ModelMessage
    if(if(query != null) query != that.query else that.query != null) return false
    if(if(concept != null) concept != that.concept else that.concept != null) return false
    return that.negativeConcept == negativeConcept
  }

  fun addAnnotationsToRun(annotationTypes: Iterable<String>): ModelMessage {
    for(type in annotationTypes) {
      addAnnotationsToRun(type)
    }
    return this
  }

  fun addAnnotationsToRun(annotationType: String): ModelMessage {
    annotationTypes += annotationType
    return this
  }

  fun withExampleIDs(exampleIDs: MutableSet<String>) : ModelMessage {
    this.exampleIDs = exampleIDs
    return this
  }

//  fun copy() : ModelMessage {
//    return fromJson(toJson(this), ModelMessage::class.java)!!
//  }

  override fun hashCode(): Int {
    var result = if(query != null) query.hashCode() else 0
    result = 31 * result + (concept?.hashCode() ?: 0)
    result = 31 * result + (negativeConcept?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "ModelMessage{" +
            "query=" + (if(query == null) "null" else query.toString()) +
            "; concept='" + concept + '\'' +
            "; negativeConcept='" + negativeConcept + '\'' +
            "; update=" + update +
            "; extractorID='" + extractorID + '\'' +
            "; exampleIds=" + exampleIDs.toString() +
            "; annotationTypes=" + annotationTypes.toString() +
            "; echo=" + echo +
            '}'
  }
}
