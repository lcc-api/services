package com.languagecomputer.services.ontology

/**
 * Class representing the JSON data for a zone record
 */
class SimplePropertyRecord : SimpleOntologyRecord {
  constructor(name: String, parent: ConceptIdentifier?,
              definition: String?, label: String?,
              children: Collection<ConceptIdentifier>?,
              map: Map<String, Any>) : super(ConceptClass.PROPERTY, name, parent, definition, label, children, map)

  private constructor() : super(ConceptClass.PROPERTY, "properties")
}

class SimplePropertyRecordList (
    var list: List<SimplePropertyRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
