package com.sha

/**
 * Config class to be used with scopt library in ShaCoin
 */
case class Config(
    masterLocation:String = "localhost",
    superMaster:Boolean = true,
    debug:Boolean = false,
    log:Boolean = true,
    prefix:String = "",
    initialInput:Int = 0,
    chunkSize:Int = 100000,
    workerLoad:Int = 1000,
    goal:Int = 300,
    timeLimit:Int = 60)