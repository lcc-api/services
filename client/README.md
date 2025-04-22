Various Utilities for creating REST Clients to interact with LCC's APIs

* RestClients.kt
  * Creating rest clients which are instances of LCC API interfaces talking to the service over http
* RestUtils
  * Utilities for interacting with rest clients, e.g. posting data
* JacksonUtil
  * Utilities for converting to/from json for interacting with REST APIs
* LCCServiceInfo
  * Primary driver for talking to LCC's Configuration Service, which is a gateway to the rest of the services
* SampleOutput
  * Wrapper for outputting in scripts, in lieu of incorporating a log framework.  We assume the users of this API will have their own logging setup they prefer.
* CommandLineUtils
  * Common utilties for commandline scripts using LCC's APIs
