package com.languagecomputer.services.api;

import com.languagecomputer.services.messages.LexicalExpansionRequest;
import com.languagecomputer.services.messages.LexicalExpansionResult;

/**
 * Interface for expanding lexical sets.
 * Given a set of lexical items: w1, w2, ..., wn; Return an ordered set of lexical items most likely to belong to the
 * same set.  For example, given "apple", "orange", "strawberry", return something like "banana", "grapes", "blueberry",
 * "kiwi".
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public interface LexicalExpansionServiceInterface {
  LexicalExpansionResult expand(LexicalExpansionRequest request);
}
