package com.template.spark.io

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

package object writer {
  trait Writer {
    def write(dataframe: DataFrame, output: String, options: Map[String, String] = Map())
  }

  class CsvWriter(saveMode: String, numPartitions: Int) extends Writer {
    override def write(dataframe: DataFrame, output: String, options: Map[String, String]): Unit = {
      dataframe
        .coalesce(numPartitions)
        .write
        .mode(saveMode)
        .options(options)
        .csv(output)
    }
  }

  class ParquetWriter(saveMode: String, numPartitions: Int) extends Writer {
    override def write(dataframe: DataFrame, output: String, options: Map[String, String]): Unit = {
      dataframe
        .coalesce(numPartitions)
        .write
        .mode(saveMode)
        .options(options)
        .parquet(output)
    }
  }

  class JdbcWriter(saveMode: String, connectionProperties: Properties) extends Writer {
    override def write(dataframe: DataFrame, output: String, options: Map[String, String]): Unit = {
      dataframe
        .write
        .mode(saveMode)
        .options(options)
        .jdbc(options("url"), output, connectionProperties)
    }
  }

  class HiveWriter(saveMode: String) extends Writer {
    override def write(dataframe: DataFrame, output: String, options: Map[String, String]): Unit = {
      dataframe.write
        .mode(saveMode)
        .options(options)
        .saveAsTable(s"$output")
    }
  }
  class AvroWriter(saveMode: String) extends Writer {
    override def write(dataframe: DataFrame, output: String, options: Map[String, String]): Unit = {
      dataframe
        .write
        .format("avro")
        .mode(saveMode)
        .options(options)
        .save(output)
    }
  }


}
