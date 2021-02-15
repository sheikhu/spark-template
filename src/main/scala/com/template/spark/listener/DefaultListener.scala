package com.template.spark.listener

import java.time.{Duration, LocalDate, LocalDateTime}

import org.apache.log4j.Logger
import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd, SparkListenerApplicationStart, SparkListenerJobEnd, SparkListenerJobStart}

class DefaultListener extends SparkListener() {

  protected var startTime: LocalDateTime = _
  protected var duration: Long = 0L

  private val log = Logger.getLogger(getClass)


  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {
    startTime = LocalDateTime.now()
    log.info("Job started at " + startTime.toString)
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    log.info("Job ended at " + LocalDateTime.now().toString)
    log.info(s"Job Duration : ${Duration.between(startTime, LocalDateTime.now()).abs().getSeconds} seconds")

  }

  def getDuration: Long = {
    duration
  }
}
