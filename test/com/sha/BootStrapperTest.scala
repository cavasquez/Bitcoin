package com.sha

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

class BootStrapperTest extends AssertionsForJUnit
{
	var config:Config = null
	
	@Before
	def initialize() =
	{
	  config = Config(masterLocation = "localhost:6677",
	    superMaster = true,
	    debug = false,
	    log = true,
	    prefix = "",
	    initialInput = 0,
	    chunkSize = 100000,
	    workerLoad = 1000,
	    goal = 300,
	    timeLimit = 60,
	    leadingZeroes= 5)
	}
	
	@Test
	def startTest() =
	{
	  BootStrapper.start(config)
	}
}