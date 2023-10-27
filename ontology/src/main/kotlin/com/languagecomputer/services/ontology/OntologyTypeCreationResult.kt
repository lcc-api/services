package com.languagecomputer.services.ontology

/**
 * This contains the result of adding a new type to the ontology.
 * @author smonahan
 *
 * @return true if there was an error creating a type.
 * Errors might be caused by duplicate names, invalid parents, etc.
 */
class OntologyTypeCreationResult(
    val isError: Boolean,
    val message: String? = "",
    val ontRecord: SimpleOntologyRecord? = null) {

  override fun toString(): String {
    return (if(isError) "error: " else "") + message + ontRecord
  }

}
