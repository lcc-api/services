package com.languagecomputer.services.ontology

import java.util.*

/**
 * This class represents the different types of ontology constructs supported by the ATESSA system.
 *
 * @author smonahan
 */
enum class ConceptClass {
  ENTITY,  // an entity: person, place, thing, essentially an ACE entity
  PREDICATE_TRIGGER,  // an event/relation/state trigger, corresponding to ace event anchors
  ATTRIBUTE,  // an attribute of another type of concept, e.g. age, modality,
  TOPIC,  // represents a topical category associated with some text
  PREDICATE_ROLE,
  ZONE, // Represents a region of a document
  META_DATA, // Represents information about a document
  PROPERTY; // represents the property that can be attached to a concept

  // a role of a predicate (e.g. event argument, relation role)
  override fun toString(): String {
    return super.toString().uppercase(Locale.ENGLISH)
  }
}
