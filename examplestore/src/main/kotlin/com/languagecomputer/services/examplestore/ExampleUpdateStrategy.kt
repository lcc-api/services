package com.languagecomputer.services.examplestore

/**
 * The strategy by which to update the example store.
 *
 * ADD: Adds new examples to the example store
 * UPDATE: Updates existing examples
 * PROBE: doesn't actually perform the change, just determines what change would be made
 * The probe will actually query the example store.  In the case of UpdateProbe, it will merge the local example with the preserved version and return it to you.
 *
 * @author smonahan
 */
enum class ExampleUpdateStrategy( val add: Boolean, val update: Boolean, val probe: Boolean) {
  ADD_OR_UPDATE_ALL(true, true, false),  // Add or Update the examples as necessary
  UPDATE_ONLY(false, true, false),  // Don't add new examples, only update existing ones
  ADD_ONLY(true, false, false),  // Don't update existing examples, only add new ones
  PROBE_ONLY(false, false, true),  // Don't add or update, just return the example that would be sent to the example store
  UPDATE_PROBE(false, true, true),  // If an update occurs, instead just return the example
  ADD_PROBE(true, false, true),  // If an add would occur, instead just return the example
  ADD_UPDATE_PROBE(true, true, true),  // If an add or update would occur, return the example
  NONE(false, false, false); // Don't touch the example store at all
}
