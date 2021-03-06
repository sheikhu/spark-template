package com.template.spark.utils

import org.apache.log4j.Logger
import org.junit.rules.TemporaryFolder


trait Workdir {

  private val log: Logger = Logger.getLogger(getClass.getName)

  val workdir = new TemporaryFolder()

  def createWorkDir(): Unit = {
    workdir.create()
  }

  def dropWorkDir(): Unit = {
    if (workdir.getRoot.exists())
      workdir.delete()
    log.info(s"Directory ${workdir.getRoot.getAbsolutePath} is deleted !")
  }
}