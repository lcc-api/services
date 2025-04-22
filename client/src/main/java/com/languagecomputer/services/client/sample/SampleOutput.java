package com.languagecomputer.services.client.sample;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Output wrapper designed to encapsulate output for sample applications such that it can later be swapped out with a logger.
 *
 * A logger is not currently used to minimize the third party dependencies and enable users to swap in their own logging system.
 *
 * @author smonahan
 */
public class SampleOutput {

  private static PrintStream out = System.out;
  private static PrintStream err = System.err;

  private SampleOutput() {}

  public static void println(Object output) {
    out.println(output);
  }

  public static void print(Object output) {
    out.print(output);
  }

  public static void printF(String output, Object... args) {
    out.printf(output, args);
  }

  public static void outputErr(Object output) {
    err.println(output);
  }

  public static void outputException(IOException e) {
    e.printStackTrace(err);
  }

  public static void setErrorStream(PrintStream errStream) {
    err = errStream;
  }

  public static void setOutputStream(PrintStream outStream) {
    out = outStream;
  }
}
