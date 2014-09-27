package com.sha

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.Identify
import com.typesafe.config.ConfigFactory
import com.bitcoin.Master
import scala.reflect.ClassTag

object Experiment 
{
  def main(args: Array[String]) =
  {
    this.test1
  }
  
  def test1() =
  {
    val config = Config(masterLocation = "localhost:6677",
	    superMaster = true,
	    debug = false,
	    log = true,
	    prefix = "test",
	    initialInput = 0,
	    chunkSize = 100000,
	    workerLoad = 1000,
	    goal = 300,
	    timeLimit = 60,
	    leadingZeroes= 5)
	    
	    BootStrapper.start(config)
  }
  
  def test2() =
	{
	  println("start")
	  
      var config = ConfigFactory.parseString("""
    akka {
       actor {
           provider = "akka.remote.RemoteActorRefProvider"
             }
       remote {
           transport = ["akka.remote.netty.tcp"]
       netty.tcp {
           hostname = "localhost"
           port = 6677
                 }
             }
        }
   """)
	  
	  //config = ConfigFactory.load()
      //config = ConfigFactory.load
      //var sys = ActorSystem("testsystem", config)
	  var sys = ActorSystem("testsystem")
	  
	  val server = sys.actorOf(Props(classOf[Server], "hey", "jude"), name = "server")
	  println(server.path)
	  server ! "hey"
	  
	  val remote = sys.actorSelection(s"akka.tcp://testsystem@localhost:6677/user/server")
	  //sys = ActorSystem("clientsystem")
	  val client = sys.actorOf(Props[Client], name = "client")
	  client ! Identify
	  
	  val workerCount = Runtime.getRuntime().availableProcessors()
      val path = "akka.tcp://testsystem@localhost:6677/user/server"
      val ct = ClassTag(classOf[Sha256Miner])
      println(classOf[Master[Sha256Miner]].getConstructors()(0).toString())
	  val test = sys.actorOf(Props(classOf[Master[Sha256Miner]], workerCount, path, ct), name = "master")
	  test ! Identify
	}
}

class Server(test1:String = "test1", test2:String = "test2") extends Actor
{
  override def preStart =
  {
    println(test1)
    println(test2)
  }
  
  def receive =
  {
    case "hey" => println("got hey from %s".format(sender))
    case Identify => println("Identifying %s".format(sender.path))
    case _ => println("got something from %s".format(sender))
  }
}

class Client() extends Actor
{
  override def preStart = 
  {
    
  }
  
  def receive = 
  {
    case _ =>
      val remote = context.actorSelection("akka.tcp://testsystem@localhost:6677/user/server")
      println("client got path %s".format(remote.pathString))
      remote ! Identify
      //remote ! "hey"
  }
}