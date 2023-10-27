package com.languagecomputer.services.ontology;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

import java.net.URI;

import static com.languagecomputer.services.ontology.CreateOntologyType.*;

/**
 * Get an ontology concept from its name.
 * @author smonahan
 */
public class GetOntologyConceptByName {

  public static void main(String[] rawArgs) {
    final OntologyArguments serviceArgs = CommandLineUtils.parseArgs(new OntologyArguments(), rawArgs);
    LCCServiceInfo serviceInfo = new LCCServiceInfo(serviceArgs);
    final Ontology ontology = serviceInfo.getService("FULLONTOLOGY", Ontology.class);
    SimpleOntologyRecord sor =  OntologyLabelToConcept.getRecordForLabel(ontology, serviceArgs.cc, serviceArgs.concept);
    SampleOutput.println("found record: " + sor);
  }
}
