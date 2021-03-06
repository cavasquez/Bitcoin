package com.bitcoin

import akka.actor.Actor
import akka.actor.ActorRef
import javax.xml.bind.DatatypeConverter
import akka.actor.IndirectActorProducer

/**
 * Worker will perform the computation provided by Master and report any 
 * results
 */
abstract class Worker(leadingZeroes:Int = 0, prefix:String = "") extends  BitcoinActor
{
  /**
   * The receive method will receive a Message and deal with it.
   */
	override def receive =
	{
	  	case Work(start, interval) =>
	  	  {
	  	    log.debug(s"%s received work: start at $start and work $interval iterations".format(self.path))
	  	    work(start, interval, sender)
	  	  }
	  	case _ => // Do nothing for now
	}
	
	/**
	 * Calculates the hash
	 * @param input	the input that will be hashed
	 * @return		the hash of input
	 */
	def calculate(input:String):Array[Byte]
	
	/**
	 * Creates the hash input
	 * @param input	the "seed" that will be used to generate strings
	 * @param		the string of input
	 */
	def makeInput(input: Long):String
	
	/**
	 * Returns the current input
	 * @return	the string representation of the current input
	 */
	def getInput():String
	
	/**
	 * Creates the next hash input
	 * @string	the string representation of the proceeding input
	 */
	def nextInput():String
	
	/**
	 * Returns whether or not the input is valid
	 * @param input	the coin that is being checked for a particular constraint
	 * @return		whether or not this coin adheres to the defined constraints
	 */
	def goodCoin(input: Array[Byte]): Boolean
	
	/**
	 * Compute and check the validity of every hash starting with start up 
	 * until the interval
	 * @param start		the seed with which work will start
	 * @param interval	the amount of hashes that will be created and checked
	 * 					against the defined constraints
	 * @param master	the master who will receive news of a good coin
	 */
	def work(start: Long, interval: Int, master: ActorRef): Unit =
	{
	  var coin:String = null
	  var hash:Array[Byte] = null
	  coin = makeInput(start)
	  
	  for(i <- 0 until interval)
	  {
	    hash = calculate(coin)
	    if(goodCoin(hash) == true)
	    {
	      log.debug("%s found coin: %s with hash: %s".format(self.path, coin, DatatypeConverter.printHexBinary(hash)))
	      master ! Result(coin, DatatypeConverter.printHexBinary(hash))
	    }
	    coin = nextInput
	  }
	  master ! WorkDone
	}
}