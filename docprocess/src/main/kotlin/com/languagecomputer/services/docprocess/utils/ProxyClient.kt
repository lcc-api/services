package com.languagecomputer.services.docprocess.utils

import com.languagecomputer.services.docprocess.CxfRawDocumentMessageBodyProvider
import com.languagecomputer.services.util.jsonProvider
import org.apache.cxf.jaxrs.client.JAXRSClientFactory
import org.apache.cxf.jaxrs.client.WebClient
import org.apache.cxf.jaxrs.provider.MultipartProvider
import java.util.concurrent.TimeUnit

class ProxyClient<T>(val uri: String, val klass: Class<T>) {
    private var providers = listOf(listOf(CxfRawDocumentMessageBodyProvider(), MultipartProvider()), jsonProvider()).flatten()
    private val proxy: T = JAXRSClientFactory.create(uri, klass, providers)
    private val config = WebClient.getConfig(proxy)
    private val conduitClient = config.httpConduit.client
    fun connectionTimeout(length: Long, timeUnit: TimeUnit) = apply { conduitClient.connectionTimeout = timeUnit.toMillis(length) }
    fun receiveTimeout(length: Long, timeUnit: TimeUnit) = apply { conduitClient.receiveTimeout = timeUnit.toMillis(length) }
    fun timeout(length: Long, timeUnit: TimeUnit) = apply {
        connectionTimeout(length, timeUnit)
        receiveTimeout(length, timeUnit)
    }
    fun withProvider(provider: Any) = apply { providers = providers + provider }
    fun make(): T = proxy
    companion object {
        @JvmStatic
        fun <T> create(uri: String, klass: Class<T>, duration: Long = 5L, timeUnit: TimeUnit = TimeUnit.MINUTES): T =
                ProxyClient(uri, klass).timeout(duration, timeUnit).make()
    }
}