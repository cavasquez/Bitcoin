package com.bitcoin

import akka.actor.Actor
import akka.actor.Props
import javax.xml.bind.DatatypeConverter
import scala.concurrent.duration._
import akka.actor.PoisonPill

class SuperMaster(start:Int = 0, 
    masterChunkSize:Int = 1000000, 
    workerChunkSize:Int = 500,
    leadingZeroes:Int = 5,
    prefix:String = "",
    goal:Int = 10,
    timeLimit:Long = 60) extends Actor
{
  var work:Int = start
  var found = 0
  
  if(timeLimit >= 0) context.system.scheduler.scheduleOnce(FiniteDuration.apply(timeLimit, "minute"))(() => self ! PoisonPill)(context.system.dispatcher)
  
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
    case Result(coin, hash) =>
      {
        println("Coin found: " +  (new String(coin), "UTF-8") +  " " +
            DatatypeConverter.printHexBinary(hash))
        found += 1
      }
    case _ => // Do nothing for now
  }
}