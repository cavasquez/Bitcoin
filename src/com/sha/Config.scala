package com.sha

/**
 * Config class to be used with the scopt library in ShaCoin in order to 
 * provide all the config parameters for ShaCoin.
 */
case class Config(
    masterLocation:String = "localhost:6677",
    superMaster:Boolean = true,
    debug:Boolean = false,
    log:Boolean = true,
    prefix:String = "",
    initialInput:Int = 0,
    chunkSize:Int = 100000,
    workerLoad:Int = 1000,
    goal:Int = 300,
    timeLimit:Long = 60,
    leadingZeroes:Int = 5)