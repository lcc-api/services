Interface project for publicly exposed LCC REST services

Requirements:
* Java 17
* Maven 3.8.5


This project is free source and enables third parties to develop against LCC's interface.

Each subproject is roughly associated with a single LCC RESTful web service.

Modules tend to be java interfaces with jax.rs annotations detailing the API, message classes written in Kotlin, with example mains either in the test package or in the client/ module

Subprojects:
* api
  * Common objects and interfaces that all modules depend on 
* client
  * Runtime applications that utilize the other modules
* Core Services
  * docprocess
    * Interfaces for submitting documents to LCC's document processing pipeline
  * examplestore
    * Examples are representation of text or other spans labelled with a concept that support human annotation  
    * Annotations used to validate quality, provide data to re-train models, and other uses
  * job
    * Keeps track of running jobs throughout LCC's pipeline, enables third party tracking of job status
  * ontology
    * The Ontology is a customizable taxonomy of known concepts that define what is extracted 

* Advanced Services
  * filestore
    * store large files such as videos to avoid having to pass them around multiple times
  * mmannstore
    * The multimodal annotation store for tracking annotations on top of video/image or various other formats.
  * mmdocprocess
    * The multimodal version of document processing (enables processing of videos, audio, and images)
  * multimodal
    * Message objects for multimodal projects, such as video, audio, and imagery
  * vectorstore
    * store vectors for later retrieval or nearest neighbor search
