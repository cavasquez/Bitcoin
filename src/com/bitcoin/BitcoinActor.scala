package com.bitcoin

import akka.actor.Actor
import akka.event.Logging

/**
 * Base class on which all other actors in com.bitcoin will be built off of
 */
abstract class BitcoinActor extends Actor
{
  val log = Logging.getLogger(context.system, this)
  
  override def preStart():Unit =
  {
    log.debug("[{}] is starting", self.path)
  }
  
  override def preRestart(reason:Throwable, message:Option[Any]):Unit =
  {
    log.error(reason, "Restarting due to [{}] when processing [{}]", 
        reason.getMessage(), message.getOrElse(""))
    
  }
  
  override def postRestart(reason:Throwable):Unit =
  {
    
  }
  
  override def postStop() = 
  {
    log.debug("[{}] is stopping", self.path)
  }
  
}