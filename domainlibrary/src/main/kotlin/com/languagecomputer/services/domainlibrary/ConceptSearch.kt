package com.languagecomputer.services.domainlibrary;

import com.languagecomputer.services.examplestore.Example
import com.languagecomputer.services.ontology.ConceptClass
import com.languagecomputer.services.ontology.ConceptIdentifier

/**
 * A fuzzy search request for a concept in the domain library.
 *
 * The user can search for concept name, restrict the concept class if desired, provide an example object to search for similar examples,
 * and restrict the query to only concepts that are in the current ontology.
 * @author smonahan
 */
data class ConceptSearchRequest @JvmOverloads constructor(
    val name: String,
    val conceptClass: ConceptClass? = null, // null if user doesn't care or didn't say
    val example: Example? = null, // null if we aren't using an example
    val internalOnly: Boolean = false,
    val vector: DoubleArray? = null, // null if we aren't using a double vector
)

data class ConceptSearchResult (
    val matches: List<ConceptMatch>,
    val cc: ConceptClass? = null, // null if multiple things
    val parents: List<ConceptMatch>,
    val domains: List<String>,
)

data class ConceptMatch (
    val ci: ConceptIdentifier,
    val score : Double,
    val external : Boolean,
    val domain : String? = null, // null if no domain
)

