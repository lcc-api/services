package com.languagecomputer.services.filestore;

import com.google.common.io.Files;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.cxf.helpers.IOUtils;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Get a video from the FileStore.
 *
 * Equivalent to
 *   wget -qO- --header="authorization:Bearer $token" ${configURL}${response} > downloadedVideo.mp4
 * @author smonahan
 */
public class GetFile {
  private static class Arguments extends CommandLineUtils.ServiceArgs {

    @CommandLine.Option(names = {"--path"}, description = {"the path to the video"}, required=true)
    String path;

    @CommandLine.Option(names = {"--saveAs"}, description = {"the path to a file to save as"}, required=true)
    File saveAs;
  }

  public static void main(String[] rawArgs) throws IOException {
    GetFile.Arguments args = CommandLineUtils.parseArgs(new GetFile.Arguments(), rawArgs);
    if(args.saveAs.exists()) {
      SampleOutput.outputErr("File already exists");
      return;
    }
    HttpClient httpClient = new HttpClient(new SimpleHttpConnectionManager());
    String path = args.configURL + args.path;
    final GetMethod getMethod = new GetMethod(path);
    getMethod.addRequestHeader("Authorization", "Bearer " + args.token);
    int statusCode = 0;
    try {
      statusCode = httpClient.executeMethod(getMethod);
      if(statusCode != 200) {
        SampleOutput.outputErr("statusCode " + statusCode);
        throw new RuntimeException("Unable to submit video");
      }
      InputStream response = getMethod.getResponseBodyAsStream();
      final byte[] bytes = IOUtils.readBytesFromStream(response);
      SampleOutput.printF("writing %d bytes to %s", bytes.length, args.saveAs);
      Files.write(bytes, args.saveAs);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
