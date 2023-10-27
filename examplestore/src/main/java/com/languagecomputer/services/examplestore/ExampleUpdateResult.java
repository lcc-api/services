package com.languagecomputer.services.examplestore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * This is meant to reflect the fact that changes to an example can have downstream effects including:
 * (1) Change of type or span will change the derived ID.
 * (2) Change of trigger span will break the link to the associated roles
 * (3) Change of derived ID will break the link to downstream attribute examples
 *
 * This enables us to return ALL of the changed examples as a result of some update action.
 */
public class ExampleUpdateResult {
  private final String error;
  @JsonProperty
  private final Map<String, Example> updates;

  private ExampleUpdateResult(String error, Map<String, Example> updates) {
    this.error = error;
    this.updates = updates;
  }

  public String getError() {
    return error;
  }

  @JsonIgnore
  public Collection<String> getChangedExampleIDs() {
    return updates.keySet();
  }

  public Example getUpdatedExampleForID(String id) {
    return updates.get(id);
  }

  public static ExampleUpdateResult error(String error) {
    return new ExampleUpdateResult(error, Maps.newHashMap());
  }

  public static ExampleUpdateResult results(Map<String, Example> updates) {
    return new ExampleUpdateResult(null, updates);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("error", error)
            .add("updates", updates)
            .toString();
  }
}
