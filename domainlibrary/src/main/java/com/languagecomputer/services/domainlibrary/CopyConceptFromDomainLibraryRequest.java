package com.languagecomputer.services.domainlibrary;

import com.languagecomputer.services.ontology.ConceptIdentifier;

/**
 * Message class telling the domain library to copy this concept into the local ontology.
 * Flags for whether to copy just the concept, or also the examples and extractors.
 * @author smonahan
 */
public class CopyConceptFromDomainLibraryRequest {
  private ConceptIdentifier concept;
  private Boolean copyExamples;
  private Boolean copyExtractors;

  public CopyConceptFromDomainLibraryRequest(ConceptIdentifier concept, Boolean copyExamples, Boolean copyExtractors) {
    this.concept = concept;
    this.copyExamples = copyExamples;
    this.copyExtractors = copyExtractors;
  }

  public ConceptIdentifier getConcept() {
    return concept;
  }

  public Boolean getCopyExamples() {
    return copyExamples;
  }

  public Boolean getCopyExtractors() {
    return copyExtractors;
  }

}
