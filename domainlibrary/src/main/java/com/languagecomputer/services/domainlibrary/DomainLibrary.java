package com.languagecomputer.services.domainlibrary;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * The library of concepts along with lexicons, extractors, and examples across many domains.
 *
 * Users can interact with the domain library to pull in those concepts and extractors into their current ontology
 * @author smonahan
 */
public interface DomainLibrary {

  @Operation(description = "Suggest an existing concept from a potential name")
  @POST
  @Path("conceptSearch")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  ConceptSearchResult conceptSearch(ConceptSearchRequest request);


  @Operation(description = "Copy information from domain library concept to current domain")
  @POST
  @Path("copyConcept/{domainName}/{conceptName}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  CopyConceptResponse copyConcept(@PathParam("domainName") String domainName, @PathParam("conceptName") String conceptName, CopyConceptFromDomainLibraryRequest request);

}
