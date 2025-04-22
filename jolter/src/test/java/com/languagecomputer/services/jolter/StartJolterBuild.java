package com.languagecomputer.services.jolter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.RestUtils;
import com.languagecomputer.services.job.Job;
import com.languagecomputer.services.job.JobArguments;
import com.languagecomputer.services.job.JobService;
import com.languagecomputer.services.util.JacksonUtil;
import picocli.CommandLine;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

/**
 * Script to kick of jolter build.
 * example usage:
 *
 * The set of available model types varies over time and per software version, so check that the your model type is still supported.
 * @author stuart
 */
public class StartJolterBuild extends JobArguments {
  @CommandLine.Option(names = "--model-name", description = {"the name to give the model being created."}, required = true)
  String modelName = null;

  StartJolterBuild(URI configURL, String modelName, ModelType modelType){
    this.configURL = configURL;
    this.modelName = modelName;
    this.modelType = modelType;
  }

  private StartJolterBuild(){
  }

  enum ModelType {
    JOINT_LM_PREDICATE
  }

  @CommandLine.Option(
      names = "--model-type",
      description = {
          "The type of jolter model to create.",
          "Default: " + CommandLineUtils.DEFAULT_VALUE,
          "Possible Values: " + CommandLineUtils.ENUM_VALUES
      })
  ModelType modelType = ModelType.JOINT_LM_PREDICATE;

  @CommandLine.Option(
      names = "--block",
      description= {
          "block on the job",
          "Default: false"
      })
  boolean block = false;

  private Job run() throws IOException {
    return run(Collections.emptySet());
  }

  Job run(Set<String> docIds) throws IOException {
    boolean docIdsProvided = docIds != null && !docIds.isEmpty();
    final LCCServiceInfo lccServiceInfo = new LCCServiceInfo(this);
    URI jolterURL = lccServiceInfo.getURIForService("JOLTER");
    SampleOutput.println(jolterURL);
    URL getModelNames = new URL(jolterURL.toString() + "/" + "model_names");
    SampleOutput.println(getModelNames);
    final Set<String> modelNames = RestUtils.get(getModelNames, Set.class);

    if (modelNames.contains(modelName)) {
      SampleOutput.outputErr("Model with name \"" + modelName + "\" already exists. Pick a different name.");
      System.exit(1);
    }

    // if not, construct the build request by loading re-done request, and then changing the model name.
    final ObjectNode jsonObject;
    if(modelType != ModelType.JOINT_LM_PREDICATE) {
      System.exit(1);
    }

    jsonObject = readJsonObject("/com/languagecomputer/services/client/joint-LM-predicate-model-params.json");
    jsonObject.put("modelName", modelName);
    if (docIdsProvided){
      ArrayNode docIdNode = jsonObject.with("documentQuery").withArray("docIds");
      docIds.forEach(docIdNode::add);
    }
    // sending build request to endpoint
    SampleOutput.println("Sending build model request.");
    URL getBuildModel = UriBuilder.fromUri(jolterURL).path("build_model").build().toURL();
    SampleOutput.println(getBuildModel);
    final Job job = RestUtils.post(getBuildModel, jsonObject.toString(), Job.class);
    SampleOutput.println("jobid: " + job.getId());
    if(block) {
      URI jobServiceURI = lccServiceInfo.getURIForService("JOBS_SERVICE");
      SampleOutput.println("blocking on job id:\"" + job.getId() + "\" name: \"" + job.getName() + "\" at " + jobServiceURI);
      JobService client = lccServiceInfo.getService("JOBS_SERVICE", JobService.class);
      boolean result = handleJob(client);
      if(!result) {
        throw new IllegalStateException("failed to process documents correctly");
      }
    }
    return job;
  }

  /**
   * Read a json object from the classpath
   * @param resourceName - the classpath to the resource
   * @return the contents
   */
  @SuppressWarnings("SameParameterValue")
  private ObjectNode readJsonObject(String resourceName) {
    return JacksonUtil.fromJson(
        new InputStreamReader(this.getClass().getResourceAsStream(resourceName)),
        ObjectNode.class);
  }

  public static void main(String[] args) throws IOException {
    StartJolterBuild startJolterBuild = CommandLineUtils.parseArgs(new StartJolterBuild(), args);
    startJolterBuild.run();
    SampleOutput.println("Build request sent. Verify the build has started properly by checking the Job Status");
  }
}
