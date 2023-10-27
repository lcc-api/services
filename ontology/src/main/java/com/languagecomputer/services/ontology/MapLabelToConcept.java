package com.languagecomputer.services.ontology;

/**
 * Identify the LCC concept name given a label.  This is a case-insensitive matching against the record label field.
 * @author smonahan
 */
public class MapLabelToConcept {

  private MapLabelToConcept() {}

  /**
   * @param ontology - The ontology to check against
   * @param cc - The concept class
   * @param name - The name of a potentially matching ontology concept
   * @param label - The label we are checking for
   * @return The SimpleOntologyRecord from the ontology for that name, if that name matches the label
   */
  public static SimpleOntologyRecord check(Ontology ontology, ConceptClass cc, String name, String label) {
    SimpleOntologyRecord concept = ontology.getRecord(cc, name);
    // Assume the casing is flexible
    if(concept.getLabel() != null && concept.getLabel().equalsIgnoreCase(label)) {
      return concept;
    }
    return null;
  }
}
