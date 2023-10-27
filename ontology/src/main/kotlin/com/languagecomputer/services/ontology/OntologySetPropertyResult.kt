package com.languagecomputer.services.ontology

/**
 * The results of setting an ontology property, indicating if it succeeded.
 */
class OntologySetPropertyResult(
    val ci: ConceptIdentifier?,
    val oldValue: Any?,
    val error: Boolean,
    val message: String,
    val errorType: ErrorType?)
{
  companion object {
    fun success(ci: ConceptIdentifier, oldValue: Any?, message: String): OntologySetPropertyResult {
      return OntologySetPropertyResult(ci, oldValue, false, message, null)
    }

    fun failure(message: String, errorType: ErrorType): OntologySetPropertyResult {
      return OntologySetPropertyResult(null, null, true, message, errorType)
    }
  }

  fun isError() : Boolean {
    return error
  }

  enum class ErrorType {
    DUPLICATE_VALUE,
    INVALID_ONT_CLASS,
    INVALID_ONT_PROPERTY,
    INVALID_VALUE,
    INVALID_EXTRACTABLE,
    CHANGE_READ_ONLY_VALUE,
    NONEXISTENT_PROPERTY,
    NONEXISTENT_CHILD,
    NONEXISTENT_PARENT,
    UNSPECIFIED
  }

  override fun toString(): String {
    return String.format("OntologySetPropertyResult: message - |%s|, error - |%s|", message, error)
  }
}
