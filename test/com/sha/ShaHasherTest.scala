package com.sha

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import javax.xml.bind.DatatypeConverter
import com.sha.ShaHasher

/**
 * Tests com.bitcoin.ShaHasher
 */
class ShaHasherTest extends AssertionsForJUnit 
{
  var test:ShaHasher = null
  @Before
  def initialize() = 
  {
    test = new ShaHasher
  }
  
  @Test
  def hashTest =
  {
    assertEquals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08".toUpperCase(), DatatypeConverter.printHexBinary(test.hash("test")))
    assertEquals("70158f9055af67cb94c0175b73624a1b198135aeab541cc06c05da81452009a8".toUpperCase(), DatatypeConverter.printHexBinary(test.hash("rawr")))
    assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".toUpperCase(), DatatypeConverter.printHexBinary(test.hash("")))
  }
}