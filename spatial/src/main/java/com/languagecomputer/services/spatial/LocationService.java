package com.languagecomputer.services.spatial;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * A service wrapping LCC's internal gazetteer, which contains around 7 million locations worldwide.
 * Created by marc on 3/19/19.
 */
@OpenAPIDefinition(
    info=@Info(
        title = "Location Service for gazetteer queries",
        version = "0.9.0"
    )
)
@Path("/api/gazetteer")
public interface LocationService {
//
  @POST
  @Path("/spatialService/gazetteer/sQuery")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  List<SimpleGazetteerRecord> getSimpleGazetteerRecords(SimpleGazetteerQuery query);

  @PUT
  @Path("/spatialService/gazetteer/sPut")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  void addSimpleGazetteerRecords(SimpleGazetteerRecord sgr);

}
