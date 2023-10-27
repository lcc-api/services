package com.languagecomputer.services.ontology

/**
 * Class representing the JSON data for a topic record
 */
class SimpleTopicRecord : SimpleOntologyRecord {
  constructor(name: String, parent: ConceptIdentifier?,
              definition: String?, label: String?,
              children: Collection<ConceptIdentifier>?,
              map: Map<String, Any>) : super(ConceptClass.TOPIC, name, parent, definition, label, children, map)

  private constructor() : super(ConceptClass.TOPIC, "topics")
}

class SimpleTopicRecordList (
    var list: List<SimpleTopicRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
