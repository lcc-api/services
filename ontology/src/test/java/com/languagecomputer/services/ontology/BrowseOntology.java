package com.languagecomputer.services.ontology;

import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Various utilities to interact with the ATESSA ontology.
 *
 * If a third party wants to ingest LCC's current ontology for integration this demonstrates that.
 *
 * @author smonahan
 */
public class BrowseOntology {

  public static class BrowseOntologyArgs extends CommandLineUtils.ServiceArgs {
    @CommandLine.Option(names = {"--limit", "--n"}, description = {"the number of concepts of each class to print out details for"})
    public Integer limit = 100;
  }

  public static void main(String[] rawArgs) {
    final BrowseOntologyArgs args = CommandLineUtils.parseArgs(new BrowseOntologyArgs(), rawArgs);
    Ontology ontology = new LCCServiceInfo(args).getService("FULLONTOLOGY", Ontology.class);
    final Collection<String> rootConcepts = ontology.getRootConcepts();
    System.out.println("root concepts " + rootConcepts);
    StringBuilder summary = new StringBuilder();
    for (ConceptClass conceptClass : EnumSet.allOf(ConceptClass.class)) {
      int n = 0;
      System.out.println(conceptClass);
      Collection<String> names = ontology.getNamesForConceptClass(conceptClass);
      for(String name : names) {
        SimpleOntologyRecord concept = ontology.getRecord(conceptClass, name);
        SampleOutput.println("\t" + concept.getName() + "\t" + concept.getLabel());
        if(n++ > args.limit) {
          break;
        }
      }
      summary.append("ATESSA knows about " + names.size() + " " + conceptClass + "\n");
    }
    SampleOutput.println(summary);
  }
}
