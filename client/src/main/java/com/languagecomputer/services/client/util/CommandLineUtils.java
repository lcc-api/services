package com.languagecomputer.services.client.util;

import com.languagecomputer.services.client.sample.SampleOutput;
import picocli.CommandLine;

import java.net.URI;

/**
 * Utilities for command line parsing for sample code executing this library
 *
 * The primary arguments are the config URL (where LCC's services are running)
 * and the authentication token, which you can find in your user account in the ATESSA UI.
 * @author stuart
 */
public class CommandLineUtils {

  private CommandLineUtils() {}

  public static final String DEFAULT_VALUE = "${DEFAULT-VALUE}";
  public static final String ENUM_VALUES = "${COMPLETION-CANDIDATES}";

  public static class BaseArgs {
    @CommandLine.Option(names = {"--help", "-h"}, description = "if given, prints this help message", usageHelp = true)
    boolean help = false;
  }

  public static class ServiceArgs extends BaseArgs {
    @CommandLine.Option(names = {"--configURL", "--config-url"}, description = {"the location of the config service, e.g. https://IP:8085/"})
    public URI configURL;

    @CommandLine.Option(names = {"--token"}, description = {"the auth token for your user account"})
    public String token;
  }

  private static void showHelpAndExit(CommandLine commandLine, int code) {
    SampleOutput.println(commandLine.getUsageMessage());
    System.exit(code);
  }

  public static <T extends BaseArgs> void parseArgAndExecute(T obj, String[] args) {
    CommandLine commandLine = new CommandLine(obj);
    int status = commandLine.execute(args);
    System.exit(status);
  }

  public static <T extends BaseArgs> T parseArgs(T obj, String[] args) {
    CommandLine commandLine = new CommandLine(obj);
    try {
      commandLine.parseArgs(args);
    } catch (Exception e) {
      SampleOutput.outputErr("\n" + e.getLocalizedMessage() + "\n");
      showHelpAndExit(commandLine, 1);
    }
    if (obj.help) {
      showHelpAndExit(commandLine, 0);
    }
    return obj;
  }

}
