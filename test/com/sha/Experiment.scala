package com.sha

import akka.actor.ActorSystem
import com.bitcoin.SuperMaster
import akka.actor.Props
import com.bitcoin.Master
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test
import akka.actor.Actor

class Experiment extends AssertionsForJUnit
{
  @Test
  def testFun = 
  {
    val sys = ActorSystem("bitcoinsystem")
    /*
    val sm = new SuperMaster(start = 0,
        masterChunkSize = 100000, 
        workerChunkSize = 1000,
        leadingZeroes = 5,
        prefix = "test",
        goal = 10,
        timeLimit = 1)
    sys.actorOf(Props(sm), name = "super")*/
    
    sys.actorOf(Props(new SuperMaster(start = 0,
        masterChunkSize = 100000,
        workerChunkSize = 1000,
        leadingZeroes = 5,
        prefix = "test",
        goal = 10,
        timeLimit = 1)), name="super")
  
    val config = new Config(masterLocation = "localhost:6677")
    val addr = config.masterLocation 
    println(s"akka.tcp://bitcoinsystem@$addr/user/super")
    
    val test = sys.actorOf(Props(new TestActor(path = "akka.tcp://bitcoinsystem@localhost:6677/user/super")))
    println(test.path)
    println("akka.tcp://bitcoinsystem@%s/user/super".format(config.masterLocation))
  }
}

class TestActor(path:String) extends Actor
{
  val superMaster = context.actorSelection(path)
  
  override def receive = null
}