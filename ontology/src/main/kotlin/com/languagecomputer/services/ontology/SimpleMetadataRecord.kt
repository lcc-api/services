package com.languagecomputer.services.ontology

/**
 * Class representing the JSON data for a zone record
 */
class SimpleMetadataRecord : SimpleOntologyRecord {
  constructor(name: String, parent: ConceptIdentifier?,
              definition: String?, label: String?,
              children: Collection<ConceptIdentifier>?,
              map: Map<String, Any>) : super(ConceptClass.META_DATA, name, parent, definition, label, children, map)

  private constructor() : super(ConceptClass.META_DATA, "metadata")
}

class SimpleMetadataRecordList (
    var list: List<SimpleMetadataRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
