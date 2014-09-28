package com.sha

import com.bitcoin.SuperMaster
import com.bitcoin.Master
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import scala.reflect.ClassTag

/**
 * Bootstraps the program
 */
object BootStrapper extends App
{
  val constant:String = "constant"
  private var sm:SuperMaster = null
  private var master:ActorRef = null
  private var sys:ActorSystem = null
    
  /**
   * Starts the program according to the provided configuration
   * @param config	The expected configuration with which to run this program
   */
  def start(config:Config):Unit =
  {
    sys = ActorSystem("bitcoinsystem")
    
    /* Check to see if this is the SuperMaster. If so, spin up the SuperMaster */
    if(config.superMaster == true)
    {
      sys.actorOf(Props(classOf[SuperMaster],
          config.initialInput,
          config.chunkSize,
          config.workerLoad,
          config.leadingZeroes,
          config.prefix,
          config.goal,
          config.timeLimit),
          	name = "super")
    }
    
    /* Spin up the master */
    val workerCount = Runtime.getRuntime().availableProcessors()
    val path = "akka.tcp://bitcoinsystem@%s/user/super".format(config.masterLocation )
    val ct = ClassTag(classOf[Sha256Miner])
    val m = manifest[Sha256Miner]
    println(classOf[Master[Sha256Miner]].getConstructors()(0))
    master = sys.actorOf(Props(classOf[Master[Sha256Miner]], workerCount, path, ct), name = "master")
    println("done")
  }
}