package com.sha

import com.bitcoin.SuperMaster
import com.bitcoin.Master
import akka.actor.ActorSystem
import akka.actor.Props

/**
 * Bootstraps the program
 */
object BootStrapper extends App
{
  private var sm:SuperMaster = null
  private var master:Master[Sha256Miner] = null
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
    master = new Master(path = "akka.tcp://bitcoinsystem@" + config.masterLocation + "/user/super")
  }
}