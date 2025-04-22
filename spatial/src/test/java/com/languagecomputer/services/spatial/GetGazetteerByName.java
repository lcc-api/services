package com.languagecomputer.services.spatial;

import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Commandline to get all the gazetteer entries for a given name.
 *
 * Serves as the simplest example of both the gazetteer/location service, as well as a standard LCC API query.
 * @author smonahan
 */
public class GetGazetteerByName {

  @CommandLine.Command()
  private static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Option(names = "--name")
    String name = null;
  }

  public static void main(String[] rawArgs) throws IOException {
    Arguments args = CommandLineUtils.parseArgs(new Arguments(), rawArgs);
    SampleOutput.println("Searching for " + args.name);
    // Change out this line if you are not using cls-client to utilize your own client creation
    final LocationService gaz = RestClients.createWithAuth(args.configURL, LocationService.class, args.token, 20, TimeUnit.MINUTES);
    SimpleGazetteerQuery query = SimpleGazetteerQueryBuilder.simpleQuery(args.name).build();
    final List<SimpleGazetteerRecord> results = gaz.getSimpleGazetteerRecords(query);
    SampleOutput.println("Found " + results.size() + " results");
    for (SimpleGazetteerRecord result : results) {
      SampleOutput.println("\t" + result.getName() +
                           "\t" + result.getLatitude() +
                           "\t" + result.getLongitude() +
                           "\t" + result.getDesignation()
      );
    }
  }

}
