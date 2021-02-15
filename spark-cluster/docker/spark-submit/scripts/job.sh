#!/usr/bin/env bash

SPARK_HOME=${SPARK_HOME:-/opt/spark}

SPARK_SUBMIT=$SPARK_HOME/bin/spark-submit
ENABLE_SPARK_UI=${ENABLE_SPARK_UI:-"true"}

SPARK_MASTER_URL=spark://spark-master:7077


$SPARK_SUBMIT \
--master ${SPARK_MASTER_URL} \
--deploy-mode cluster \
--supervise \
--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=/app/file:log4j.properties" \
--conf "spark.executor.extraJavaOptions=-Dlog4j.configuration=file:log4j.properties" \
--conf "spark.extraListeners=com.template.spark.listener.DefaultListener" \
--files /app/log4j.properties \
--conf "spark.ui.enabled=$ENABLE_SPARK_UI" \
--class ${SPARK_APPLICATION_MAIN_CLASS} ${SPARK_APPLICATION_JAR_LOCATION} ${SPARK_APPLICATION_ARGS}

