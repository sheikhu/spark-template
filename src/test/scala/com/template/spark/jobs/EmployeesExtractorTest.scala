package com.template.spark.jobs

import com.template.spark.config.AppConfig
import com.template.spark.io.IOHandler
import com.template.spark.models.JobParameters
import com.template.spark.utils.BaseJobTest
import org.scalatest.Matchers

class EmployeesExtractorTest extends BaseJobTest with Matchers {


  test("Test EmployeesExtractor worker pipeline") {
    val output = workdir.newFolder("output")

    val options = JobParameters(
      input = "src/main/resources/datasets/employees.csv",
      output = output.getAbsolutePath
    )

    val ioHandler = new IOHandler(spark, new AppConfig)
    new EmployeesExtractor(ioHandler, autoclose = false).run(options)

    output.exists() shouldBe true
  }
}
