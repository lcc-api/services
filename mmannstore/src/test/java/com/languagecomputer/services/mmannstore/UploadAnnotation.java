package com.languagecomputer.services.mmannstore;

import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.util.JacksonUtil;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author smonahan
 */
public class UploadAnnotation {

  private static class Arguments extends CommandLineUtils.ServiceArgs {

    @CommandLine.Option(names = {"--annotation"}, description = {"the file path to a file containing the annotation"}, required=true)
    File annotation;
  }

  public static void main(String[] rawArgs) throws IOException {
    UploadAnnotation.Arguments args = CommandLineUtils.parseArgs(new UploadAnnotation.Arguments(), rawArgs);
    MultimodalAnnotationStore annstore = new LCCServiceInfo(args).getService("MM_DOCUMENT_STORE", MultimodalAnnotationStore.class);
    AttributeStoreMessage asm = JacksonUtil.fromJson(new FileReader(args.annotation), AttributeStoreMessage.class);
    queryEntityID(asm.getEntityId(), annstore);
    SampleOutput.println("saving " + JacksonUtil.toJson(asm));
    annstore.addAttributeStoreMessage(asm);
    queryEntityID(asm.getEntityId(), annstore);
  }

  private static void queryEntityID(String entityId, MultimodalAnnotationStore annstore) {
    AttributeStoreMessageQuery query = new AttributeStoreMessageQuery(entityId);
    final AttributeStoreMessageList list = annstore.queryAttributeStoreMessage(query, false);
    SampleOutput.printF("found %d attributes for entity %s\n", list.getAttributeStoreMessages().size(), entityId);
  }
}
