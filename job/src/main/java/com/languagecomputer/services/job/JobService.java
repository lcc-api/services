package com.languagecomputer.services.job;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The job service is used to track long-running jobs across the services so that third parties (and internal services) can
 * observe the progress and block on them completing.
 */
@OpenAPIDefinition(
    info=@Info(
        title = "Job Service Interface",
        version = "0.1"
    )
)
public interface JobService {

  @Operation(description = "Get a job by ID")
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  Job getJob(@PathParam("id") Long id);

  @Operation(description = "Get all jobs (unfiltered)")
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  List<Job> getJobs();

  @Operation(description = "Search jobs")
  @POST
  @Path("search")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  List<Job> searchJobs(JobSearch jobSearch);

  @Operation(description = "Create job")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Job createJob(@Valid Job job);

  @Operation(description = "Update job state")
  @PUT
  @Path("{jobId}/state/{state}")
  @Produces(MediaType.APPLICATION_JSON)
  Job updateState(@PathParam("jobId") Long jobId, @PathParam("state") JobState jobState);

  @Operation(description = "Update job progress")
  @PUT
  @Path("{jobId}/progress/{progress}")
  @Produces(MediaType.APPLICATION_JSON)
  Job updateProgress(@PathParam("jobId") Long jobId, @PathParam("progress") Double progress);

  @Operation(description = "Increment job unit of work")
  @PUT
  @Path("{jobId}/work/{unitsOfWork}")
  @Produces(MediaType.APPLICATION_JSON)
  Job incrementWork(@PathParam("jobId") Long jobId, @PathParam("unitsOfWork") Double unitsOfWork);

  @Operation(description = "Update job property")
  @PUT
  @Path("{jobId}/property/{propertyKey}/{propertyValue}")
  @Produces(MediaType.APPLICATION_JSON)
  Job updateProperty(@PathParam("jobId") Long jobId, @PathParam("propertyKey") JobPropertyKey propertyKey,
                                  @PathParam("propertyValue") String value);

  @Operation(description = "Delete job property")
  @PUT
  @Path("{jobId}/property/{propertyKey}")
  @Produces(MediaType.APPLICATION_JSON)
  Job deleteProperty(@PathParam("jobId") Long jobId, @PathParam("propertyKey") JobPropertyKey propertyKey);

  @Operation(description = "Delete job by ID")
  @DELETE
  @Path("{jobId}")
  @Produces(MediaType.APPLICATION_JSON)
  boolean deleteJob(@PathParam("jobId") Long jobId);

}
