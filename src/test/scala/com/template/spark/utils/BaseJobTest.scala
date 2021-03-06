package com.template.spark.utils

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.log4j.Logger
import org.junit.rules.TemporaryFolder
import org.scalatest.{BeforeAndAfterAll, FunSuite}

abstract class BaseJobTest extends FunSuite with DataFrameSuiteBase with BeforeAndAfterAll with Workdir {

  protected val log: Logger = Logger.getLogger(getClass.getName)

  override protected implicit def enableHiveSupport: Boolean = false


  override def beforeAll(): Unit = {
    super.beforeAll()
    spark.sparkContext.setLogLevel("WARN")

    // Create test workdir
    createWorkDir()
    log.info(s"Directory ${workdir.getRoot.getAbsolutePath} is created !")
  }


  override def afterAll(): Unit = {
    super.afterAll()
    // Delete test workdir
    dropWorkDir()

  }

}
