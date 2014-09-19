package com.bitcoin

sealed trait Message 
case object Ready extends Message
case class Chunk(start:Long, interval:Int, partitionSize:Int) extends Message
case class Work(start: Long, interval: Int) extends Message
case class Result(coin: Array[Byte], hash: Array[Byte]) extends Message
case class InitialSetup(leadingZeroes:Int, prefix:String) extends Message
case object Initialize extends Message
case object WorkDone extends Message