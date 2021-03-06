package com.template.spark.io

import org.apache.spark.sql.{DataFrame, SparkSession}

package object reader {


  abstract class Reader(sparkSession: SparkSession ) {
    def read(input: String, options: Map[String, String] = Map()): DataFrame
    def spark: SparkSession = sparkSession
  }


  class CsvReader(spark: SparkSession) extends Reader(spark) {
    override def read(input: String, options: Map[String, String]): DataFrame = {
      val defaultOptions = Map(
        "header" -> "true",
        "delimiter" -> ";"
      )
      spark.read.options(defaultOptions ++ options).csv(input)
    }
  }

  class ParquetReader(spark: SparkSession) extends Reader(spark) {
    override def read(input: String, options: Map[String, String]): DataFrame = {
      spark.read.options(options).parquet(input)
    }
  }
  class AvroReader(spark: SparkSession) extends Reader(spark) {
    override def read(input: String, options: Map[String, String]): DataFrame = {
      spark.read.format("avro").options(options).load(input)
    }
  }
  class HiveReader(spark: SparkSession) extends Reader(spark) {
    override def read(input: String, options: Map[String, String]): DataFrame = {
      spark.read.options(options).table(input)
    }
  }
  class JdbcReader(spark: SparkSession) extends Reader(spark) {
    override def read(input: String, options: Map[String, String]): DataFrame = {
      spark.read.format("jdbc").options(options).load(input)
    }
  }
}
