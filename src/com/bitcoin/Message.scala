package com.bitcoin

sealed trait Message 
case object Calculate extends Message
case class Work(start: Long, interval: Int) extends Message
case class Result(coin: Array[Byte], hash: Array[Byte]) extends Message
case object Done extends Message