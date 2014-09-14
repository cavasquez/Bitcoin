package com.bitcoin

sealed trait Message 
case object Calculate extends Message
case class Work(start: Int, nrOfElements: Int) extends Message
case class Result(value: Double) extends Message
case object Done extends Message