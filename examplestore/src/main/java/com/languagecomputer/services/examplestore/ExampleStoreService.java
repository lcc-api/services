package com.languagecomputer.services.examplestore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface to interact with the ATESSA example store, which stores interesting examples of various concepts in the ontology.
 * The primary usage for this is storing human annotations as to whether the examples are correct or incorrect.
 * These annotations are used for building machine learning models.
 * @author smonahan
 */
@OpenAPIDefinition(
    info=@Info(
        title = "Example Store Service Interface",
        description = "Example Store Service Interface",
        version = "0.1"

    )
)
public interface ExampleStoreService {
  // The following paths are used by the service to create RESTful APIs for these methods

  /**
   * @param query - a query in the example store
   * @return the number of examples matching that query
   */
  @POST
  @Path("/examples/count")
  @Consumes(MediaType.APPLICATION_JSON)
  int countExamples(final ExampleStoreQuery query);

  /**
   * @param query - a query into the example store
   * @return the examples matching the query
   */
  @POST
  @Path("/examples/search")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Example> findExamples(final ExampleStoreQuery query);

  /**
   * Search the example store for the roles (e.g. event arguments) of the matching examples.
   * @param query - a query into the example store
   * @return - the matching examples of roles
   */
  @POST
  @Path("/examples/searchRoles")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Example> findExamplesForRoles(final ExampleStoreQuery query);

  @POST
  @Path("/examples/addExternal")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Example addExampleExternal(ExternalExample example);

  /**
   * Will not add the examples that are already in the example store.
   * @param examples - add the examples into the example store
   */
  @POST
  @Path("/examples/bulkadd")
  @Consumes(MediaType.APPLICATION_JSON)
  void addExamples(final Collection<Example> examples);

  /**
   * @param exampleID - the example id
   * @return the Example object matching that id or null if it is not found
   */
  @GET
  @Path("/examples/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  Example getExample(@PathParam("id") final String exampleID);

  /**
   * @param exampleID - the example ID to delete
   */
  @DELETE
  @Path("/examples/{id}")
  void deleteExample(@PathParam("id") final String exampleID);

  /**
   * Will delete all examples that are already in the example store.
   * @param exampleIDs - delete the examples from the example store
   */
  @POST
  @Path("/examples/bulkdeleteIDs")
  @Consumes(MediaType.APPLICATION_JSON)
  int deleteExamples(final Collection<String> exampleIDs);

  /**
   * Will delete all examples that match the query in the example store.
   * @param query - query
   */
  @POST
  @Path("/examples/bulkdeleteQuery")
  @Consumes(MediaType.APPLICATION_JSON)
  int deleteExamples(final ExampleStoreQuery query);

  /**
   * Puts a new example into the store, overwriting an existing example.
   * Contrary to addExamples, this will add the example if it is not already in the store.
   *
   * Some care should be taken to start with existing example object and not overwrite any existing properties.
   *
   * @param exampleID - the id of the existing example
   * @param updatedExample - the example payload
   */
  @PUT
  @Path("/examples/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  ExampleUpdateResult updateExample(@PathParam("id") final String exampleID, final Example updatedExample);

  /**
   * Associate a human key with an example.
   * @param exampleID - the id of the example
   * @param key - the human annotation
   */
  @PUT
  @Path("/examples/{id}/key")
  @Consumes(MediaType.APPLICATION_JSON)
  void keyExample(@PathParam("id") final String exampleID, final ExampleCall key);

  /**
   * Same as keyExample, except bulk
   * @param key - the human call
   * @param exampleIDs - the list of example ids
   * @return true if no exceptions occur
   */
  @POST
  @Path("/examples/keys")
  @Consumes(MediaType.APPLICATION_JSON)
  boolean keyExamples(@QueryParam("key") final ExampleCall key, final List<String> exampleIDs);

}
