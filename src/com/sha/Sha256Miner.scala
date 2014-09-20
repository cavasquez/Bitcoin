package com.sha

import com.bitcoin.Worker

/**
 * A Worker that will look for SHA-256 hashes that have the provided leading 
 * zeroes
 */
class Sha256Miner(leadingZeroes:Int, prefix:String) extends Worker(leadingZeroes, prefix)
{
  var gen:AsciiGenerator = null
  
  def calculate(input: Array[Byte]): Array[Byte] =
  {
    null
  }
  
  def nextInput():Array[Byte] =
  {
    null
  }
	
  def makeInput(input:Long):Array[Byte] =
  {
    null
  }
  
  def goodCoin(input:Array[Byte]):Boolean =
  {
    false
  }		
}