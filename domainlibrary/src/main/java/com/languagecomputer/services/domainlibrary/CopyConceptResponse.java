package com.languagecomputer.services.domainlibrary;

/**
 * The response for copying a concept from the domain library to the current ontology.
 * @author smonahan
 */
public class CopyConceptResponse {
  private Integer numExamples;
  private Integer numExtractors;
  private Integer numLexicals;

  public CopyConceptResponse(Integer numExamples, Integer numExtractors, Integer numLexicals) {
    this.numExamples = numExamples;
    this.numExtractors = numExtractors;
    this.numLexicals = numLexicals;
  }

  public Integer getNumExamples() {
    return numExamples;
  }

  public Integer getNumExtractors() {
    return numExtractors;
  }

  public Integer getNumLexicals() { return numLexicals; }
}
