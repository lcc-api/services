package com.languagecomputer.services.examplestore;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.ontology.ConceptClass;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

/**
 * Add a "role Example" to a predicate trigger.  For example, associate the location with an event.
 * The role is the "location" and the event is associated with the trigger (e.g. verb) of the event.
 * @author smonahan
 */
public class AddRoleExampleToAtessa {
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

    @CommandLine.Parameters(index = "6", description = {"index of the starting character of the role text"})
    int startChar2 = -1;

    @CommandLine.Parameters(index = "7", description = {"index of the ending character of the role text"})
    int endChar2 = -1;

    @CommandLine.Parameters(index = "8", description = {"roleType", "the type of role"})
    String roleType = "";
  }

  private final ExampleStoreService client;

  public AddRoleExampleToAtessa(CommandLineUtils.ServiceArgs args) {
    client = new LCCServiceInfo(args).getService(EXAMPLE_STORE_SERVICE, ExampleStoreService.class);
  }

  public Example addExampleExternal(ExternalExample ee) {
    return client.addExampleExternal(ee);
  }

  public static void main(String[] rawArgs) {
    AddRoleExampleToAtessa.Arguments args = CommandLineUtils.parseArgs(new AddRoleExampleToAtessa.Arguments(), rawArgs);
    AddRoleExampleToAtessa service = new AddRoleExampleToAtessa(args);
    Map<String, String> properties = new HashMap<>();
    properties.put("external", "true");
    if(args.propKey != null && args.propValue != null) {
      properties.put(args.propKey, args.propValue);
    }
    ExternalExample externalExample = new ExternalExample(
        args.docid,
        args.startChar, args.endChar - args.startChar,
        args.cc, args.concept,
        properties, ExampleCall.POSITIVE,
        args.startChar2, args.endChar2 - args.startChar2, null, null, args.roleType);
    Example result = service.addExampleExternal(externalExample);
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
