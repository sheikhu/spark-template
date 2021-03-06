package com.template.spark.config

import com.template.spark.models._
import scopt.{DefaultOParserSetup, OParserSetup, OptionParser}
class InputParser(args: Array[String]) {

  val inputFormats: Array[String] = Array("csv", "parquet", "avro", "hive", "jdbc")
  def parse: Option[JobParameters] = {

    val parser = new OptionParser[JobParameters]("scopt") {
      head("Worker")

      opt[String]('i', "input")
        .required()
        .validate(value => if (value.trim.isEmpty) failure("input should not be empty") else success)
        .action((x, c) => c.copy(input = x))
        .text("input should not be empty")

      opt[String]('o', "output")
        .required()
        .validate(value => if (value.trim.isEmpty) failure("output should not be empty") else success)
        .action((x, c) => c.copy(output = x))
        .text("output should not be empty")

      opt[String]("input-format")
        .required()
        .validate(value => if(!inputFormats.contains(value)) failure("input format is invalid") else success)
        .action((x, c) => c.copy(inputFormat = x))
        .text(s"Accepted input formats: ${inputFormats.mkString(",")}")
        .withFallback(() => "parquet")

      opt[String]("output-format")
        .required()
        .action((x, c) => c.copy(outputFormat = x))
        .withFallback(() => "csv")

      opt[Boolean]('v', "verbose")
        .optional()
        .action((x, c) => c.copy(verbose = x))

      opt[Boolean]("debug")
        .optional()
        .action((x, c) => c.copy(debug = x))
        .hidden()

      override def showUsageOnError: Option[Boolean] = Some(true)
    }

    parser.parse(args, JobParameters())
  }

}
