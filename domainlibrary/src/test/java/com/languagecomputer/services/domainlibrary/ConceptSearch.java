package com.languagecomputer.services.domainlibrary;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.ontology.ConceptClass;
import picocli.CommandLine;

/**
 * Dynamic free-form concept search (not keyword based) using domain library neural network.
 * @author smonahan
 */
public class ConceptSearch {

  private static final String DOMAIN_LIBRARY_SERVICE = "DOMAIN_LIBRARY";

  private static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Option(names = "--label", description = {"the concept label"}, required = true)
    String label;

    @CommandLine.Option(names = "--internalOnly", description = {"whether to include domain library concepts"}, required = true)
    Boolean internalOnly;

    @CommandLine.Option(names = "--conceptClass", description = {"the concept class, or none"}, required = false)
    ConceptClass conceptClass = null;
  }

  public static void main(String[] rawArgs) {
    ConceptSearch.Arguments args = CommandLineUtils.parseArgs(new ConceptSearch.Arguments(), rawArgs);
    DomainLibrary client = new LCCServiceInfo(args).getService(DOMAIN_LIBRARY_SERVICE, DomainLibrary.class);
    ConceptSearchRequest request = new ConceptSearchRequest(args.label, args.conceptClass, null, args.internalOnly);
    final ConceptSearchResult result = client.conceptSearch(request);
    ConceptSearchUtil.printConceptSearchResult(result, System.out);
  }
}
