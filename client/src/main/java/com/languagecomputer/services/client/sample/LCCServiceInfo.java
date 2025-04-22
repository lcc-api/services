package com.languagecomputer.services.client.sample;

import com.fasterxml.jackson.databind.JsonNode;
import com.languagecomputer.services.client.util.CommandLineUtils;
import com.languagecomputer.services.util.RestClients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Get the list of LCC services and their URLs from the config service
 *
 * @author smonahan
 */
public class LCCServiceInfo {
  // The services by default run at $HOST/api/config
  private static final String CONFIG_SERVICE_ENDPOINT = "/api/config";
  private final ConfigService configService;
  private final URI baseURL;
  private final String token;

  public LCCServiceInfo(CommandLineUtils.ServiceArgs args) {
    this(args.configURL, args.token);
  }

  private LCCServiceInfo(URI configURL, String token) {
    this.baseURL = UriBuilder.fromUri(configURL).replacePath("").build();
    configService = RestClients.createWithAuth(UriBuilder.fromUri(baseURL).replacePath(CONFIG_SERVICE_ENDPOINT).build(), ConfigService.class, token);
    this.token = token;
  }

  /**
   * Helper method to get a service URI from the config service master.
   * @param service                   The service we are interested in.
   * @return                          The URL for the target service
   * @throws IllegalArgumentException if the service is not found.  Could indicate the service is not running, or the
   *                                  service name is invalid.
   */
  public URI getURIForService(String service) {
    final String path = configService.getEndpointForService(service);
    URI address = UriBuilder.fromUri(baseURL).replacePath(path).build();
    SampleOutput.println("using address " + address + " for " + service + " from " + baseURL + " and " + path + " for port "+ baseURL.getPort());
    return address;
  }

  /**
   * Wraps getURIForService and returns an instance of the interface class
   * @param serviceName - The service we are loading
   * @param serviceClass - The interface class we are casting the result to
   * @return - An instance of the interface class
   * @param <T> - The interface class such that the return value doesn't need to be cast
   */
  public <T> T getService(String serviceName, Class<T> serviceClass) {
    URI uri = getURIForService(serviceName);
    SampleOutput.println(serviceName + " " + uri + " " + serviceClass.getName());
    return RestClients.createWithAuth(uri, serviceClass, token);
  }

  /**
   * Get services available to the config service.
   * If services are provided, it returns the subset of those that are available.
   * @param services        Services whose availability we want to know about, or empty for all
   * @return                Services the config service knows are running
   */
  public Set<String> getAvailableServices(String... services){
    final HashSet<String> allAvailableServicesSet = new HashSet<>(configService.getServiceList());
    if (services.length == 0) {
      return allAvailableServicesSet;
    } else {
      final HashSet<String> servicesSet = new HashSet<>();
      for (String service: services){
        if (allAvailableServicesSet.contains(service)){
          servicesSet.add(service);
        }
      }
      return servicesSet;
    }
  }

  /**
   * Convenience method that prints an error message and exits if the provided services are not available according to
   * the config service at the provided URL
   * @param services        Services whose availability we're requiring
   */
  public boolean ensureAvailable(String... services){
    final Set<String> availableServices = getAvailableServices(services);
    Set<String> unavailableServices = new LinkedHashSet<>();
    for (String service: services){
      if (!availableServices.contains(service)){
        unavailableServices.add(service);
      }
    }
    if (unavailableServices.isEmpty()) {
      SampleOutput.printF("The following required services are not available at %s", baseURL);
      SampleOutput.println("");
      for (String service: unavailableServices){
        SampleOutput.outputErr(service);
      }
      return false;
    }
    return true;
  }

  public ConfigService getConfigService() {
    return configService;
  }

  public static void main(String[] rawArgs) {
    final CommandLineUtils.ServiceArgs serviceArgs = CommandLineUtils.parseArgs(new CommandLineUtils.ServiceArgs(), rawArgs);
    URI url = serviceArgs.configURL;
    LCCServiceInfo serviceInfo = new LCCServiceInfo(url, serviceArgs.token);
    List<String> services = serviceInfo.getConfigService().getServiceList();
    for(String service : services) {
      try {
        JsonNode result = serviceInfo.getConfigService().getServiceInstanceList(service);
        String address = result.get("instances").get(0).get("uri").toString();
        String start = result.get("instances").get(0).get("startInstant").toString();
        String facets = result.get("instances").get(0).get("serviceFacets").toString();
        SampleOutput.println(service + "\t" + address + "\t" + start + "\t" + facets);
      } catch (Exception e) {
        SampleOutput.outputErr("failed to get address for: " + service);
      }
    }
  }

  // Interface wrapping LCC's Config Service.  Outside of this class, we do not anticipate significant integration with this.
  @Path("")
  public interface ConfigService {
    @GET
    @Path("services")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> getServiceList();

    @GET
    @Path("services/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    JsonNode getServiceInstanceList(@PathParam("name") String serviceName);

    @GET
    @Path("services/{name}/endpoint")
    @Produces(MediaType.APPLICATION_JSON)
    default String getEndpointForService(@PathParam("name") String serviceName) { return null; }

    @GET
    @Path("services/withFacet/{facet}")
    @Produces(MediaType.APPLICATION_JSON)
    String getServicesWithFacet(@PathParam("facet") ServiceFacet facet);

    // Mirrors an enum in LCC proprierty code
    enum ServiceFacet {
      OUTPUTTER,
      FACETED_FRAME_INDEX,
      MODULITH,
      ANALYTIC,
      ONTOLOGY_AWARE
    }
  }
}
