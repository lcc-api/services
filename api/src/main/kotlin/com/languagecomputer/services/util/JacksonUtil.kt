@file:JvmName(name = "JacksonUtil")
package com.languagecomputer.services.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paranamer.ParanamerModule
import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type
import javax.ws.rs.core.GenericType

object Instance {
  val mapper = objectMapper()
}

fun objectMapper() : ObjectMapper {
  @Suppress("DEPRECATION")
  return ObjectMapper()
          .registerModule(JavaTimeModule())
          .registerModule(KotlinModule.Builder().configure(KotlinFeature.NullIsSameAsDefault, true).build())
          .registerModule(ParanamerModule())
          .registerModule(GuavaModule())
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
          //.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
          // This is not available using other methods, so this deprecated code is necessary for this version of jackson
          .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
          .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
          .setSerializationInclusion(JsonInclude.Include.ALWAYS)
}

fun jsonProvider() : List<JacksonJaxbJsonProvider> {
  return listOf(JacksonJaxbJsonProvider(objectMapper(), JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS))
}

fun <T> readValue(value: String?, clazz: Class<T>) : T? {
  if (value == null) {
    return null
  }
  return Instance.mapper.readValue(value, clazz)
}

fun toPrettyString(obj: Any?) : String? {
  if (obj == null) {
    return null
  }
  return Instance.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
}

fun toString(obj: Any?) : String? {
  if (obj == null) {
    return null
  }
  return Instance.mapper.writeValueAsString(obj)
}

fun <T> fromJson(value: String?, type: TypeReference<T>) : T? {
  if (value == null) {
    return null
  }
  return Instance.mapper.readValue(value, type)
}

fun <T> fromJson(value: String?, clazz: Class<T>) : T? {
  return readValue(value, clazz)
}

fun <T> fromJson(value: String?, type: Type) : T? {
  if (value == null) {
    return null
  }
  val javaType = Instance.mapper.constructType(type)
  return Instance.mapper.readValue(value, javaType)
}

fun <T> fromJson(value: String?, type: GenericType<T>) : T? {
  if (value == null) {
    return null
  }
  val javaType = Instance.mapper.constructType(type.type)
  return Instance.mapper.readValue(value, javaType)
}

fun <T> fromJson(value: Reader, type: Class<T>) : T {
  return Instance.mapper.readValue(value, type)
}

fun <T> fromJson(value: Reader, type: Type) : T {
  val javaType = Instance.mapper.constructType(type)
  return Instance.mapper.readValue(value, javaType)
}

fun <T> fromJson(value: Reader, type: GenericType<T>) : T {
  val javaType = Instance.mapper.constructType(type.type)
  return Instance.mapper.readValue(value, javaType)
}

fun <T> fromJson(value: Reader, type: TypeReference<T>) : T {
  return Instance.mapper.readValue(value, type)
}

fun fromJson(string: String?) : JsonNode? {
  if (string == null) {
    return null
  }
  return Instance.mapper.readTree(string)
}

fun toJson(source: Any): String? {
  return toJson(source, true)
}

// method previously always threw if null, so changed signature to only accept non-null values
fun toJson(source: Any, prettyPrint: Boolean): String? {
  return if (prettyPrint) {
    toPrettyString(source)
  } else toString(source)
}

fun toJson(source: Any?, writer: Writer?) {
  toJson(source, writer, true)
}

fun toJson(source: Any?, writer: Writer?, prettyPrint: Boolean) {
  if(prettyPrint) {
    Instance.mapper.writerWithDefaultPrettyPrinter().writeValue(writer, source)
  } else {
    Instance.mapper.writeValue(writer, source)
  }
}
