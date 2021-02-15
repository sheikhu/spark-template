package com.template.spark.core

import com.template.spark.io.IOHandler

abstract class Worker(ioHandler: IOHandler) {

  def run(): Unit

}
