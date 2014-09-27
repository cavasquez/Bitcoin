package com.bitcoin

import akka.actor.Actor
import akka.actor.Props
import javax.xml.bind.DatatypeConverter
import scala.concurrent.duration._
import akka.actor.PoisonPill
import akka.actor.IndirectActorProducer

/**
 * The SuperMaster will spawn its own Master and then be connected to by other
 * Master nodes. SuperMaster will provide work to each Master.
 */
class SuperMaster(start:Int = 0, 
    masterChunkSize:Int = 1000000, 
    workerChunkSize:Int = 500,
    leadingZeroes:Int = 5,
    prefix:String = "",
    goal:Int = 10,
    timeLimit:Long = 60) extends BitcoinActor
{
  var work:Int = start
  var found = 0
  
  override def preStart() =
  {
    /* Terminates the SuperMaster and any of its dependencies in timeLimit minutes
     * (if timeLimit is not negative) */
     if(timeLimit >= 0) context.system.scheduler.scheduleOnce(FiniteDuration.apply(timeLimit, "minute"))(() => self ! PoisonPill)(context.system.dispatcher)
  }
  
  final override def receive =
  {
    case Ready => 
      {
        log.debug(s"%s received Ready from %s. Sent chunk $work".format(self.path, sender.path))
        sender ! Chunk(work, masterChunkSize, workerChunkSize)
        work += masterChunkSize
      }
    case Initialize =>
      {
        /* "Register" new master */
        log.debug("%s received Initialize from %s".format( self.path, sender.path))
        context.watch(sender)
        sender ! InitialSetup(leadingZeroes, prefix)
      }
    case Result(coin, hash) =>
      {
        log.info("%s received Result(%s, %s) from %s".format(self.path,
            new String(coin), "UTF-8"),
            DatatypeConverter.printHexBinary(hash),
            sender.path)
            
        println("Coin found: %s, %s".format(new String(coin), "UTF-8"),
            DatatypeConverter.printHexBinary(hash))
        found += 1
      }
    case _ => // Do nothing for now
  }
}