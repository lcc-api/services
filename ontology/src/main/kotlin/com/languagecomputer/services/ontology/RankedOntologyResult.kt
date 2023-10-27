package com.languagecomputer.services.ontology

/**
 * Results of a call to [LexicalExpansionService], lexical items that are
 * likely co-hyponyms of a seed set.
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
class RankedOntologyResult(var ontologyEntryList: List<SimpleOntologyRecord>) {
  var scores: List<Double> = emptyList()
  var explanations: List<String> = emptyList()

  fun asOntologyList(): List<SimpleOntologyRecord> {
    return ontologyEntryList
  }
}
