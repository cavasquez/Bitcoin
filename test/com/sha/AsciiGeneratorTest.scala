package com.sha

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

class AsciiGeneratorTest extends AssertionsForJUnit
{
	var test:AsciiGenerator = null
	
	@Before
	def initialize() =
	{
	  test = new AsciiGenerator()
	}
	
	@Test
	def constructorTest() =
	{
	  assertEquals("!", test.getString)
	  test = new AsciiGenerator(5)
	  assertEquals("&", test.getString)
	  test = new AsciiGenerator(92)
	  assertEquals("}", test.getString)
	  test = new AsciiGenerator(91)
	  assertEquals("|", test.getString)
	  test = new AsciiGenerator(93)
	  assertEquals("~", test.getString)
	  test = new AsciiGenerator(94)
	  assertEquals("!!", test.getString)
	}
	
	@Test
	def nextStringTest()
	{	  
	  test = new AsciiGenerator(0, 97, 100)
	  assertEquals("a", test.getString)
	  assertEquals("b", test.nextString)
	  assertEquals("c", test.nextString)
	  assertEquals("d", test.nextString)
	  
	  assertEquals("aa", test.nextString)
	  assertEquals("ab", test.nextString)
	  assertEquals("ac", test.nextString)
	  assertEquals("ad", test.nextString)
	  
	  assertEquals("ba", test.nextString)
	  assertEquals("bb", test.nextString)
	  assertEquals("bc", test.nextString)
	  assertEquals("bd", test.nextString)
	  
	  assertEquals("ca", test.nextString)
	  assertEquals("cb", test.nextString)
	  assertEquals("cc", test.nextString)
	  assertEquals("cd", test.nextString)
	  
	  assertEquals("da", test.nextString)
	  assertEquals("db", test.nextString)
	  assertEquals("dc", test.nextString)
	  assertEquals("dd", test.nextString)
	  
	  assertEquals("aaa", test.nextString)
	  assertEquals("aab", test.nextString)
	  assertEquals("aac", test.nextString)
	  assertEquals("aad", test.nextString)
	  
	  assertEquals("aba", test.nextString)
	  assertEquals("abb", test.nextString)
	  assertEquals("abc", test.nextString)
	  assertEquals("abd", test.nextString)
	  
	  assertEquals("aca", test.nextString)
	  assertEquals("acb", test.nextString)
	  assertEquals("acc", test.nextString)
	  assertEquals("acd", test.nextString)
	  
	  assertEquals("ada", test.nextString)
	  assertEquals("adb", test.nextString)
	  assertEquals("adc", test.nextString)
	  assertEquals("add", test.nextString)
	  
	}
}