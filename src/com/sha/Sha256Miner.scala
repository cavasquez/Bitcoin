package com.sha

import com.bitcoin.Worker

/**
 * A Worker that will look for SHA-256 hashes that have the provided leading 
 * zeroes
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
}