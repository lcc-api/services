package com.languagecomputer.services.ontology;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Set;

/**
 * Service which represents LCCs customer facing ontology.
 * Notably, all information learned, extracted, and outputted by LCC will have a corresponding concept in this ontology.
 * @author smonahan
 */
public interface Ontology {

  @GET
  @Path("/ontology/root")
  @Produces(MediaType.APPLICATION_JSON)
  Collection<String> getRootConcepts();

  @GET
  @Path("/ontology/conceptClass/{conceptClass}")
  @Produces(MediaType.APPLICATION_JSON)
  Collection<String> getNamesForConceptClass(@PathParam("conceptClass") ConceptClass conceptClass);

  @GET
  @Path("/ontology/toasterStages/default")
  @Produces(MediaType.APPLICATION_JSON)
  Collection<String> getDefaultToasterStages();

  @GET
  @Path("/ontology/toasterStagesTOASTER_STAGES_ROOT/all")
  @Produces(MediaType.APPLICATION_JSON)
  Collection<String> getPossibleToasterStages();

  /**
   * Searches the ontology for any concept that contains the provided property with the provided value.
   * @param property The property attached ot the concept.
   * @param value The value of the property attached to the concept.
   * @return A collection of ConceptIdentifiers of all concepts matching the search.
   */
  @GET
  @Path("/ontology/findConcepts")
  @Produces(MediaType.APPLICATION_JSON)
  Collection<ConceptIdentifier> findConcepts(@QueryParam("property") final String property, @QueryParam("value") final String value);

  /**
   * Determine if the concept is one of, or a child of the potentials.
   * As a practical matter, the user should check if potentials.contains(concept) to avoid a web service call.
   * @param concept - The concept
   * @param potentials - The potential things to check
   * @return the set of concepts matching
   */
  @POST
  @Path("/ontology/isTypeOf")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  ConceptIdentifierList isTypeOf(@QueryParam("concept") final String concept, final Set<String> potentials);

  @GET
  @Path("/ontology/conceptClass/{conceptClass}/subtree/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  SimpleRecordList getSubtree(@PathParam("conceptClass") final ConceptClass conceptClass, @PathParam("name") final String name);

  @GET
  @Path("/ontology/conceptClass/{conceptClass}/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  SimpleOntologyRecord getRecord(@PathParam("conceptClass") final ConceptClass conceptClass, @PathParam("name") final String name);

  @GET
  @Path("/ontology/conceptClass/{conceptClass}/chain/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  SimpleRecordList getAncestorChain(@PathParam("conceptClass") final ConceptClass conceptClass, @PathParam("name") final String name);

  @POST
  @Path("/ontology/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  OntologyTypeCreationResult createType(@QueryParam("ontClass") final String ontClass,
                                        @QueryParam("parent") final String parent,
                                        final OntologyTypeCreationRequest request);

  /**
   * Deletes a concept from the ontology
   * @param conceptClass - concept class
   * @param name - concept to delete
   * @return a boolean indicating success or failure
   */
  @DELETE
  @Path("/ontology/conceptClass/{conceptClass}/{name}")
  boolean deleteConcept(@PathParam("conceptClass") String conceptClass, @PathParam("name") String name);

  @DELETE
  @Path("/ontology/unsetProperty")
  @Produces(MediaType.APPLICATION_JSON)
  OntologySetPropertyResult unsetProperty(@QueryParam("ontClass") String ontClass,
                                          @QueryParam("name") String name,
                                          @QueryParam("property") String property);

  @DELETE
  @Path("/ontology/removeOther")
  @Produces(MediaType.APPLICATION_JSON)
  OntologySetPropertyResult deleteOtherRelationship(@QueryParam("ontClass") String ontClass,
                                                    @QueryParam("parent") String parent,
                                                    @QueryParam("child") String child);

  @GET
  @Path("/ontology/predicates/roles/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  SimplePredicateRoleList getRolesForPredicate(@PathParam("name") final String name);

  @GET
  @Path("/ontology/conceptForName")
  @Produces(MediaType.APPLICATION_JSON)
  ConceptClass getConceptClassForName(@QueryParam("name") final String name);

  @GET
  @Path("/ontology/full")
  String getFullOntology();

  @POST
  @Path("/ontology/predicates/addRole")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  SimplePredicateRole addRole(@QueryParam("predicate") String predicate,
                              SimplePredicateRole role,
                              @QueryParam("recursive") final boolean recursive);

  @DELETE
  @Path("/ontology/predicates/removeRole")
  @Produces(MediaType.APPLICATION_JSON)
  boolean removeRole(@QueryParam("predicate") String predicate,
                     @QueryParam("role") final String role,
                     @QueryParam("recursive") final boolean recursive);

  @POST
  @Path("/ontology/setProperty")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  OntologySetPropertyResult setProperty(@QueryParam("ontClass") String ontClass,
                                        @QueryParam("name") String name,
                                        @QueryParam("property") String property,
                                        OntPropertyValue value,
                                        @QueryParam("append") boolean append
  );
}
