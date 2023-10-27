@file:JvmName(name = "Digest")
package com.languagecomputer.services.util

import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Class to encapsulate the usage of java.security.MessageDigest
 */
enum class Digest(private val algorithm: String) {
  /**
   * Representation of the possible digest (aka hash) algorithms
   * as an enum instead of by string
   */
  MD5("MD5"),
  SHA("SHA"),
  SHA1("SHA-1"),
  SHA256("SHA-256"),
  SHA512("SHA-512");

  fun md(): MessageDigest {
    return try {
      MessageDigest.getInstance(algorithm)
    } catch(e: NoSuchAlgorithmException) {
      // This exception should never happen
      throw RuntimeException(e)
    }
  }

  fun digest(toDigest: Any): ByteArray {
    return md().digest(toDigest.toString().toByteArray())
  }

  @Throws(IOException::class)
  fun digest(stream: InputStream): ByteArray {
    val md = md()
    md.reset()
    var n: Int
    val bytes = ByteArray(1024)
    while(stream.read(bytes, 0, bytes.size).also { n = it } != -1) {
      md.update(bytes, 0, n)
    }
    val digest = md.digest()
    md.reset()
    return digest
  }

  /**
   * @param toDigest the object to digest
   * @return A string with each byte in the digest as a 2-character hexadecimal representation
   */
  fun digestString(toDigest: Any): String {
    return toHexString(digest(toDigest))
  }

  @Throws(IOException::class)
  fun digestString(stream: InputStream): String {
    return toHexString(digest(stream))
  }

  companion object {
    /**
     * Convert a byte array to a string to hexadecimal digits.
     */
    fun toHexString(bytes: ByteArray): String {
      val result = StringBuilder(bytes.size * 2)
      for(i in bytes.indices) {
        val b = bytes[i].toInt()
        result.append(HEX_CHARS[b shr 4 and 0x0f])
        result.append(HEX_CHARS[b and 0x0f])
      }
      return result.toString()
    }

    private val HEX_CHARS = "0123456789abcdef".toCharArray()
  }

}

