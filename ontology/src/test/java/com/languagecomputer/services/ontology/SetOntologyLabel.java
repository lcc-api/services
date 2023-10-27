package com.languagecomputer.services.ontology;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

/**
 * Set a human readable label.  The underlying ontology concept name remains the same, but the outputter can
 * show the new label.  This facilitates integration with existing output formats and third party tools.
 * @author smonahan
 */
public class SetOntologyLabel {

  private static class LabelArguments extends CreateOntologyType.OntologyArguments {

    @CommandLine.Option(names = "--label", description = {"the new label"}, required = true)
    String label;
  }

  public static void main(String[] rawArgs) {
    LabelArguments args = CommandLineUtils.parseArgs(new LabelArguments(), rawArgs);
    Ontology ontology = new LCCServiceInfo(args).getService("FULLONTOLOGY", Ontology.class);
    final SimpleOntologyRecord sor = ontology.getRecord(args.cc, args.concept);
    if(sor == null) {
      SampleOutput.outputErr("no such type for parent as " + args.concept + "  " + args.cc);
      return;
    }
    ontology.setProperty(args.cc.toString(), args.concept, "LABEL",
                         new OntPropertyValue(args.label), false);
    final SimpleOntologyRecord record2 = ontology.getRecord(args.cc, args.concept);
    SampleOutput.println("old label for " + args.concept +
                       " was \"" + sor.getLabel() +
                       "\" new label is \"" + record2.getLabel() + "\"");
  }
}
