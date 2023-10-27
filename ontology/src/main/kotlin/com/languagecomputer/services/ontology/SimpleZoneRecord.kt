package com.languagecomputer.services.ontology

/**
 * Class representing the JSON data for a zone record
 */
class SimpleZoneRecord : SimpleOntologyRecord {
  constructor(name: String, parent: ConceptIdentifier?,
              definition: String?, label: String?,
              children: Collection<ConceptIdentifier>?,
              map: Map<String, Any>) : super(ConceptClass.ZONE, name, parent, definition, label, children, map)

  private constructor() : super(ConceptClass.ZONE, "zones")
}

class SimpleZoneRecordList (
    var list: List<SimpleZoneRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
