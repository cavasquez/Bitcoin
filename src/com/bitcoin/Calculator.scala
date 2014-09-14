package com.bitcoin

/**
 * Calculator is an abstract object that will be extended to provide the kind
 * of calculation that a worker should do. 
 */
abstract class Calculator 
{
  /**
   * 
   */
  def calculate(input: Array[Byte]): Array[Byte]
}