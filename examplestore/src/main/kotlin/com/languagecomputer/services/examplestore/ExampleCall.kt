package com.languagecomputer.services.examplestore

/**
 * Enumeration of possible system/human calls.  Note that UNKEYED should only be used for human calls.
 */
enum class ExampleCall {
  POSITIVE, 
  NEGATIVE, 
  PUNT, 
  UNKEYED,
}