package com.languagecomputer.services.domainlibrary;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.ontology.ConceptClass;
import com.languagecomputer.services.ontology.ConceptIdentifier;
import picocli.CommandLine;

/**
 * Copy a concept out of the domain library and into local ontology.
 * @author smonahan
 */
public class CopyConceptFromDomainLibrary {
  private static final String DOMAIN_LIBRARY_SERVICE = "DOMAIN_LIBRARY";
  private static class Arguments extends CommandLineUtils.ServiceArgs {

    @CommandLine.Option(names = "--domain", description = {"the concept domain"}, required = true)
    String domain;
    @CommandLine.Option(names = "--concept", description = {"the concept name"}, required = true)
    String concept;

    @CommandLine.Option(names = "--cc", description = {"the concept class"}, required = true)
    ConceptClass cc;

    @CommandLine.Option(names = "--copyExtractors", description = {"if true then copy the extractors"}, required = false)
    boolean copyExtractors = true;

    @CommandLine.Option(names = "--copyExamples", description = {"if true then copy the examples"}, required = false)
    boolean copyExamples = true;
  }

  public static void main(String[] rawArgs) {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    DomainLibrary client = new LCCServiceInfo(args).getService(DOMAIN_LIBRARY_SERVICE, DomainLibrary.class);
    ConceptIdentifier ci = new ConceptIdentifier(args.concept, args.concept, args.cc);
    final CopyConceptFromDomainLibraryRequest request = new CopyConceptFromDomainLibraryRequest(ci, args.copyExamples, args.copyExtractors);
    final CopyConceptResponse copyConceptResponse = client.copyConcept(args.domain, args.concept, request);
    SampleOutput.println("copied " + copyConceptResponse.getNumExamples() + " examples and " + copyConceptResponse.getNumExtractors() + " extractors");
  }
}
