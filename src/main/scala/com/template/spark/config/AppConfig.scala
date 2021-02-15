package com.template.spark.config

import com.typesafe.config.{Config, ConfigException, ConfigFactory}

class AppConfig {

  protected val config: Config = ConfigFactory.load()

  def get(key: String): String = {
    config.getString(key)
  }

  def hasKey(key: String): Boolean = {
    try {
      config.hasPath(key)
    } catch {
      case e: ConfigException.BadPath => false
    }
  }

  def getOrDefault(key: String, default: String): String = {
    try {
      config.getString(key)
    } catch {
      case e: ConfigException.Missing => default
    }
  }

}
