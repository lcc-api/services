package com.languagecomputer.services.ontology;

import java.util.Collection;
import java.util.Locale;

/**
 * Utility to convert from human-readable "labels" to the correct ontology name
 */
public class OntologyLabelToConcept {

  private OntologyLabelToConcept() {}

  /**
   * @param ontology - The ontology to check against
   * @param cc - The ConceptClass
   * @param label - The label we are trying to map
   * @return - The SimpleOntologyRecord with the associated label, or null if none found.
   */
  public static SimpleOntologyRecord getRecordForLabel(Ontology ontology, ConceptClass cc, String label) {
    SimpleOntologyRecord sor = null;
    Collection<String> names = ontology.getNamesForConceptClass(cc);
    // First pass just check the things with the same label as name, or a substring thereof (e.g. missile_weapon to Missile)
    for(String name : names) {
      if(label.equalsIgnoreCase(name)) {
        // This is the exact name match, so assume this is correct.
        return ontology.getRecord(cc, name);
      }

      if(label.toLowerCase(Locale.ENGLISH).startsWith(name.toLowerCase(Locale.ENGLISH)) ||
                  name.toLowerCase(Locale.ENGLISH).startsWith(label.toLowerCase(Locale.ENGLISH))) {
        sor = check(ontology, cc, name, label);
        if(sor != null) {
          return sor;
        }
      }
    }

    // Second pass brute force check everything (will be slow)
    for(String name : names) {
      sor = check(ontology, cc, name, label);
      if(sor != null) {
        return sor;
      }
    }
    return null;
  }

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
