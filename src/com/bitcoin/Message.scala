package com.bitcoin

/**
 * The trait by which each actor will communicate
 */
sealed trait Message 

/**
 * Message sent to the SuperMaster by the Master telling the SuperMaster the
 * Master is ready for more work
 */
case object Ready extends Message

/**
 * Message Sent by the SuperMaster to the Master telling it the amount of work
 * it should do and how it should split its work among its children
 */
case class Chunk(start:Long, interval:Int, partitionSize:Int) extends Message

/**
 * Message sent by the Master to a Worker telling it the range of elements to
 * compute
 */
case class Work(start: Long, interval: Int) extends Message

/**
 * Message sent by the Worker to the Master or the Master to the SuperMaster
 * telling the receiver about a found coin
 */
case class Result(coin: Array[Byte], hash: Array[Byte]) extends Message

/**
 * Message sent by the SuperMaster to the Master communicating the settings it
 * should use when computing a coin
 */
case class InitialSetup(leadingZeroes:Int, prefix:String) extends Message

/**
 * Message sent by the Master to the SuperMaster asking it for an InitialSetup
 * message
 */
case object Initialize extends Message

/**
 * Message sent by the Worker to the Master communicating that it has finished
 * working
 */
case object WorkDone extends Message