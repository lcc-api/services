package com.languagecomputer.services.messages;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Results of a call to {@link com.languagecomputer.services.api.LexicalExpansionServiceInterface}, lexical items that are
 * likely co-hyponyms of a seed set.
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public class LexicalExpansionResult {

  private List<LexicalItem> lexicalItems;
  private List<Double> scores;
  private List<String> explanations;

  public LexicalExpansionResult(List<LexicalItem> lexicalItems) { // I would prefer if this didn't have to be public...
    this.lexicalItems = lexicalItems;
  }

  public void setExplanations(List<String> explanations) { this.explanations = explanations; }

  public List<String> asStrings() {
    return lexicalItems.stream().map(li -> li.word).collect(Collectors.toList());
  }

  public List<LexicalItem> asLexicalItems() {
    return lexicalItems;
  }

  public List<String> getExplanations() { return explanations; }

  public List<Double> getScores() {
    return scores;
  }

  public void setScores(List<Double> scores) {
    this.scores = scores;
  }
}
