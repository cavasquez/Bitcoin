package com.bitcoin

import akka.actor.Actor
import akka.actor.ActorRef

abstract class Worker(zeroes: Int, prefix: String) extends Actor
{
  /**
   * The receive method will receive a Message and deal with it.
   */
	override def receive =
	{
	  	case Work(start, interval) => work(start, interval, sender)
	  	case _ => // Do nothing for now
	}
	
	def calculate(input: Array[Byte]): Array[Byte]
	def makeInput(input: Array[Byte]): Array[Byte]
	def makeInput(input: Long): Array[Byte]
	def goodCoin(input: Array[Byte]): Boolean
	
	def work(start: Long, interval: Int, receiver: ActorRef): Unit =
	{
	  var coin:Array[Byte] = null
	  var hash:Array[Byte] = null
	  for(i <- 0 to interval)
	  {
	    coin = makeInput(start + i)
	    hash = calculate(coin)
	    if(goodCoin(hash)) receiver ! Result(coin, hash)
	  }
	}
}