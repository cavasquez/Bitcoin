package com.sha

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

class ZeroCheckterTest extends AssertionsForJUnit 
{
  var test:ZeroChecker = null
  @Before
  def initialize() = 
  {
    
  }
  
  @Test
  def constructorTest =
  {
    test = new ZeroChecker(5)
    assertEquals(0x00.toByte, test.checker(0))
    assertEquals(0x00.toByte, test.checker(1).toByte)
    assertEquals(0x0F.toByte, test.checker(2).toByte)
    assertEquals(3, test.checker .length)
    
    test = new ZeroChecker(0)
    assertEquals(0, test.checker.length)
  }
  
  @Test
  def checkLeadingZeroesTest =
  {
    test = new ZeroChecker(5)
    var testee:Array[Byte] = new Array[Byte](5)
    testee(0) = 0x00.toByte
    testee(1) = 0x00.toByte
    testee(2) = 0x03.toByte
    testee(3) = 0x45.toByte
    testee(4) = 0xF0.toByte
    
    assertTrue(test.checkLeadingZeroes(testee))
    
    testee = new Array[Byte](5)
    testee(0) = 0x01.toByte
    testee(1) = 0x00.toByte
    testee(2) = 0x03.toByte
    testee(3) = 0x45.toByte
    testee(4) = 0xF0.toByte
    
    assertFalse(test.checkLeadingZeroes(testee))
    
    testee = new Array[Byte](2)
    testee(0) = 0x01.toByte
    testee(1) = 0x00.toByte
    
    assertFalse(test.checkLeadingZeroes(testee))
  }
}