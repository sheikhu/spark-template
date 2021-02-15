package com.template.spark.io

import com.template.spark.config.AppConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class IOHandler(spark: SparkSession, appConfig: AppConfig) {

  def load(input: String, format: String, options: Map[String, String] = Map()): DataFrame = {
    format match {
      case "parquet" => loadParquet(input, mergeSchema = true, options)
      case "csv" => loadCsv(input, options)
      case "hive" => loadHive(table = input)
    }
  }

  def write(dataframe: DataFrame, output: String, format: String, saveMode: String = "default",
            parts: Int = 1, options: Map[String, String] = Map()): Unit = {

    format match {
      case "parquet" => writeParquet(
        dataframe, output, saveMode, parts, options = options)
      case "csv" => writeCsv(
        dataframe, output,
        parts,
        saveMode,
        options
      )
      case "hive" => writeHive(dataframe, output, saveMode, options)
    }
  }

  def loadCsv(filename: String,
              options: Map[String, String] = Map()): DataFrame = {
    spark.read
      .options(options)
      .csv(filename)
  }

  def loadParquet(input: String, mergeSchema: Boolean = true, options: Map[String, String] = Map()): DataFrame = {
    spark.read
      .option("mergeSchema", mergeSchema)
      .options(options)
      .parquet(input)
  }

  def loadHive(database: String = appConfig.get("jdbc.database-name"),
               table: String,
               options: Map[String, String] = Map()): DataFrame = {
    val input = if (table.contains(".")) table else s"$database.table"
    spark.read.options(options).table(input)
  }


  def writeCsv(df: DataFrame, output: String,
                numPartitions: Int = 4,
                saveMode: String = "default",
                options: Map[String, String] = Map()): Unit = {
    df.coalesce(numPartitions)
      .write
      .mode(saveMode)
      .options(options)
      .csv(output)
  }

  def writeParquet(df: DataFrame, filename: String,
                   saveMode: String = "default",
                   numPartitions: Int = 4,
                   options: Map[String, String] = Map()): Unit = {
    df.coalesce(numPartitions)
      .write
      .mode(saveMode)
      .options(options)
      .parquet(filename)
  }

  def writeJdbc(df: DataFrame, schema: String = appConfig.get("jdbc.database-name"), table: String,
                options: Map[String, String] = Map()): Unit = {
    df.write.option("url", appConfig.get("jdbc.url"))
      .option("user", appConfig.get("jdbc.user"))
      .option("password", appConfig.get("jdbc.password"))
      .option("dbtable", s"$schema.$table")
      .options(options)
  }

  def writeHive(df: DataFrame, table: String,
                saveMode: String = "default",
                options: Map[String, String] = Map()): Unit = {
    df.write
      .mode(saveMode)
      .options(options)
      .saveAsTable(s"$table")
  }

  def sparkSession: SparkSession = spark

  def config: AppConfig = appConfig

}
