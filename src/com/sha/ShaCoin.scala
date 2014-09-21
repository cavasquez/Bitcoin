package com.sha

import scopt.OptionParser

object ShaCoin
{
    def main(args:Array[String])
    {
      val parser:OptionParser[Config] = new OptionParser[Config]("ShaCoin")
	  {
	    head("ShaCoin", "1.0")
	    
	    opt[String]('a', "address") action 
	    { 
	      (x,c) => c.copy(masterLocation = x) 
	    } text("Sets the address of the super-master actor.")
	    
	    opt[Boolean]('s', "super-master") action 
	    { 
	      (x,c) => c.copy(superMaster = x) 
	    } text("Designates whether this process will act as a super-master or not.")
	    
	    opt[Boolean]('d', "debug") action 
	    { 
	      (x,c) => c.copy(debug = x) 
	    } text("Sets debug mode on or off.")
	    
	    opt[Boolean]('l', "log") action
	    {
	      (x,c) => c.copy(log = x)
	    } text("Turns the logger on or off.")
	    
	    opt[String]('p', "prefix") action
	    {
	      (x,c) => c.copy(prefix = x)
	    } text("Sets the prefix that will be prefixed to the hash input.")
	    
	    opt[Int]('i', "initial") action
	    {
	      (x,c) => c.copy(initialInput = x)
	    } validate
	    {
	      x =>
	      if(x >= 0) success
	      else failure("Option ---initial must be greater than or equal to 0")
	    } text("Provides the starting point for the hash input.")
	    
	    opt[Int]('c', "chunk") action
	    {
	      (x,c) => c.copy(chunkSize = x)
	    } validate
	    {
	      x =>
	      if(x >= 0) success
	      else failure("Option ---chunk must be greater than or equal to 0")
	    } text("Sets the amount of work that each master will be given.")
	    
	    opt[Int]('l', "load") action
	    {
	      (x,c) => c.copy(workerLoad = x)
	    } validate
	    {
	      x =>
	      if(x >= 0) success
	      else failure("Option ---load must be greater than or equal to 0")
	    } text("Sets the amount of work each worker will be given.")
	    
	    opt[Int]('g', "goal") action
	    {
	      (x,c) => c.copy(goal = x)
	    } text("Sets the goal coin count for the program. Once achieved, the program will terminate." +
	        " A negative input will result in an infinite goal.")
	    
	    opt[Long]('t', "time") action
	    {
	      (x,c) => c.copy(timeLimit = x)
	    } text("Sets the max running time (in minutes) that the program should run. " +
	        " A negative input will result in an infinite time limit.")
	    
	    opt[Int]('z', "leading-zeroes") action
	    {
	      (x,c) => c.copy(leadingZeroes = x)
	    } validate
	    {
	      x =>
	      if(x >= 0) success
	      else failure("Option ---leading-zeroes must be greater than or equal to 0")
	    } text("Sets the number of leading zeroes that will be looked for in the hash of the coin.")
	  }
      
	  parser.parse(args, Config()) map
	  {
	    /* Options are good, continue */
	    config => BootStrapper.start(config)
	      
	  } getOrElse { /* Options are bad and error message has been displayed */ }
	  
   }
}