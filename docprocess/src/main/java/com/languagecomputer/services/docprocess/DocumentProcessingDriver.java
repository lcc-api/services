package com.languagecomputer.services.docprocess;

import com.google.common.util.concurrent.Uninterruptibles;
import com.languagecomputer.services.job.Job;

import java.util.Collection;
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

  public String createOrUpdateDocument(DocumentJob job, String outputterType, boolean doReprocess) {
    return client.createOrUpdateDocument(job, outputterType, doReprocess);
  }

  public void waitForJobs(Collection<Long> jobids,
                          Consumer<DocumentProcessingStatusDetail> update,
                          Long waitMs) {
    for(Long jobid : jobids) {
      waitForJob(jobid, update, waitMs);
    }
  }

  public void waitForJob(Long jobid,
                         Consumer<DocumentProcessingStatusDetail> update,
                         Long waitMs) {
    DocumentProcessingStatusDetail statusDetail = null;
    while (statusDetail == null || statusDetail.stillWorking()) {
      statusDetail = getStatus(jobid);
      update.accept(statusDetail);
      Uninterruptibles.sleepUninterruptibly(waitMs, TimeUnit.MILLISECONDS);
    }
  }

  public DocumentType getType() {
    return type;
  }

  public DocumentProcessingLevel getLevel() {
    return level;
  }
}
