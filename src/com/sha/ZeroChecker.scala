package com.sha

/**
 * Checks for a certain number of leading zeroes in a given Byte array
 */
class ZeroChecker(leadingZeroes:Int = 0) 
{
  val ZERO_SIZE = 4 // half a byte
  val BYTE_SIZE = 8
  
  val checker:Array[Byte] = makeChecker(leadingZeroes)
  
  /* Validation Logic */
  if(leadingZeroes < 0) throw new IllegalArgumentException("leadingZeroes must be greater than or equal to 0")
  
  /**
   * Returns the checker used to compare against any other Array
   */
  private final def makeChecker(leadingZeroes:Int):Array[Byte] = 
  {
    var byteCount =  Math.ceil((leadingZeroes * ZERO_SIZE).toDouble/BYTE_SIZE.toDouble).toInt
    var arr:Array[Byte] = new Array[Byte](byteCount)
    var zeroesLeft = leadingZeroes
    var i = 0
    
    /* Populate arr */
    while(zeroesLeft > 0)
    {
      if(zeroesLeft > 1) arr(i) = 0x00
      else if(zeroesLeft == 1) arr(i) = 0x0F
      
      i+= 1
      zeroesLeft -= 2
    }
    return arr
  }
  
  /**
   * Checks x against arr to confirm that it has at least a certain number of
   * leading zeroes
   */
  def checkLeadingZeroes(x:Array[Byte]):Boolean = 
  {
    var answer = false
    
    if(x.length >= checker.length)
    {
      answer = true
      for(i:Int <- 0 until checker.length) if(x(i) > checker(i)) answer = false
    }
    
    return answer
  }
}