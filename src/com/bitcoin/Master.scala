package com.bitcoin

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props
import scala.reflect.ClassTag
import akka.routing.SmallestMailboxPool

/**
 * Master will delegate work to the Worker on behalf of the SuperMaster and 
 * communicate to the SuperMaster any results. It will also request more work
 * from the SuperMaster. This relationship is similar to a server-client 
 * relationship where the Master is a client and the SuperMaster is the server.
 */
class Master[T <: Worker : ClassTag](workerCount: Int = Runtime.getRuntime().availableProcessors(), path:String) extends Actor
{
  val superMaster = context.actorSelection(path)
  var workers:ActorRef = null
  var workLeft = 0
  
  final override def receive =
  {
    case Chunk(start, interval, partitionSize) => work(start, interval, partitionSize)
    case Result(coin, hash) => superMaster ! Result(coin, hash)
    case WorkDone => if(workLeft <= 0) superMaster ! Ready
    case InitialSetup(leadingZeroes, prefix) => 
      workers = context.actorOf(Props(classOf[ClassTag[T]], leadingZeroes, prefix).withRouter(SmallestMailboxPool(workerCount)), name = "workerRouter")
      //workers = context.actorOf(Props[T].withRouter(SmallestMailboxPool(workerCount)), name = "workerRouter")
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
    workLeft = interval/partitionSize
    for(i <- 0 until (interval/partitionSize)) workers ! Work(start + (i * partitionSize), partitionSize)
  }
}
