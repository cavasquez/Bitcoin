package com.bitcoin

import akka.actor.Actor

class SuperMaster(start:Int = 0, 
    masterChunkSize:Int = 1000000, 
    workerChunkSize:Int = 500,
    leadingZeroes:Int = 5,
    prefix:String = "",
    goal:Int = 10,
    timeLimit:Int = 60) extends Actor
{
  var work:Int = start
  var found = 0
  
  final override def receive =
  {
    case Ready => 
      {
        sender ! Chunk(work, masterChunkSize, workerChunkSize)
        work += masterChunkSize
      }
    case Initialize =>
      {
        context.watch(sender)
        sender ! InitialSetup(leadingZeroes, prefix)
      }
    case Result(coin, hash) => // implement
    case _ => // Do nothing for now
  }
}