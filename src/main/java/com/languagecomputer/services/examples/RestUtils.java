package com.languagecomputer.services.examples;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * This class provides utilities for getting and posting to RESTful endpoints.
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public class RestUtils {
  public static <T> T post(URL url, Object request, Class<T> resultClass) throws IOException {
    Charset charset = Charsets.UTF_8;
    URLConnection connection = url.openConnection();
    connection.setDoOutput(true); // Triggers POST.
    connection.setRequestProperty("Accept-Charset", charset.toString());
    connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
    OutputStream output = connection.getOutputStream();
    // 1. convert message to JSON
    Gson gson = new Gson();
    String jsonInString = gson.toJson(request);
    // 2. send request
    output.write(jsonInString.getBytes(charset));
    // 3. read result
    String text = "";
    try(
        final InputStream in = connection.getInputStream();
        final InputStreamReader inr = new InputStreamReader(in)) {
      text = CharStreams.toString(inr);
      // 4. convert result to object
    }
    T result = gson.fromJson(text, resultClass);
    return result;
  }
}
