package com.template.spark

import org.apache.spark.sql.SparkSession

object Main extends App {

    val spark = SparkSession.builder()
      .master("local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    val users = Seq(
        ("Joe", 30),
        ("Jane", 20)
    ).toDF("name", "age")

    users.show(false)

    spark.stop
}