package com.languagecomputer.services.ontology;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

/**
 * Create a new type in LCC's ontology.  Note this will not be extracted right away, as the system has no examples of it.
 * But the concept is available for manual annotation in documents, and the user can utilize LCC's workflows for
 * building a new extractor or lexicon.
 * @author smonahan
 */
public class CreateOntologyType {

  public static class OntologyArguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Option(names = {"--cc"}, description = {"the concept class"}, required=true)
    ConceptClass cc = null;

    @CommandLine.Option(names = {"--name", "-n"}, description = {"Concept Name"}, required=true)
    String concept = null;

    @CommandLine.Option(names = {"--parent", "-p"}, description = {"Parent Name"})
    String parent = null;
  }

  public static void main(String[] rawArgs) {
    OntologyArguments args = CommandLineUtils.parseArgs(new OntologyArguments(), rawArgs);
    Ontology ontology = new LCCServiceInfo(args).getService("FULLONTOLOGY", Ontology.class);
    final SimpleOntologyRecord sor = ontology.getRecord(args.cc, args.parent);
    if(sor == null) {
      SampleOutput.outputErr("no such type for parent as " + args.parent + "  " + args.cc);
      return;
    }
    final ConceptClass ccForName = ontology.getConceptClassForName(args.concept);
    if(ccForName != null) {
      SampleOutput.outputErr("concept \"" + args.concept + "\" already exists as a " + ccForName);
      return;
    }
    final OntologyTypeCreationRequest request = new OntologyTypeCreationRequest(args.concept, null, null, false, false);
    ontology.createType(args.cc.toString(), args.parent, request);
  }
}
