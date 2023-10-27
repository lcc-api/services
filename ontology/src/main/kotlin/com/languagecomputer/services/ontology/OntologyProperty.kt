package com.languagecomputer.services.ontology

import com.google.common.base.MoreObjects
import com.google.common.base.Objects

/**
 * A general property of an ontology concept, not a specific one, describing the metadata of the property.
 */
class OntologyProperty(val propertyType: PropertyClass, val name: String?, val label: String?, val value: Any?, val definition: String?) {
  override fun toString(): String {
    return MoreObjects.toStringHelper(OntologyProperty::class.java)
            .add("name", name)
            .add("type", propertyType)
            .add("label", label)
            .add("definition", definition)
            .toString()
  }

  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(other !is OntologyProperty) return false
    return Objects.equal(name, other.name) &&
            propertyType == other.propertyType &&
            Objects.equal(label, other.label) &&
            Objects.equal(definition, other.definition)
  }

  override fun hashCode(): Int {
    return Objects.hashCode(name, propertyType, label, definition)
  }
}

class OntologyPropertyList(val list: MutableList<OntologyProperty>)
