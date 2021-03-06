package com.template.spark.core

import com.template.spark.io.IOHandler
import com.template.spark.models.JobParameters
import org.apache.log4j.Logger

abstract class BaseWorker(ioHandler: IOHandler) {

  def run(options: JobParameters): Unit

}
