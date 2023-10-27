package com.languagecomputer.services.ontology

/**
 * For requesting the creation of new Ontology Types.
 *
 * When creating an object whose parent is an attribute, use the full constructor.
 * Otherwise use the name-only constructor.
 *
 */
class OntologyTypeCreationRequest (
    val name: String,
    val attrValueType: SimpleAttributeRecord.AttributeValueType?,
    val attributeOf: Set<SimpleAttributeRecord.AttributeOf>?,
    private val isValue: Boolean?,
    private val hasValue: Boolean?
) {
  fun isValue(): Boolean {
    return isValue ?: false
  }

  fun hasValue(): Boolean {
    return hasValue ?: false
  }
}

class OntologyTypeCreationRequestBuilder(val name: String) {
  var attrValueType: SimpleAttributeRecord.AttributeValueType? = null
  var attributeOf: Set<SimpleAttributeRecord.AttributeOf>? = null
  var isValue: Boolean? = null
  var hasValue: Boolean? = null
  fun value(attrValueType: SimpleAttributeRecord.AttributeValueType?, attributeOf: Set<SimpleAttributeRecord.AttributeOf>?, isValue: Boolean?, hasValue: Boolean?): OntologyTypeCreationRequestBuilder {
    this.attrValueType = attrValueType
    this.attributeOf = attributeOf
    this.isValue = isValue
    this.hasValue = hasValue
    return this
  }

  fun build(): OntologyTypeCreationRequest {
    return OntologyTypeCreationRequest(name, attrValueType, attributeOf, isValue, hasValue)
  }
}
