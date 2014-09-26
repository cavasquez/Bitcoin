package com.sha

import com.bitcoin.Worker
import akka.actor.Actor

/**
 * A Worker that will look for SHA-256 hashes that have the provided leading 
 * zeroes. Sha256Miner will provide support for its methods using:
 * com.sha.AsciiGenerator
 * com.sha.ZeroChecker
 * com.sha.ShaHasher
 */
class Sha256Miner(leadingZeroes:Int, prefix:String) extends Worker(leadingZeroes, prefix)
{
  var gen:AsciiGenerator = null
  var checker = new ZeroChecker(leadingZeroes)
  var hasher = new ShaHasher
  
  def calculate(input:String): Array[Byte] = hasher.hash(prefix + input)
  
  def getInput():String = gen.getString
  
  def nextInput():String = gen.nextString
	
  def makeInput(input:Long):String =
  {
    gen = new AsciiGenerator(initial = input.toInt)
    return gen.getString
  }
  
  def goodCoin(input:Array[Byte]):Boolean = checker.checkLeadingZeroes(input)
  
  /* IndirectActorProducer methods */
  override def actorClass = classOf[Actor]
  
  override def produce = new Sha256Miner(leadingZeroes, prefix)
}