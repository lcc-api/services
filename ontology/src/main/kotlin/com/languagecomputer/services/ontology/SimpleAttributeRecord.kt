package com.languagecomputer.services.ontology

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Class representing the JSON data for an attribute entry in the ontology.
 */
class SimpleAttributeRecord : SimpleOntologyRecord {
  @get:JsonProperty("isValue")
  var isValue = false; private set
  @get:JsonProperty("hasValue")
  var hasValue = false; private set
  var attributeOf: Set<AttributeOf>? = null; private set
  var valueType: AttributeValueType? = null; private set

  constructor(name: String, parent: ConceptIdentifier?, definition: String?, isValue: Boolean,
              hasValue: Boolean, label: String?, attributeOf: Set<AttributeOf>?, valueType: AttributeValueType?,
              children: Collection<ConceptIdentifier>?,
              map: Map<String, Any> = HashMap()) : super(ConceptClass.ATTRIBUTE, name, parent, definition, label, children, map) {
    this.isValue = isValue
    this.hasValue = hasValue
    this.attributeOf = attributeOf
    this.valueType = valueType
  }

  private constructor() : super(ConceptClass.ATTRIBUTE, "attributes")

  /**
   * The extractable concept the attribute is relative to
   */
  enum class AttributeOf {
    DOCUMENT, ENTITY, STATE, ATTRIBUTE, RELATION, PREDICATE, PREDICATE_ROLE, DISCOURSE_UNIT
  }

  /**
   * The type of value produced by the Attribute.
   */
  enum class AttributeValueType {
    ENUM, ENUMSET, BOOLEAN, STRING, DOUBLE, DOUBLE_UNITS, INTEGER, TIMEX
  }

  companion object {
    fun getAttributeRepresentation(attrName: String, valueType: AttributeValueType?, value: Any): String {
      val prefix: String
      prefix = when(valueType) {
        AttributeValueType.BOOLEAN -> "b"
        AttributeValueType.INTEGER, AttributeValueType.DOUBLE -> "n"
        AttributeValueType.DOUBLE_UNITS -> "nu"
        AttributeValueType.STRING -> "s"
        AttributeValueType.ENUM -> "e"
        AttributeValueType.TIMEX -> "t"
        AttributeValueType.ENUMSET -> "es"
        else -> {
          System.err.println("Invalid attribute type for attribute example: $attrName")
          throw IllegalArgumentException("Invalid attribute type")
        }
      }
      return "$prefix:$value"
    }
  }
}

class SimpleAttributeRecordList (
    var list: List<SimpleAttributeRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
