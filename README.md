# services
Language Computer APIs and Example for RESTful services

This project only contains interfaces and object definitions, and is designed to interact via REST with a Java program which implements these interfaces.

## Supported Services
* Lexicon Expansion - com/languagecomputer/services/api/LexicalExpansionServiceInterface.java
  * mvn exec:java -Dexec.mainClass="com.languagecomputer.services.examples.LexicalExpansionExample" -Dexec.args="http://IP_ADDRESS:9122/lexicalexpansion/expand kiwi"

## Messages
* src/main/java/com/languagecomputer/services/messages
* This package contains the input and output Java classes which are serialized as JSON to the web services

## Examples
* src/main/java/com/languagecomputer/services/examples
  * This package contains examples for how to use the services
