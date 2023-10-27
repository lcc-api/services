package com.languagecomputer.services.docprocess;

import com.languagecomputer.services.client.sample.LCCServiceInfo;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.client.util.CommandLineUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URI;

/**
 * @author smonahan
 */
public class GetOutputters extends CommandLineUtils.ServiceArgs {

  public void run() {

  }

  public static void main(String[] args) throws IOException {
    GetOutputters getter = CommandLineUtils.parseArgs(new GetOutputters(), args);
    final LCCServiceInfo lccServiceInfo = new LCCServiceInfo(getter);
    final LCCServiceInfo.ConfigService configService = lccServiceInfo.getConfigService();
    final String servicesWithFacet = configService.getServicesWithFacet(LCCServiceInfo.ConfigService.ServiceFacet.OUTPUTTER);
    SampleOutput.println("servicesWithFacet " + LCCServiceInfo.ConfigService.ServiceFacet.OUTPUTTER + " " + servicesWithFacet);
  }
}
