package com.languagecomputer.services.docprocess;

import com.google.common.util.concurrent.Uninterruptibles;
import com.languagecomputer.services.job.Job;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Methods for interacting with document processing.
 * @author smonahan
 */
public class DocumentProcessingDriver {
  public static final String DEFAULT_JOB_NAME = "addDocumentJob";

  protected final DocumentProcessingService client;
  private final DocumentProcessingLevel level;
  private final DocumentType type;

  public DocumentProcessingDriver(DocumentType type, DocumentProcessingService client, DocumentProcessingLevel level) {
    this.type = type;
    this.client = client;
    this.level = level;
  }


  public DocumentProcessingStatusDetail getStatus(Long jobId) {
    return getStatus(client, jobId);
  }

  public static DocumentProcessingStatusDetail getStatus(DocumentProcessingService client, Long jobId) {
    return client.getDetailedStatus(jobId);
  }

  public Job process(DocumentJob job) {
    return client.createNewDocument(job);
  }

  public String createOrUpdateDocument(DocumentJob job, String outputterType, boolean b) {
    return client.createOrUpdateDocument(job, outputterType, b);
  }

  public void processAndWait(Iterable<DocumentJob> documentJobs,
                             Consumer<DocumentProcessingStatusDetail> update,
                             Long waitMs) {
    for(DocumentJob documentJob : documentJobs) {
      DocumentProcessingStatusDetail statusDetail;
      do {
        statusDetail = getStatus(documentJob.getJobId());
        update.accept(statusDetail);
        Uninterruptibles.sleepUninterruptibly(waitMs, TimeUnit.MILLISECONDS);
      } while (statusDetail == null || statusDetail.getStatuses().stream().anyMatch(
          s ->  s != null && s.getStatus()!=null && !s.getStatus().isFinished()
      ));
    }
  }

  public DocumentType getType() {
    return type;
  }

  public DocumentProcessingLevel getLevel() {
    return level;
  }
}
