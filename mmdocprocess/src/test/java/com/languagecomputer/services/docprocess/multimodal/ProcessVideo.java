package com.languagecomputer.services.docprocess.multimodal;

import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.job.JobArguments;
import com.languagecomputer.services.job.JobService;
import picocli.CommandLine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Given a previously uploaded video (to the filestore) tell the system to process that video with all of the active analytics.
 * @author smonahan
 */
public class ProcessVideo {
  private static class Arguments extends JobArguments {

    @CommandLine.Option(names = {"--url"}, description = {"the url in the FileStore"}, required=true)
    String url;
    @CommandLine.Option(names = {"--title"}, description = {"the title"}, required=true)
    String title;
    @CommandLine.Option(names = {"--uuid"}, description = {"the uuid"}, required=true)
    String uuid;
  }

  public static void main(String[] rawArgs) throws IOException {
    ProcessVideo.Arguments args = CommandLineUtils.parseArgs(new ProcessVideo.Arguments(), rawArgs);
    LCCServiceInfo info = new LCCServiceInfo(args);
    MMDocProcessService mmdp = info.getService("MM_DOCUMENT_PROCESSING", MMDocProcessService.class);
    Map<String, String> metadata = new HashMap<>();
    SubmitVideoRequest request = new SubmitVideoRequest(args.uuid, args.title, args.url, metadata);
    final MultiModalJobResponse multiModalJobResponse = mmdp.submitVideo(request);
    final long jobId = multiModalJobResponse.getJobId();
    // Overwrite the jobid
    args.jobID = jobId + "";
    JobService client = info.getService("JOBS_SERVICE", JobService.class);
    boolean result = args.handleJob(client);
  }
}
