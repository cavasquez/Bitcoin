package com.bitcoin

import akka.actor.Actor
import akka.actor.ActorRef

abstract class Worker(leadingZeroes:Int = 0, prefix:String = "") extends Actor
{
  /**
   * The receive method will receive a Message and deal with it.
   */
	override def receive =
	{
	  	case Work(start, interval) => work(start, interval, sender)
	  	case _ => // Do nothing for now
	}
	
	/**
	 * Calculates the hash
	 */
	def calculate(input:String):Array[Byte]
	
	/**
	 * Creates the hash input
	 */
	def makeInput(input: Long):String
	
	/**
	 * Returns the current input
	 */
	def getInput():String
	
	/**
	 * Creates the next hash input
	 */
	def nextInput():String
	
	/**
	 * Returns whether or not the input is valid
	 */
	def goodCoin(input: Array[Byte]): Boolean
	
	/**
	 * Compute and check the validity of every hash starting with start up 
	 * until the interval
	 */
	def work(start: Long, interval: Int, master: ActorRef): Unit =
	{
	  var coin:String = null
	  var hash:Array[Byte] = null
	  coin = makeInput(start)
	  
	  for(i <- 0 until interval)
	  {
	    hash = calculate(coin)
	    if(goodCoin(hash)) master ! Result(coin.getBytes(), hash)
	    coin = nextInput
	  }
	  master ! WorkDone
	}
}