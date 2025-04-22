package com.languagecomputer.wikipedia;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

// Class wrapping LCC's rich set of Wikipedia based knowledge
public interface WikipediaProcessing {

  /**
   * search for Wikipedia information about the given string
   * @param surfaceText term to search in Wikipedia. "List of x" will return the list links for x.
   * @return a WikipediaServiceResult containing the page results for the given entity
   */
  @GET
  @Path("/process/{surfaceText}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  WikipediaServiceResult process(@PathParam("surfaceText") final String surfaceText);
}
