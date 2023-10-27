package com.languagecomputer.services.filestore;

import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Upload a non-video file
 */
public class UploadFile {
  /**
   * Represents the UUID of the video, used for reference throughout the system.
   */
  static class UUIDArguments {
    @CommandLine.Option(names = {"--uuid"}, description = {"the uuid"})
    String uuid;

    @CommandLine.Option(names = {"--random", "--randomUUID"}, description = {"generate a random UUID and output it"})
    boolean randomUUID;

    public String getUUID() {
      if(uuid != null && uuid.length() > 0) {
        return uuid;
      } else if(randomUUID) {
        return UUID.randomUUID().toString();
      }
      throw new IllegalArgumentException("invalid uuid");
    }
  }

  static class ContentTypeArguments {
    @CommandLine.Option(names = {"--contentType"}, description = {"the content type"})
    String contentType;

    @CommandLine.Option(names = {"--detectType"}, description = {"detect the content type"})
    boolean detect;

    public String getContentType(Path path) {
      if(contentType != null && contentType.length() > 0) {
        return contentType;
      } else if(detect) {
        try {
          return Files.probeContentType(path);
        } catch(Exception e) {
          throw new RuntimeException(e);
        }
      }
      throw new IllegalArgumentException("invalid content type provided");
    }
  }
  private static class Arguments extends CommandLineUtils.ServiceArgs {

    @CommandLine.Option(names = {"--file"}, description = {"the file we are uploading"}, required=true)
    public File file;

    @CommandLine.ArgGroup(multiplicity = "1")
    UUIDArguments uuid;

    @CommandLine.ArgGroup(multiplicity = "1")
    ContentTypeArguments contentType;

  }

  public static void main(String[] rawArgs) throws IOException {
    UploadFile.Arguments args = CommandLineUtils.parseArgs(new UploadFile.Arguments(), rawArgs);
    if (!args.file.exists()) {
      SampleOutput.outputErr("no such file");
      return;
    }
    String uuid = args.uuid.getUUID();
    String contentType = args.contentType.getContentType(args.file.toPath());
    //file.setContentType("video/mp4");
    SampleOutput.outputErr("UUID: " + uuid);
    String response = uploadFile(args.configURL, args.file, uuid, args.token, contentType);
    SampleOutput.println(response);
  }

  public static String uploadFile(URI configURL, File file, String uuid, String token, String contentType) throws IOException {
    HttpClient httpClient = new HttpClient(new SimpleHttpConnectionManager());
    String config = configURL.toString() + "/api/config/upload/" + uuid;
    System.err.println(config + " " + contentType);
    final PostMethod postMethod = new PostMethod(config);
    postMethod.addRequestHeader("Authorization", "Bearer " + token);
    FilePart filePart = new FilePart("file", file);
    filePart.setContentType(contentType);
    Part[] part = {filePart};
    MultipartRequestEntity multipartEntity = new MultipartRequestEntity(part, postMethod.getParams());
    postMethod.setRequestEntity(multipartEntity);
    int statusCode = 0;
    try {
      statusCode = httpClient.executeMethod(postMethod);
      if(statusCode != 200) {
        SampleOutput.outputErr("statusCode " + statusCode);
        throw new RuntimeException("Unable to submit file");
      }
      String response = postMethod.getResponseBodyAsString();
      return response;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}