package com.languagecomputer.services.api;

public enum BooleanClauseType {
  MUST,// clause must be included for a result to be returned
  MUST_NOT,// clause must not be included for a result to be returned
  SHOULD,// clause being present will increase relevance score, but its presence in results is optional
}
