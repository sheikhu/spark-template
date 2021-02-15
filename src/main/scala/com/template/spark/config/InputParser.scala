package com.template.spark.config

import com.template.spark.models._
import scopt.{DefaultOParserSetup, OParserSetup, OptionParser}
class InputParser(args: Array[String]) {

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

      opt[String]('f', "format")
        .required()
        .action((x, c) => c.copy(format = x))
        .withFallback(() => "csv")

      override def showUsageOnError: Option[Boolean] = Some(true)
    }

    parser.parse(args, JobParameters())
  }

}
