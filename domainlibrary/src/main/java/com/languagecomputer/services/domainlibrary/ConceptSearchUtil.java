package com.languagecomputer.services.domainlibrary;

import java.io.PrintStream;
import java.util.List;

/**
 * Utilities for Concept Search
 * @author smonahan
 */
public class ConceptSearchUtil {

  private ConceptSearchUtil() {}

  public static void printConceptSearchResult(ConceptSearchResult result, PrintStream printer) {
    printer.println("Suggested concept class is : " + result.getCc());
    final List<ConceptMatch> matches = result.getMatches();
    for(ConceptMatch match : matches) {
      printer.println("match: " + match.getCi().getName() + " "
                             + match.getCi().getOntClass() + " "
                             + match.getScore() + " "
                             + match.getExternal() + " "
                             + match.getDomain());
    }
    for(ConceptMatch parent : result.getParents()) {
      printer.println("parent : " + parent.getCi().getName());
    }
    for(String domain : result.getDomains()) {
      printer.println("domain : " + domain);
    }
  }
}
