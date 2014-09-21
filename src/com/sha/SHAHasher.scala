package com.sha

import java.security.MessageDigest

/**
 * SHAHasher will hash some input and provide the SHA-256 hash of the provided
 * input
 */
class SHAHasher 
{
  val sha = MessageDigest.getInstance("SHA-256")
  
  /**
   * Computes the SHA-256 hash of input
   */
  def hash(input:String): Array[Byte] = sha.digest(input.getBytes)
}