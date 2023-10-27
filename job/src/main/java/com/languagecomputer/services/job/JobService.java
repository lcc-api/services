package com.languagecomputer.services.job;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  @Produces(MediaType.APPLICATION_JSON)
  List<Job> getJobs();

  @Operation(description = "Get job count")
  @GET
  @Path("count")
  @Produces(MediaType.APPLICATION_JSON)
  long getJobCount();

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

  @Operation(description = "Update job")
  @PUT
  @Path("{jobId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Job updateJob(@PathParam("jobId") Long existingJobId, Job newJobData);

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

  @Operation(description = "Update job property")
  @PUT
  @Path("{jobId}/property/{propertyKey}/{propertyValue}")
  @Produces(MediaType.APPLICATION_JSON)
  Job updateProperty(@PathParam("jobId") Long jobId, @PathParam("propertyKey") Job.PropertyKey propertyKey,
                                  @PathParam("propertyValue") String value);

  @Operation(description = "End job by ID")
  @PUT
  @Path("{jobId}/end")
  void endJob(@PathParam("jobId") Long jobId);

  @Operation(description = "Fail job by ID")
  @PUT
  @Path("{jobId}/fail")
  void failJob(@PathParam("jobId") Long jobId);

  @Operation(description = "Delete job by ID")
  @DELETE
  @Path("{jobId}")
  @Produces(MediaType.APPLICATION_JSON)
  boolean deleteJob(@PathParam("jobId") Long jobId);

  @Operation(description = "Delete all jobs")
  @DELETE
  @Path("deleteAll")
  @Produces(MediaType.APPLICATION_JSON)
  int deleteAllJobs();

  @Operation(description = "Add child relationships to a job")
  @POST
  @Path("{parentJobId}/addChildren")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> addChildJobIds(@PathParam("parentJobId") Long parentJobId, Collection<Long> childJobIds);


  @Operation(description = "Remove child relationships from a job")
  @POST
  @Path("{parentJobId}/removeChildren")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> removeChildJobIds(@PathParam("parentJobId") Long parentJobId, Collection<Long> childJobIds);

  @Operation(description = "Remove all child jobs from a job")
  @POST
  @Path("{parentJobId}/removeAllChildren")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> removeAllChildJobIds(@PathParam("parentJobId") Long parentJobId);

  @Operation(description = "Check if job is a parent")
  @GET
  @Path("{parentJobId}/isParent")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  boolean isParentJobId(@PathParam("parentJobId") Long parentJobId);

  @Operation(description = "Get child jobs of a job")
  @GET
  @Path("{parentJobId}/childJobs")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> getChildJobIds(@PathParam("parentJobId") Long parentJobId);

  @Operation(description = "Replace a child job with some jobs")
  @POST
  @Path("{oldJobId}/isParent")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> replaceChildJobId(@PathParam("oldJobId") Long oldJobId, Collection<Long> newJobIds);

  @Operation(description = "Get all parent job IDs.")
  @GET
  @Path("allParentJobsIds")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> getAllParentJobIds();

  @Operation(description = "Track jobs to a subscribed job")
  @POST
  @Path("{subscribedJobId}/track")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> trackJobIds(
      @PathParam("subscribedJobId") Long subscribedJobId,
      Collection<Long> jobIds
  );

  @Operation(description = "Untrack jobs to a subscribed job")
  @POST
  @Path("{subscribedJobId}/untrack")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> untrackJobIds(
      @PathParam("subscribedJobId") Long subscribedJobId,
      List<Long> jobIds
  );

  @Operation(description = "Get tracked job IDs of a subscribed job")
  @GET
  @Path("{subscriptionJobId}/trackedJobIds")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> getTrackedJobIds(
      @PathParam("subscriptionJobId") Long subscriptionJobId
  );

  @Operation(description = "Replace tracked job ID with a list of new job IDs")
  @POST
  @Path("{oldJobId}/replaceTrackingJobIds/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> replaceTrackingJobId(
      @PathParam("oldJobId") Long oldJobId,
      // Note: 404 error issues if make it a path param
      List<Long> newJobIds
  );

  @Operation(description = "Get subscription job IDs of the tracked job ID")
  @GET
  @Path("{trackingJobId}/subscriptionJobIds")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> getSubscriptionJobIds(
      @PathParam("trackingJobId") Long trackingJobId
  );

  @Operation(description = "Get all subscribed job IDs")
  @GET
  @Path("subscriptionJobIds")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Collection<Long> getAllSubscriptionJobIds();

  /**
   * Get detailed information about subscribed jobs
   *
   * @param jobIds   if provided, return status information for jobs.
   * Otherwise, return information about all subscribed jobs.
   * @return        List containing detailed subscribed job status information.
   */
  @Operation(description = "Get detailed information about subscribed jobs.")
  @GET
  @Path("detailedStatuses")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  JobSubscriptionStatusList getDetailedStatuses(@QueryParam("jobIds") final List<Long> jobIds);

  @Operation(description = "Get detailed information about subscribed jobs.")
  @GET
  @Path("detailedStatuses")
  @Produces(MediaType.APPLICATION_JSON)
  JobSubscriptionStatusList getDetailedStatuses(@QueryParam("jobIds") final List<Long> jobIds, @QueryParam("numResults") int numResults);

  /**
   * Get detailed information about subscribed jobs
   *
   * @param jobId   if provided, return status information for job.
   * Otherwise, return information about all subscribed jobs.
   * @return        List containing detailed subscribed job status information.
   */
  @Operation(description = "Get detailed information about subscribed jobs.")
  @GET
  @Path("detailedStatus")
  @Produces(MediaType.APPLICATION_JSON)
  JobSubscriptionStatusList getDetailedStatus(@Nullable @QueryParam("jobId") Long jobId);

  /**
   * Get summary information about processing subscription jobs.
   *
   * @param jobId If provided, only information regarding job
   *             state associated with this subscription jobId
   *             is returned.  If not provided, information
   *             regarding all subscription job IDs is returned.
   * @return The summary information being returned is the number of subscription jobs in each job state.
   */
  @Operation(description = "Get summary information about processing subscription jobs.")
  @GET
  @Path("statuses")
  @Produces(MediaType.APPLICATION_JSON)
  JobStatusCounts getStatus(@Nullable @QueryParam("jobId") Long jobId);

  @Operation(description = "Get document category status information about processing subscription jobs.")
  @GET
  @Path("docCatStatuses")
  @Produces(MediaType.TEXT_PLAIN)
  String getDocumentCategoryStatusForJob(@Nullable @QueryParam("jobId") Long jobId);

    /***
     * @return summary statuses of subscription job processing, indexed by jobId
     */
  @Operation(description = "Return the number of subscription job processing submissions in each job state, for each job.")
  @GET
  @Path("statusesByJob")
  @Produces(MediaType.APPLICATION_JSON)
  Map<Long, JobStatusCounts> getJobStatuses();
}
