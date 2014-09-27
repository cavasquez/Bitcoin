package com.sha

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.Identify
import com.typesafe.config.ConfigFactory
import com.bitcoin.Master
import scala.reflect.ClassTag
import scala.concurrent.duration.{FiniteDuration, Duration}
import akka.actor.Scheduler
import java.util.concurrent.TimeUnit

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
	    goal = 10,
	    timeLimit = 1,
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
  {/*
    context.system.scheduler.schedule(
        Duration.create(500, TimeUnit.MILLISECONDS),
        self,
        "time",
        context.dispatcher,
        null)*/
    context.system.scheduler.scheduleOnce(FiniteDuration.apply(5, "second"))(() => self ! "time")(context.system.dispatcher)
    //if(timeLimit >= 0) context.system.scheduler.scheduleOnce(FiniteDuration.apply(timeLimit, "minute"))(() => self ! PoisonPill)(context.system.dispatcher)
  }
  
  def receive = 
  {
    case "time" => println("got time message")
    case _ =>
      val remote = context.actorSelection("akka.tcp://testsystem@localhost:6677/user/server")
      println("client got path %s".format(remote.pathString))
      remote ! Identify
      //remote ! "hey"
  }
}