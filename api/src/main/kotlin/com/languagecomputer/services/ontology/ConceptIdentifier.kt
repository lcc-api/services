package com.languagecomputer.services.ontology

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.MoreObjects

/**
 * This class represents an ontology concept, containing the name and the type of concept.
 *
 * It is very important that label is not part of equality or hashcode, since sometimes components fake or change the label, and it shouldn't be problematic.
 */
data class ConceptIdentifier(val name: String, val label: String?, val ontClass: ConceptClass) {

  init {
    require(name.isNotEmpty()) { "Invalid name for concept identifier." }
    require(label == null || label.isNotEmpty()) { "Invalid label for concept identifier." }
  }

  @JsonIgnore
  fun getConceptType() : ConceptClass {
    return ontClass
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(ConceptIdentifier::class.java)
            .add("name", name)
            .add("type", ontClass)
            .add("label", label)
            .toString()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ConceptIdentifier
    return name == other.name && ontClass == other.ontClass
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + ontClass.hashCode()
    return result
  }
}

class ConceptIdentifierList(var list: List<ConceptIdentifier>)
