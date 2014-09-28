package com.bitcoin

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props
import scala.reflect.ClassTag
import akka.routing.SmallestMailboxPool
import akka.actor.IndirectActorProducer
import akka.actor.ActorSelection
import javax.xml.bind.DatatypeConverter

/**
 * Master will delegate work to the Worker on behalf of the SuperMaster and 
 * communicate to the SuperMaster any results. It will also request more work
 * from the SuperMaster. This relationship is similar to a server-client 
 * relationship where the Master is a client and the SuperMaster is the server.
 * 
 * Note that the Master will not wait until workLeft reaches 0 to request more 
 * work. Master will check to see if the work left is less than or equal to the
 * number of workers left times the workCoefficient in order to request more 
 * work. This is done to keep the workers busy. 
 */
class Master[T <: Worker : ClassTag](workerCount: Int = Runtime.getRuntime().availableProcessors(), path:String) extends BitcoinActor
{
  var superMaster:ActorSelection = null
  var workers:ActorRef = null
  var workLeft = 0
  val workCoefficient = 5
  
  override def preStart = 
  {
    superMaster = context.actorSelection(path)
    
    /* Send SuperMaster Initialize message */
    superMaster ! Initialize
    log.debug("sent %s InitialiZe".format(superMaster.pathString))
  }
  
  final override def receive =
  {
    case Chunk(start, interval, partitionSize) =>
      {
        log.debug(s"%s received Chunk($start, $interval, $partitionSize) from %s".format(self.path, sender.path))
        work(start, interval, partitionSize)
      }
    case Result(coin, hash) =>
      {
        log.info("%s received Result(%s, %s) from %s".format(self.path, coin, hash, sender.path))
        superMaster ! Result(coin, hash)
      }
    case WorkDone => 
      {
        log.debug(s"%s received WorkDone from %s. Work remaining: $workLeft".format(self.path, sender.path))
        workLeft -= 1
        if(workLeft <= (workCoefficient * workerCount)) superMaster ! Ready 
      }
    case InitialSetup(leadingZeroes, prefix) => 
      {
        log.debug(s"%s received InitialSetup($leadingZeroes, $prefix) from %s.".format(self.path, sender.path))
        log.debug("%s creating %s".format(self.path, implicitly[ClassTag[T]].runtimeClass))
        workers = context.actorOf(Props(implicitly[ClassTag[T]].runtimeClass, leadingZeroes, prefix).withRouter(SmallestMailboxPool(workerCount)), name = "workerRouter")
        superMaster ! Ready
      }
    case _ => // Do nothing for now
  }
  
  /**
   * Does the partitionSize amount of work with start as a seed. The work will 
   * be split into intervals that will be given to the workers.
   * @param	start			the "seed" at which computation will start
   * @param interval		the amount of work each Worker will perform	
   * @param partitionSize	the amount of work the Master will perform
   */
  def work(start:Long, interval:Int, partitionSize:Int): Unit =
  {
    workLeft += interval/partitionSize
    for(i <- 0 until (interval/partitionSize)) workers ! Work(start + (i * partitionSize), partitionSize)
  }
}
