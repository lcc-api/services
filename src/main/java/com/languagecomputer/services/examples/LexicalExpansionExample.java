package com.languagecomputer.services.examples;

import com.languagecomputer.services.messages.LexicalExpansionRequest;
import com.languagecomputer.services.messages.LexicalExpansionResult;
import com.languagecomputer.services.messages.LexicalItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an example of how to utilize the Lexical Expansion service to expand lexemes.
 *
 * mvn exec:java -Dexec.mainClass="com.languagecomputer.services.examples.LexicalExpansionExample" -Dexec.args="http://IP_ADDRESS:9122/lexicalexpansion/expand kiwi"
 *
 * Copyright 2017 Language Computer Corporation.
 * This software is provided to the US Government with unlimited rights.
 *
 */
public class LexicalExpansionExample {

  public static void main(String[] args) throws IOException {
    URL url = new URL(args[0]);
    List<LexicalItem> seeds = new ArrayList<>();
    for(int i = 1; i < args.length; i++) {
      seeds.add(new LexicalItem(args[i]));
    }
    LexicalExpansionRequest request = new LexicalExpansionRequest(seeds);
    LexicalExpansionResult result = RestUtils.post(url, request, LexicalExpansionResult.class);
    List<String> strings = result.asStrings();
    System.out.println(result.asStrings());
    if(result.getScores() != null) {
      System.out.println(result.getScores());
    }
    System.out.println(result.getExplanations());
    for(int i = 0; i < strings.size(); i++) {
      System.out.println(strings.get(i) + "\t" + result.getExplanations().get(i));
    }
  }
}
