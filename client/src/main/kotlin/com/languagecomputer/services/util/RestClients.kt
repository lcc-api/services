@file:JvmName(name = "RestClients")
package com.languagecomputer.services.util

//import com.languagecomputer.services.docprocess.CxfRawDocumentMessageBodyProvider
import org.apache.cxf.configuration.jsse.TLSClientParameters
import org.apache.cxf.configuration.security.AuthorizationPolicy
import org.apache.cxf.jaxrs.client.JAXRSClientFactory
import org.apache.cxf.jaxrs.client.WebClient
import org.apache.cxf.jaxrs.provider.MultipartProvider
import org.apache.cxf.message.Message
import org.apache.cxf.transport.http.auth.HttpAuthSupplier
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory

/**
 * Utilities for configuring REST Clients for authentication.
 */

private fun getProviders(): List<Any> {
  return listOf(
//    listOf(CxfRawDocumentMessageBodyProvider(), MultipartProvider()),
    listOf(MultipartProvider()),
    jsonProvider()
  ).flatten()
}

private var keyStore: KeyStore? = null

enum class KeystoreType {
  JKS,
  PKCS12;

  companion object {
    @JvmStatic
    fun guessType(filename: String): KeystoreType = when {
      filename.lowercase().endsWith("p12")
           -> PKCS12
      else -> JKS
    }
  }
}

private fun getKeyStore(
  keyTrustStore: InputStream,
  password: String,
  keystoreType: KeystoreType
): KeyStore {
  if (keyStore == null) {
    keyStore = KeyStore.getInstance(keystoreType.name)
    keyStore!!.load(keyTrustStore, password.toCharArray())
  }
  return keyStore!!
}

private fun getKeyManagers(
  keyTrustStore: InputStream,
  password: String,
  keystoreType: KeystoreType
): Array<KeyManager> {
  val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
  keyManagerFactory.init(getKeyStore(keyTrustStore, password, keystoreType), password.toCharArray())
  return keyManagerFactory.keyManagers
}

private fun getTrustManagers(
  keyTrustStore: InputStream,
  password: String,
  keystoreType: KeystoreType
): Array<TrustManager> {
  val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
  trustManagerFactory.init(getKeyStore(keyTrustStore, password, keystoreType))
  return trustManagerFactory.trustManagers
}

fun <T> configureSsl(
  service: T,
  keyTrustStore: InputStream,
  password: String,
  keystoreType: KeystoreType
) {
  val tlsClientParameters = TLSClientParameters()
  tlsClientParameters.keyManagers = getKeyManagers(keyTrustStore, password, keystoreType)
  tlsClientParameters.trustManagers = getTrustManagers(keyTrustStore, password, keystoreType)
  WebClient.getConfig(service).httpConduit.tlsClientParameters = tlsClientParameters
}

fun <T> create(baseURI: String, clazz: Class<T>, configurer: (T) -> Unit) : T {
  val service: T = JAXRSClientFactory.create(baseURI, clazz, getProviders())
  configurer.invoke(service)
  return service
}

fun <T> create(baseURI: String, clazz: Class<T>, duration: Long, timeUnit: TimeUnit) : T {
  return create(baseURI, clazz) { service ->
    val config = WebClient.getConfig(service)
    val client = config.httpConduit.client
    client.connectionTimeout = timeUnit.toMillis(duration)
    client.receiveTimeout = timeUnit.toMillis(duration)
  }
}

fun <T> create(baseURI: URI, clazz: Class<T>, duration: Long, timeUnit: TimeUnit) : T {
  return create(baseURI.toString(), clazz, duration, timeUnit)
}

fun <T> create(baseURI: String, clazz: Class<T>) : T {
  return create(baseURI, clazz, 60L, TimeUnit.MINUTES)
}

fun <T> createSsl(
  baseURI: String,
  clazz: Class<T>,
  keyTrustStore: InputStream,
  storePassword: String,
  keystoreType: KeystoreType,
): T {
  return create(baseURI, clazz) { service ->
    val config = WebClient.getConfig(service)
    val client = config.httpConduit.client;
    client.connectionTimeout = TimeUnit.MINUTES.toMillis(60L)
    client.receiveTimeout = TimeUnit.MINUTES.toMillis(60L)
    configureSsl(service, keyTrustStore, storePassword, keystoreType)
  }
}

fun <T> createSslAuth(
        baseURI: String,
        clazz: Class<T>,
        keyTrustStore: InputStream,
        storePassword: String,
        keystoreType: KeystoreType,
        jwtToken: String
): T {
  return create(baseURI, clazz) { service ->
    val config = WebClient.getConfig(service)
    val client = config.httpConduit.client;
    config.httpConduit.setAuthSupplier(JWTAuthSupplier(jwtToken));
    client.connectionTimeout = TimeUnit.MINUTES.toMillis(60L)
    client.receiveTimeout = TimeUnit.MINUTES.toMillis(60L)
    configureSsl(service, keyTrustStore, storePassword, keystoreType)
  }
}

fun <T> create(baseURI: URI, clazz: Class<T>): T {
  return create(baseURI.toString(), clazz)
}

fun <T> create(baseURL: URL, clazz: Class<T>): T {
  return create(baseURL.toString(), clazz)
}

class JWTAuthSupplier(private val token: String) : HttpAuthSupplier {

  override fun requiresRequestCaching(): Boolean {
    return false;
  }

  /**
   * Currently uses a simple header based Java Web Token, the actually HttpAuthSupplier
   * allows for more complicated policies and messages support oauth2 type functionality among
   * other approaches.
   */
  override fun getAuthorization(p0: AuthorizationPolicy?, p1: URI?, p2: Message?, p3: String?): String {
    return "Bearer $token";
  }

}

fun <T> createWithPasswordAuth(baseURI: URI, clazz: Class<T>, username: String, password: String) : T {
  val service: T = JAXRSClientFactory.create(baseURI.toString(), clazz, getProviders(), username, password, null)
  val config = WebClient.getConfig(service)
  val client = config.httpConduit.client
  client.connectionTimeout = TimeUnit.MINUTES.toMillis(60L)
  client.receiveTimeout = TimeUnit.MINUTES.toMillis(60L)
  return service
}


fun <T> createWithAuth(baseURI: URI, clazz: Class<T>, token: String, duration: Long, timeUnit: TimeUnit) : T {
  return createWithAuthSupplier(baseURI, clazz, duration, timeUnit, JWTAuthSupplier(token))
}

fun <T> createWithAuthSupplier(baseURI: URI, clazz: Class<T>, duration: Long, timeUnit: TimeUnit, supplier: HttpAuthSupplier) : T {
  val service: T = JAXRSClientFactory.create(baseURI.toString(), clazz, getProviders(), null)
  val config = WebClient.getConfig(service)
  config.httpConduit.setAuthSupplier(supplier);
  val client = config.httpConduit.client
  client.connectionTimeout = timeUnit.toMillis(duration)
  client.receiveTimeout = timeUnit.toMillis(duration)
  return service
}

fun <T> createWithAuth(baseURI: URI, clazz: Class<T>, token: String) : T {
  return createWithAuth(baseURI, clazz, token, 60L, TimeUnit.MINUTES);
}
