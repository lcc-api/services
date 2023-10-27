package com.languagecomputer.services.examplestore;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.ontology.ConceptClass;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

/**
 * Add an example to the ATESSA example store.
 *
 * docid, start char, end char, type, string
 * @author smonahan
 */
public class AddExampleToAtessa {
  private static final String EXAMPLE_STORE_SERVICE = "BASIC_EXAMPLE";

  private static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Parameters(index = "0", description = {"Document ID"})
    String docid = null;

    @CommandLine.Parameters(index = "1", description = {"index of the starting character"})
    int startChar = -1;

    @CommandLine.Parameters(index = "2", description = {"index of the ending character"})
    int endChar = -1;

    @CommandLine.Parameters(index = "3", description = {"Concept ID (not the label!)"})
    String concept = null;

    @CommandLine.Parameters(index = "4", description = {"concept class", "this class of the concept, e.g. ENTITY, PREDICATE_TRIGGER."})
    ConceptClass cc = null;

    @CommandLine.Parameters(index = "5", description = {"expected text", "this parameter is used to validate the span of the example after creation."})
    String expectedText = "";

    @CommandLine.Option(names = {"--propKey"}, description = "a property key to add to example", required = false)
    String propKey = null;

    @CommandLine.Option(names = {"--propValue"}, description = "a property value to add to example", required = false)
    String propValue = null;
  }

  private final ExampleStoreService client;

  public AddExampleToAtessa(Arguments args) {
    client = new LCCServiceInfo(args).getService(EXAMPLE_STORE_SERVICE, ExampleStoreService.class);
  }

  public Example addExampleExternal(String docid, int startChar, int length, ConceptClass conceptClass, String concept, String propKey, String propValue) {
    Map<String, String> properties = new HashMap<>();
    properties.put("external", "true");
    if(propKey != null && propValue != null) {
      properties.put(propKey, propValue);
    }
    ExternalExample externalExample = new ExternalExample(docid, startChar, length, conceptClass, concept, properties, ExampleCall.POSITIVE);
    return client.addExampleExternal(externalExample);
  }

  public static void main(String[] rawArgs) {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    AddExampleToAtessa service = new AddExampleToAtessa(args);
    Example result = service.addExampleExternal(args.docid, args.startChar, args.endChar - args.startChar, args.cc, args.concept, args.propKey, args.propValue);
    if(result == null) {
      SampleOutput.outputErr("result null from adding example");
      return;
    }
    SampleOutput.outputErr("got result with ID: " + result.getID() + " for text " + result.getTrigger());
    if(!args.expectedText.equals(result.getTrigger())) {
      throw new IllegalStateException("produced example with different text than expected \"" + args.expectedText + "\" != \"" + result.getTrigger() + "\"");
    }
  }
}
