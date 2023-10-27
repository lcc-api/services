package com.languagecomputer.services.examplestore

import com.google.common.base.MoreObjects
import com.languagecomputer.services.ontology.ConceptClass


/**
 * This class represents the data an external system has with which they are creating an example object.
 *
 * Notably, the example object operates off of LCC-internal token and sentence offsets, which are not available to third parties.
 *
 * This class enables the creation of example objects using directly character offsets.
 * @author smonahan
 */
class ExternalExample @JvmOverloads constructor(
    val docid: String,
    val startChar: Int,
    val charLength: Int,
    val conceptClass: ConceptClass,
        // The concept of the example.  For PREDICATE_ROLE examples this is the predicate class.
    val concept : String,
    val properties: Map<String, String>,
    var humanCall: ExampleCall,

        // Fields used by PREDICATE role examples
        // The start character of the role text
    val startChar2 : Int? = null,
        // The character length of the role text
    val charLength2 : Int? = null,
        // The id of the predicate example
    val predID : String? = null,
        // If the role is already modelled with an example, pass that in here
    val roleID : String? = null,
        // For example in an Attack event, this would be "Attacker"
    val roleType : String? = null ) {

  override fun toString(): String {
    return MoreObjects.toStringHelper(ExternalExample::class.java)
            .add("docid", docid)
            .add("start", startChar)
            .add("length", charLength)
            .add("cc", conceptClass)
            .add("concept", concept)
            .add("humanCall", humanCall)
            .add("properties", properties)
            .toString()
  }

}
