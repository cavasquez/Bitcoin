package com.sha

import com.bitcoin.Worker

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
  
  def calculate(input:String): Array[Byte] = hasher.hash(input)
  
  def getInput():String = prefix + gen.getString
  
  def nextInput():String = prefix + gen.nextString
	
  def makeInput(input:Long):String =
  {
    gen = new AsciiGenerator(initial = input.toInt)
    return (prefix + gen.getString)
  }
  
  def goodCoin(input:Array[Byte]):Boolean = checker.checkLeadingZeroes(input)
}