package com.languagecomputer.services.ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A value of a property of an ontology concept.
 *
 * @author smonahan
 */
public class OntPropertyValue {
  @JsonProperty
  private String result;
  @JsonCreator
  public OntPropertyValue(@JsonProperty("result")String results) {
    this.result = results;
  }
  public String getResults() {
    return result;
  }
  public void setResults(String results) {
    this.result = results;
  }

  public int hashCode() {
    return result.hashCode();
  }
  public boolean equals(Object that) {
    if(that instanceof OntPropertyValue thatValue) {
      return thatValue.result.equals(result);
    }
    return false;
  }
}
