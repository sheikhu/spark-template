package com.socgen.bigban.bluebook_pid.io

import com.socgen.bigban.config.AppConfig
import com.socgen.bigban.utils._
import com.socgen.bigban.parsers.cobalt.tables.CobaltOperation
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class IOHandler(spark: SparkSession, appConfig: AppConfig) {

  def load(input: String, format: String, options: Map[String, String] = Map()): DataFrame = {
    format match {
      case "parquet" => loadParquet(input, mergeSchema = true, options)
      case "csv" => loadCsv(input, header = true, delimiter = ";", options)
      case "hive" => loadHive(table = input)
    }
  }

  def write(dataframe: DataFrame, output: String, format: String, saveMode: String = "default",
            parts: Int = 1,  options: Map[String, String] = Map()): Unit = {

    format match {
      case "parquet" => writeParquet(
        dataframe, output, saveMode, parts, options = options)
      case "csv" => writeCsv(
        dataframe, output,
        options.getOrElse("header", "true").toBoolean,
        options.getOrElse("delimiter", ";"),
        parts,
        saveMode,
        options
      )
      case "hive" => writeHive(dataframe,output, saveMode, options)
    }
  }

  def loadCsv(filename: String, header: Boolean = true, delimiter: String = ";",
              options: Map[String, String] = Map()): DataFrame = {
    spark.read
      .option("header", header)
      .option("delimiter", delimiter)
      .options(options)
      .csv(filename)
  }

  def loadParquet(input: String, mergeSchema: Boolean = true, options: Map[String, String] = Map()): DataFrame = {
    spark.read
      .option("mergeSchema", mergeSchema)
      .options(options)
      .parquet(input)
  }

  def loadHive(database: String = appConfig.get("jdbc.datababase-name"), table: String, options: Map[String, String] = Map()): DataFrame = {
    val input = if(table.contains(".")) table else s"$database.table"
    spark.read.options(options).table(input)
  }

  def cobaltOpe(input: String, tables: Seq[String] = Seq("*")): RDD[CobaltOperation] = {
    spark.sparkContext.cobalt(input, tables)
  }

  def writeCsv(df: DataFrame, output: String, header: Boolean = true, delimiter: String = ";",
               numPartitions: Int = 4, saveMode: String = "default", options: Map[String, String] = Map()): Unit = {
    df.coalesce(numPartitions)
      .write
      .option("header", header)
      .option("delimiter", delimiter)
      .options(options)
      .mode(saveMode)
      .csv(output)
  }

  def writeParquet(df: DataFrame, filename: String, saveMode: String = "default", numPartitions: Int = 4,
                   options: Map[String, String] = Map()): Unit = {
    df.coalesce(numPartitions)
      .write
      .mode(saveMode)
      .options(options)
      .parquet(filename)
  }

  def writeJdbc(df: DataFrame, schema: String = appConfig.get("jdbc.datababase-name"), table: String,
           options: Map[String, String] = Map()): Unit = {
    df.write.option("url", appConfig.get("jdbc.url"))
      .option("user", appConfig.get("jdbc.user"))
      .option("password", appConfig.get("jdbc.password"))
      .option("dbtable", s"$schema.$table")
      .options(options)
  }

  def writeHive(df: DataFrame, table: String, saveMode: String = "default", options: Map[String, String] = Map()): Unit = {
    df.write
      .mode(saveMode)
      .options(options)
      .saveAsTable(s"$table")
  }
  def sparkSession: SparkSession = spark

  def config: AppConfig = appConfig

}
