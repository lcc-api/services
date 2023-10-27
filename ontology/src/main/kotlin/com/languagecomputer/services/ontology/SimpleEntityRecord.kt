package com.languagecomputer.services.ontology


/**
 * Class representing the JSON data for an entity entry in the ontology.
 */
class SimpleEntityRecord : SimpleOntologyRecord {
  var model: String? = null; private set
  var corefGroup: String? = null; private set
  var corefSpecificType: String? = null; private set
  var keywords: Collection<String>? = null; private set
  var mappedTypes: Collection<String>? = null; private set
  var isActive = false; private set
  var isCorefActive = false; private set
  var isCommon = false; private set

  constructor(name: String, parent: ConceptIdentifier?, definition: String?,
              label: String?, children: Collection<ConceptIdentifier>?,
              model: String?, corefGroup: String?, corefSpecificType: String?,
              keywords: Collection<String>?, mappedTypes: Collection<String>?,
              active: Boolean, corefActive: Boolean, common: Boolean)
          : super(ConceptClass.ENTITY, name, parent, definition, label, children, HashMap<String, Any?>()) {
    this.model = model
    this.corefGroup = corefGroup
    this.corefSpecificType = corefSpecificType
    this.keywords = keywords
    this.mappedTypes = mappedTypes
    isActive = active
    isCorefActive = corefActive
    isCommon = common
  }

  constructor(name: String, parent: ConceptIdentifier?, definition: String?,
              label: String?, children: Collection<ConceptIdentifier>?, map: Map<String, Any?>)
          : super(ConceptClass.ENTITY, name, parent, definition, label, children, map)


  private constructor() : super(ConceptClass.ENTITY, "entities")
}

class SimpleEntityRecordList (
    var list: List<SimpleEntityRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
