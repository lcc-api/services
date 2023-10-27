package com.languagecomputer.services.vectorstore;

import io.swagger.v3.oas.annotations.Operation;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author smonahan
 */
public interface VectorStoreService {

  @Operation(description = "Get the vector for the given ID")
  @GET
  @Path("space/{vector_space}/vector/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  VectorMessage getVector(@PathParam("vector_space") final String vectorSpace, @PathParam("id") final String id);

  @Operation(description = "Put a vector with the given ID into the given vector space. If a vector with the given ID already exists, it's value is updated.")
  @POST
  @Path("space/{vector_space}/vector")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  void putVector(@PathParam("vector_space") final String vectorSpace, VectorMessage vector);

  @Operation(description = "Put a vector with the given ID into the given vector space. If a vector with the given ID already exists, it's value is updated.")
  @POST
  @Path("space/{vector_space}/vectors")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  default void bulkPutVectors(@PathParam("vector_space") final String vectorSpace, List<VectorMessage> vector) {
    for (VectorMessage vectorWrapper : vector) {
      this.putVector(vectorSpace, vectorWrapper);
    }
  }

  @Operation(description = "Find up nearest neighbors to the supplied vector. Vectors are ordered by cosine distance from the query vector.")
  @POST
  @Path("space/{vector_space}/neighbors")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  VectorNeighbors findNN(@PathParam("vector_space") final String vectorSpace, VectorNearestNeighborsQuery query);

  @Operation(description = "Find up nearest neighbors to the vector. Vectors are ordered by cosine distance from the query vector.")
  @GET
  @Path("space/{vector_space}/vector/{vector_id}/neighbors")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  VectorNeighbors findNN(
      @PathParam("vector_space") final String vectorSpace,
      @PathParam("vector_id") final String vectorId,
      @Nullable @QueryParam("num_neighbors") Integer numNeighbors, // default value is 100 if not supplied
      @Nullable @QueryParam("max_distance") Double maxDistance
  );

}
