package com.languagecomputer.services.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.CharStreams;
import com.languagecomputer.services.client.sample.SampleOutput;
import com.languagecomputer.services.util.JacksonUtil;

import javax.annotation.Nullable;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Utility methods for dealing with REST clients and services.
 *
 * @author wayne
 **/
public final class RestUtils {

  private RestUtils() {}

  /**
   * Inserts the headers required for cross-origin resource sharing.
   * See https://en.wikipedia.org/wiki/Cross-origin_resource_sharing for details.
   *
   * @param headers Headers from the response object.  These will be modified in-place.
   */
  public static void addCorsHeaders(final MultivaluedMap<String, Object> headers) {
    // Note that the settings here allow any Javascript with network access to the services to send requests.
    // This many not be appropriate for all security environments.

    // Make sure these aren't duplicated
//    headers.putSingle("Access-Control-Allow-Origin", "*");
    headers.putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    headers.putSingle("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, X-XRSF-TOKEN");
  }

  /**
   * Creates an {@link Entity} from an arbitrary Java object, with the media type specified for JSON
   *
   * @param data Java object to pack into an entity
   * @return {@link Entity} with the given object packed inside
   */
  public static <T> Entity<T> jsonEntity(final T data) {
    return Entity.entity(data, MediaType.APPLICATION_JSON_TYPE);
  }

  /**
   * Creates connection object with default settings.
   * @param url - URL to be requested
   * @return connection object
   * @throws IOException
   */
  public static URLConnection getURLConnection(URL url) throws IOException {
    Charset charset = StandardCharsets.UTF_8;
    URLConnection connection = url.openConnection();
    connection.setRequestProperty("Accept-Charset", charset.toString());
    connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
    return connection;
  }

  /**
   * Get the default serializer used for when it is not provided as an argument.
   * @return Default object used to serialize requests.
   */
  private static Function<Object,String> getSerializer(){
    return req -> {
      try {
        return JacksonUtil.objectMapper().writeValueAsString(req);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
    };
  }

  /**
   * Get the default deserializer used for when it is not provided as an argument.
   *
   * @param targetClass type of response
   * @return Default deserialization function for responses
   */
  private static <T> Function<String, T> getDeserializer(Class<T> targetClass){
    return s -> {
      try {
        return JacksonUtil.objectMapper().readValue(s, targetClass);
      } catch (JsonProcessingException e) {
        throw new IllegalArgumentException(e);
      }
    };
  }

  /**
   * Makes an HTTP GET request, performing no serialization or deserialization.
   *
   * @param connection        - Connection object
   * @param logException
   * @return Response to GET request
   */
  private static String getResponseFromConnection(URLConnection connection, boolean logException)  throws IOException {
    // 3. read result
    String text = "";
    try(final InputStream in = connection.getInputStream();
        final InputStreamReader inr = new InputStreamReader(in)) {
      text = CharStreams.toString(inr);
      return text;
    } catch(IOException e) {
      if(logException) {
        SampleOutput.outputException(e);
      }
      throw new RuntimeException(e);
    }
  }

  private static <T> T get(URLConnection connection, Function<String, T> deserializer, boolean logException) throws IOException {
    return deserializer.apply(getResponseFromConnection(connection, logException));
  }

  public static <T> T get(URLConnection connection, Class<T> resultClass) throws IOException {
    return get(connection, getDeserializer(resultClass), true);
  }

  /**
   * Get content from a URL
   * @param url - url
   * @param resultClass - class of expected results
   * @param <T> class of the expected result object
   * @return Deserialized HTTP response body
   */
  public static <T> T get(URL url, Class<T> resultClass) throws IOException {
    return get(getURLConnection(url), resultClass);
  }

  public static <T> T get(URL url, Class<T> resultClass, String auth)  throws IOException {
    return get(url, resultClass, auth, true);
  }

  public static <T> T get(URL url, Class<T> resultClass, String auth, boolean logException)  throws IOException {
    URLConnection connection = getURLConnection(url);
    // TODO: resolve #11196
    connection.setRequestProperty("Authorization", "Basic " + auth);
    return get(connection, getDeserializer(resultClass), logException);
  }

  /**
   * Method performs a post request given a url and a body string.
   *
   * @param connection   - connection object for url to post message to
   * @param requestBody  - a string containing the message body.
   * @param authToken
   * @param logException
   */
  private static String post(URLConnection connection, String requestBody, @Nullable String authToken, boolean logException) throws IOException {
    connection.setDoOutput(true); // Triggers POST.
    if(authToken != null) {
      connection.setRequestProperty("Authorization", "Bearer " + authToken);
    }
    Charset charset = StandardCharsets.UTF_8;
    OutputStream output = connection.getOutputStream();
    if(output == null) {
      SampleOutput.outputErr("NULL CONNECTION - ");
      return "";
    }
    if(requestBody!=null) {
      output.write(requestBody.getBytes(charset));
    }
    return getResponseFromConnection(connection, logException);
  }

  private static <T> T post(URLConnection connection, String request, Function<String, T> deserializer, boolean logException) throws IOException {
    return deserializer.apply(post(connection, request, (String)null, logException));
  }

  private static <T> T postWithBearerAuth(URLConnection connection, String request, String authToken, Function<String, T> deserializer, boolean logException) throws IOException {
    return deserializer.apply(post(connection, request, authToken, logException));
  }

  /**
   * Overload for requests where we want to do our own serialization and deserialization
   */
  public static String post(URL url, String requestBody) throws IOException {
   return post(url, requestBody, true);
  }
  public static String post(URL url, String requestBody, boolean logException) throws IOException {
    return post(getURLConnection(url), requestBody, (String)null, logException);
  }

  /**
   * Overload for requests where we want to do our own serialization and deserialization
   */
  public static String postWithBearerAuth(URL url, String requestBody, String authToken) throws IOException {
    return post(getURLConnection(url), requestBody, authToken, true);
  }

  /**
   * Overload for requests where we want to do our own deserialization.
   */
  public static String post(URL url, Object request) throws IOException {
    return post(getURLConnection(url), getSerializer().apply(request), (String)null, true);
  }

  /**
   * Overload for requests where we've already performed serialization.
   */
  public static <T> T post(URL url, String requestBody, Class<T> responseClass) throws IOException{
    return post(url, requestBody, responseClass, true);
  }

  public static <T> T post(URL url, String requestBody, Class<T> responseClass, boolean logException) throws IOException{
    return post(getURLConnection(url), requestBody, getDeserializer(responseClass), logException);
  }

  /**
   * Overload for requests where we've already performed serialization.
   */
  public static <T> T postWithBearerAuth(URL url, String requestBody, String authToken, Class<T> responseClass) throws IOException{
    return postWithBearerAuth(url, requestBody, authToken, responseClass, true);
  }

  public static <T> T postWithBearerAuth(URL url, String requestBody, String authToken, Class<T> responseClass, boolean logException) throws IOException{
    return postWithBearerAuth(getURLConnection(url), requestBody, authToken, getDeserializer(responseClass), logException);
  }

  /**
   * Posts content to a URL
   * @param url - url
   * @param requestBody  - request object
   * @param <T> class of the expected result object
   * @return Deserialized HTTP response body.
   */
  public static <T> T post(URL url, Object requestBody, Class<T> requestType) throws IOException {
    return getDeserializer(requestType).apply(post(getURLConnection(url), getSerializer().apply(requestBody), (String)null, true));
  }
}