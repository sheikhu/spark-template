package com.template.spark.io

import com.template.spark.config.AppConfig
import com.template.spark.io.reader._
import com.template.spark.io.writer._
import org.apache.spark.sql.{DataFrame, SparkSession}

class IOHandler(spark: SparkSession, appConfig: AppConfig) {

  def load(input: String, format: String, options: Map[String, String] = Map()): DataFrame = {
    format match {
      case "parquet" => loadParquet(input, mergeSchema = true, options)
      case "csv" => loadCsv(input, options)
      case "avro" => loadAvro(input, options)
      case "hive" => loadHive(table = input)
      case "jdbc" => loadJdbc(table = input)
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
      case "avro" => writeAvro(dataframe, output, saveMode, options)
      case "jdbc" => writeJdbc(
        df = dataframe,
        table = output,
        saveMode = saveMode,
        options = options)
    }
  }

  def loadCsv(input: String,
              options: Map[String, String] = Map()): DataFrame = {
    new CsvReader(spark).read(input, options)
  }

  def loadParquet(input: String, mergeSchema: Boolean = true, options: Map[String, String] = Map()): DataFrame = {
    new ParquetReader(spark).read(input, Map("mergeSchema" -> mergeSchema.toString) ++ options)
  }

  def loadHive(database: String = appConfig.get("hive.database-name"),
               table: String,
               options: Map[String, String] = Map()): DataFrame = {
    val input = if (table.contains(".")) table else s"$database.table"
    spark.read.options(options).table(input)
  }

  def loadJdbc(table: String, options: Map[String, String] = Map()): DataFrame = {
    spark.read.format("jdbc").options(options).load()
  }

  def loadAvro(input: String, options: Map[String, String] = Map()): DataFrame = {
    new AvroReader(spark).read(input, options)
  }

  def writeCsv(df: DataFrame, output: String,
                numPartitions: Int = 4,
                saveMode: String = "default",
                options: Map[String, String] = Map()): Unit = {
    new CsvWriter(saveMode, numPartitions).write(df, output, options)
  }

  def writeParquet(df: DataFrame, output: String,
                   saveMode: String = "default",
                   numPartitions: Int = 4,
                   options: Map[String, String] = Map()): Unit = {
    new ParquetWriter(saveMode, numPartitions).write(df, output, options)
  }

  def writeJdbc(df: DataFrame, schema: String = appConfig.get("jdbc.database-name"), table: String,
                saveMode: String,
                options: Map[String, String] = Map()): Unit = {

    val output = options("dbtable")

    val jdbcOptions = Map(
      "url" -> appConfig.get("jdbc.url"),
      "user" -> appConfig.get("jdbc.user"),
      "password" -> appConfig.get("jdbc.password")
    )

    new JdbcWriter(saveMode).write(df, output, jdbcOptions ++ options)
  }

  def writeHive(df: DataFrame, table: String,
                saveMode: String = "default",
                options: Map[String, String] = Map()): Unit = {
    new HiveWriter(saveMode).write(df, table, options)

  }

  def writeAvro(df: DataFrame, output: String, saveMode: String, options: Map[String, String]): Unit = {
    new AvroWriter(saveMode).write(df, output, options)
  }

  def sparkSession: SparkSession = spark

  def config: AppConfig = appConfig

}
