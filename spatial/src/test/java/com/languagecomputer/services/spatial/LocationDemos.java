package com.languagecomputer.services.spatial;

import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.util.RestClients;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by marc on 3/20/19.
 * Demonstrates how to use the location search interface
 * args[0] is the query type
 * other arguments depend on the query type. use the --help argument for more information.
 */
public class LocationDemos {
  public static final String GAZETTEER_ENDPOINT = "/api/gazetteer";

  @CommandLine.Command()
  private static class Arguments extends CommandLineUtils.ServiceArgs {
    @CommandLine.Option(names = "--type")
    String type = null;

    @CommandLine.Command(name = "SIMPLE", mixinStandardHelpOptions = true, version = "1.0")
    public void simpleQuery(
        @CommandLine.Parameters(index = "0", description = "query", paramLabel = "query")
        String query
    ) {
      submitQuery(this, SimpleGazetteerQueryBuilder.Companion.simpleQuery(query));
    }

    @CommandLine.Command(name = "RELATIVE_LAT_LONG", mixinStandardHelpOptions = true, version = "1.0")
    public void relativeLatLongQuery(
        @CommandLine.Parameters(index = "0", paramLabel = "latitude")
        double latitude,
        @CommandLine.Parameters(index = "1", paramLabel = "longitude")
        double longitude,
        @CommandLine.Parameters(index = "2", paramLabel = "maxDistance", description = "maximum distance in kilometers")
        double maxDistanceKilos
    ) {
      submitQuery(this, SimpleGazetteerQueryBuilder.Companion.relativeQuery(latitude, longitude, maxDistanceKilos));
    }

    @CommandLine.Command(name = "RELATIVE", mixinStandardHelpOptions = true, version = "1.0")
    public void relativeQuery(
        @CommandLine.Parameters(index = "0", paramLabel = "name")
        String name,
        @CommandLine.Parameters(index = "1", paramLabel = "maxDistance", description = "maximum distance in kilometers")
        double maxDistanceKilos
    ) {
      submitQuery(this, SimpleGazetteerQueryBuilder.Companion.relativeQuery(name, maxDistanceKilos));
    }
  }

  public static void main(String[] rawArgs) {
    CommandLineUtils.parseArgAndExecute(new Arguments(), rawArgs);
  }

  public static void submitQuery(Arguments args, SimpleGazetteerQueryBuilder querybuilder) {
    if (args.type != null) {
      querybuilder.setType(args.type);
    }
    SimpleGazetteerQuery query = querybuilder.build();
    final LocationService gaz = RestClients.createWithAuth(args.configURL, LocationService.class, args.token, 20, TimeUnit.MINUTES);
    SampleOutput.outputErr("Querying " + args.configURL + " with " + query);
    List<SimpleGazetteerRecord> results = gaz.getSimpleGazetteerRecords(query);
    for (SimpleGazetteerRecord sgr : results) {
     SampleOutput.outputErr(sgr.toString());
    }
  }
}
