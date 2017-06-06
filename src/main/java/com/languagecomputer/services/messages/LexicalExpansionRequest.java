package com.languagecomputer.services.messages;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A request object for {@link com.languagecomputer.services.api.LexicalExpansionServiceInterface}
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public class LexicalExpansionRequest {

  public enum ExpansionMethod {
    MIXTURE,
    WORDNET_SYNONYMS,
    WORDNET_ANCESTORS,
    WORDNET_SISTER_TERMS,
    WORDNET_DESCENDANTS,
    WORDNET_RELATED,
    SHARED_CONTEXTS,
    SIMILARITY_WORD2VEC,
    SIMILARITY_DEPENDENCY,
    SIMILARITY_LSA
  }

  private ExpansionMethod method = ExpansionMethod.MIXTURE;
  private List<LexicalItem> seeds;
  private List<Double> weights;
  private List<LexicalItem> negatives;
  private String lang = "ENGLISH";
  private int resultLimit = 1000;

  public LexicalExpansionRequest(List<LexicalItem> seeds) {
    this.seeds = seeds;
  }

  public LexicalExpansionRequest(ExpansionMethod method,
                                 Iterable<LexicalItem> seeds,
                                 Iterable<LexicalItem> negatives) {
    this.method = method;
    this.seeds = Lists.newArrayList(seeds);
    this.negatives = Lists.newArrayList(negatives);
  }

  public LexicalExpansionRequest method(ExpansionMethod method) {
    this.method = method;
    return this;
  }

  public LexicalExpansionRequest weights(List<Double> weights) {
    this.weights = weights;
    return this;
  }

  public LexicalExpansionRequest negatives(List<LexicalItem> negatives) {
    this.negatives = negatives;
    return this;
  }

  public LexicalExpansionRequest limit(int lim) {
    this.resultLimit = lim;
    return this;
  }

  public void language(String lang) {
    this.lang = lang;
  }

  public ExpansionMethod getMethod() {
    return method;
  }

  public Iterable<LexicalItem> getSeeds() {
    return seeds;
  }

  public Iterable<LexicalItem> getNegatives() {
    return negatives;
  }

  public List<Double> getWeights() {
    return weights;
  }

  public String getLang() {
    return lang;
  }

  public int getResultLimit() {
    return resultLimit;
  }
}
