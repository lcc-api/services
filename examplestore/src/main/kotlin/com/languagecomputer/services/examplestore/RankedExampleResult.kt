package com.languagecomputer.services.examplestore

import java.util.*

/**
 * Results of a call to [LexicalExpansionService], lexical items that are
 * likely co-hyponyms of a seed set.
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
class RankedExampleResult @JvmOverloads constructor(
    var examplesList: MutableList<Example> = ArrayList(),
    var scores: MutableList<Double> = ArrayList(),
    var explanations: MutableList<String> = ArrayList()) {

  fun addExample(example: Example, score: Double, explanation: String) {
    examplesList.add(example)
    scores.add(score)
    explanations.add(explanation)
  }

  fun asExamples(): List<Example> {
    return Collections.unmodifiableList(examplesList)
  }
}
