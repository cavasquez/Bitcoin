package com.sha

import com.bitcoin.SuperMaster
import com.bitcoin.Master

/**
 * Bootstraps the program
 */
object BootStrapper
{
  def start(config:Config):Unit =
  {
    var sm:SuperMaster = null
    var master:Master[Sha256Miner] = null
    
    if(config.superMaster)
    {
      sm = new SuperMaster(start = config.initialInput, 
          masterChunkSize = config.chunkSize, 
          workerChunkSize = config.workerLoad,
          leadingZeroes = config.leadingZeroes,
          prefix = config.prefix,
          goal = config.goal,
          timeLimit = config.timeLimit)
    }
    else
    {
      
    }
  }
}