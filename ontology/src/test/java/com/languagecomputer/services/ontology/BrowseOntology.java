package com.languagecomputer.services.ontology;

import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.util.RestClients;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
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
  public static void main(String[] rawArgs) {
    final CommandLineUtils.ServiceArgs args = CommandLineUtils.parseArgs(new CommandLineUtils.ServiceArgs(), rawArgs);
    Ontology ontology = new LCCServiceInfo(args).getService("FULLONTOLOGY", Ontology.class);
    final Collection<String> rootConcepts = ontology.getRootConcepts();
    System.out.println("root concepts " + rootConcepts);
    for (ConceptClass conceptClass : EnumSet.allOf(ConceptClass.class)) {
      System.out.println(conceptClass);
      Collection<String> names = ontology.getNamesForConceptClass(conceptClass);
      for(String name : names) {
        SimpleOntologyRecord concept = ontology.getRecord(conceptClass, name);
        SampleOutput.println("\t" + concept.getName() + "\t" + concept.getLabel());
      }
      SampleOutput.println("\tATESSA knows about " + names.size() + " " + conceptClass);
      SampleOutput.println("\t" + names);
    }
  }
}
