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
  //private var master:Master[Sha256Miner] = null
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
    if(config.superMaster)
    {
      sm = new SuperMaster(start = config.initialInput, 
          masterChunkSize = config.chunkSize, 
          workerChunkSize = config.workerLoad,
          leadingZeroes = config.leadingZeroes,
          prefix = config.prefix,
          goal = config.goal,
          timeLimit = config.timeLimit)
      sys.actorOf(Props(sm), name = "super")
    }
    
    /* Spin up the masters */
    val workerCount = Runtime.getRuntime().availableProcessors()
    val path = "akka.tcp://bitcoinsystem@%s/user/super".format(config.masterLocation )
    val ct = ClassTag(classOf[Sha256Miner])
    master = sys.actorOf(Props(classOf[Master[Sha256Miner]], workerCount, path, ct), name = "master")
    //master = new Master(path = "akka.tcp://bitcoinsystem@%s/user/super".format(config.masterLocation ))
  }
}