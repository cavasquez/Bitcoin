package com.bitcoin

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props
import scala.reflect.ClassTag
import akka.routing.SmallestMailboxPool

class Master[T <: Worker : ClassTag](workerCount: Int = Runtime.getRuntime().availableProcessors()) extends Actor
{
  val superMaster = context.actorSelection("akka.tcp://")
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
  
  def work(start:Long, interval:Int, partitionSize:Int): Unit =
  {
    workLeft = interval/partitionSize
    for(i <- 0 until (interval/partitionSize)) workers ! Work(start + (i * partitionSize), partitionSize)
  }
}
