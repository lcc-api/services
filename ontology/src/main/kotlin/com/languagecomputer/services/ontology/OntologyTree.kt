package com.languagecomputer.services.ontology

/**
 * Structures ontology information as a tree
 *
 * @param record - ontology record
 * @param children - list of children
 */
class OntologyTree(val record: SimpleOntologyRecord, val children: List<OntologyTree>) {
  /**
   * the label as a String
   */
  val label: String?

  /**
   * the name (id) as a String
   */
  val name: String?

  /**
   * @return the number of descendants as an int
   */
  var numberOfDescendants = 0

  init {
    label = record.label
    name = record.name
    for(child in children) {
      numberOfDescendants += child.numberOfDescendants + 1
    }
  }
}
