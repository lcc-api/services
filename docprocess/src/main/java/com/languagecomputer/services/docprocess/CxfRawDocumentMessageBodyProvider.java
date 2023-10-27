package com.languagecomputer.services.docprocess;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

@Produces({"multipart/mixed", MediaType.MULTIPART_FORM_DATA})
@Consumes({"multipart/mixed", MediaType.MULTIPART_FORM_DATA})
@Provider
public class CxfRawDocumentMessageBodyProvider implements MessageBodyWriter<RawDocumentJob>, MessageBodyReader<RawDocumentJob> {

  @Context
  private Providers providers;

  @Override
  public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
    return RawDocumentJob.class.equals(aClass);
  }

  @Override
  public void writeTo(RawDocumentJob rawDocumentJob, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
    List<Attachment> atts = new LinkedList<>();
    final DocumentJob documentJob = new DocumentJob.Builder().document(new DocumentMessage.Builder(rawDocumentJob.getMessage(), "")
        .build()).copyMetadata(rawDocumentJob).build();
    atts.add(new Attachment("job", MediaType.APPLICATION_JSON, documentJob));
    atts.add(new Attachment("file", MediaType.APPLICATION_OCTET_STREAM, rawDocumentJob.getMessage().getPayload()));
    final MessageBodyWriter<MultipartBody> messageBodyWriter = providers.getMessageBodyWriter(MultipartBody.class, type, annotations, mediaType);
    final MultipartBody multipartBody = new MultipartBody(atts, true);
    messageBodyWriter.writeTo(multipartBody, MultipartBody.class, type, annotations, new MediaType("multipart","mixed"), multivaluedMap, outputStream);
  }

  @Override
  public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
    return isWriteable(aClass, type, annotations, mediaType);
  }


  @Override
  public RawDocumentJob readFrom(Class<RawDocumentJob> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
    final MessageBodyReader<MultipartBody> messageBodyReader = providers.getMessageBodyReader(MultipartBody.class, type, annotations, mediaType);
    final MultipartBody mp = messageBodyReader.readFrom(MultipartBody.class, type, annotations, mediaType, multivaluedMap, inputStream);
    final Attachment job = mp.getAttachment("metadata");
    final DocumentJob documentJob = job.getObject(DocumentJob.class);
    final byte[] file = mp.getAttachmentObject("payload", byte[].class);
    RawDocumentMessage documentMessage = new RawDocumentMessage.Builder(documentJob.getMessage(), file).build();
    return new RawDocumentJob.Builder(documentMessage).copyMetadata(documentJob).build();
  }
}
