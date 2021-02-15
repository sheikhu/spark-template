package com.template.spark

import com.template.spark.config.{AppConfig, InputParser}
import com.template.spark.io.IOHandler
import com.template.spark.listener.DefaultListener
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import com.template.spark.models._

object Main extends App {

  val parametersOption = new InputParser(args).parse

  if (parametersOption.isEmpty) sys.exit(-1)

  val spark = SparkSession
    .builder()
    .getOrCreate()

  import spark.implicits._

  val options = parametersOption.get

  val ioHandler = new IOHandler(spark, new AppConfig)

  val employeesDf = ioHandler.loadCsv(
    options.input,
    Map(
      "header" -> "true",
      "delimiter" -> ";"
    )
  )
  employeesDf.groupBy("gender").agg(count("*").as("total")).show

  spark.stop
}
