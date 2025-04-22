package com.languagecomputer.services.ontology

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * For requesting the creation of new Ontology Types.
 *
 * When creating an object whose parent is an attribute, use the full constructor.
 * Otherwise use the name-only constructor.
 *
 */
class OntologyTypeCreationRequest @JvmOverloads constructor(
  val name: String,
  val attrValueType: SimpleAttributeRecord.AttributeValueType? = null,
  val attributeOf: Set<SimpleAttributeRecord.AttributeOf>? = null,
  private val isValue: Boolean? = null,
  private val hasValue: Boolean? = null,
  val label: String? = null, // if given, additionally sets the label for the concept.
) {
  @JsonProperty("isValue")
  fun isValue(): Boolean {
    return isValue ?: false
  }

  @JsonProperty("hasValue")
  fun hasValue(): Boolean {
    return hasValue ?: false
  }
}

class OntologyTypeCreationRequestBuilder(val name: String) {
  var attrValueType: SimpleAttributeRecord.AttributeValueType? = null
  var attributeOf: Set<SimpleAttributeRecord.AttributeOf>? = null
  var isValue: Boolean? = null
  var hasValue: Boolean? = null
  var label: String? = null

  fun label(label: String?): OntologyTypeCreationRequestBuilder {
    this.label = label
    return this
  }

  fun value(attrValueType: SimpleAttributeRecord.AttributeValueType?, attributeOf: Set<SimpleAttributeRecord.AttributeOf>?, isValue: Boolean?, hasValue: Boolean?): OntologyTypeCreationRequestBuilder {
    this.attrValueType = attrValueType
    this.attributeOf = attributeOf
    this.isValue = isValue
    this.hasValue = hasValue
    return this
  }

  fun build(): OntologyTypeCreationRequest {
    return OntologyTypeCreationRequest(name, attrValueType, attributeOf, isValue, hasValue, label)
  }
}