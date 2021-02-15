#!/usr/bin/env bash

SPARK_HOME=${SPARK_HOME:-/opt/spark}

SPARK_SUBMIT=$SPARK_HOME/bin/spark-submit
ENABLE_SPARK_UI=${ENABLE_SPARK_UI:-"true"}

$SPARK_SUBMIT \
--master local \
--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=file:src/main/resources/log4j.properties" \
--conf "spark.extraListeners=com.template.spark.listener.DefaultListener" \
--conf "spark.ui.enabled=$ENABLE_SPARK_UI" \
--class com.template.spark.Main $@

