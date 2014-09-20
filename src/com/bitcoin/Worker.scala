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
	
	def calculate(input: Array[Byte]):Array[Byte]
	def makeInput(input: Long):Array[Byte]
	def nextInput():Array[Byte]
	def goodCoin(input: Array[Byte]): Boolean
	
	def work(start: Long, interval: Int, master: ActorRef): Unit =
	{
	  var coin:Array[Byte] = null
	  var hash:Array[Byte] = null
	  makeInput(start)
	  
	  for(i <- 0 until interval)
	  {
	    coin = nextInput()
	    hash = calculate(coin)
	    if(goodCoin(hash)) master ! Result(coin, hash)
	  }
	  master ! WorkDone
	}
}