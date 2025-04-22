package com.languagecomputer.services.docprocess;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

/**
 * @author smonahan
 */
public class DocumentProcessingUtils {

  private DocumentProcessingUtils() {}

  public static List<File> getFilesRecursive(Collection<String> paths) {
    List<File> filePaths = paths.parallelStream().map(File::new).toList();
    Spliterator<File> allFiles = Files.fileTraverser().depthFirstPreOrder(filePaths).spliterator();
    return StreamSupport.stream(allFiles, false)
        .filter(file -> !file.isHidden() && !file.isDirectory()) // Skip hidden files and directories!
        .toList();
  }

  public static DocumentJob getDocumentJob(File file, String jobName, List<String> stages, DocumentType type, DocumentProcessingLevel level) throws IOException {
    String docid = file.getName();
    if(docid.contains(".")) {
      docid = docid.substring(0, docid.lastIndexOf('.'));
    }
    String text = null;
    String url = null;
    if(file.getName().contains(".pdf") || file.getName().contains(".docx")) {
      byte[] encode = Base64.getEncoder().encode(Files.toByteArray(file));
      text = new String(encode);
    } else if(type == DocumentType.URL) {
      url = file.getName();
    } else {
      text = Files.asCharSource(file, StandardCharsets.UTF_8).read();
    }
    DocumentMessage message = new DocumentMessage(type, docid, Instant.now(), text, url);
    DocumentJob.Builder builder = new DocumentJob.Builder(message, level);
    if(jobName != null) {
      builder.jobName(jobName);
    }
    DocumentJob job = builder.build();
    if(stages != null && !stages.isEmpty()) {
      job.setStages(stages);
    }
    job.getProps().put(SOURCE_URI_META_DATA, file.getName());
    return job;
  }
  public static final String SOURCE_URI_META_DATA = "source_uri";

  public static Iterable<DocumentJob> getMessages(Iterable<File> files, String jobName, DocumentType type, DocumentProcessingLevel level) {
    return StreamSupport.stream(files.spliterator(), false).map(f -> {
      try {
        return getDocumentJob(f, jobName, null, type, level);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }).toList();
  }

}
