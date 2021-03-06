package com.template.spark.jobs

import com.template.spark.Main.getClass
import com.template.spark.config.{AppConfig, InputParser}
import com.template.spark.core.BaseWorker
import com.template.spark.io.IOHandler
import com.template.spark.models.JobParameters
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.count

class EmployeesExtractor(ioHandler: IOHandler, autoclose: Boolean = true) extends BaseWorker(ioHandler) {

  protected val log: Logger = Logger.getLogger(getClass.getName)

  override def run(options: JobParameters): Unit = {

    val employeesDf = ioHandler.load(options.input, options.inputFormat)

    employeesDf.groupBy("gender").agg(count("*").as("total")).show

    val spark = ioHandler.sparkSession

    if (autoclose) spark.stop
    else log.warn("Spark Session is not closed ! You should close it yourself.")

  }
}

object EmployeesExtractor extends App {

  val params = new InputParser(args).parse

  params match {
    case Some(options) =>

      val spark = SparkSession.builder()
        .appName("EmployeesWorker")
        .getOrCreate()

      spark.sparkContext.setLogLevel("WARN")
      val ioHandler = new IOHandler(spark, new AppConfig)

      new EmployeesExtractor(ioHandler).run(options)

    case _ => sys.exit(-1)
  }
}
