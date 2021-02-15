FROM spark-base:latest

COPY /datasets /app/datasets
COPY /scripts /app/scripts
COPY spark-submit.sh /app
ADD spark-template-1.0-SNAPSHOT-dev.tar.gz /app
COPY spark-template-1.0-SNAPSHOT.jar /app
COPY log4j.properties /app

ENV SPARK_MASTER_URL="spark://spark-master:7077"
ENV SPARK_SUBMIT_ARGS=""
ENV SPARK_APPLICATION_ARGS "--input /app/datasets/employees.csv --output output --format csv"
ENV SPARK_APPLICATION_JAR_LOCATION /app/hello/hello.jar
ENV SPARK_APPLICATION_MAIN_CLASS com.template.spark.Main

CMD ["/bin/bash", "/app/scripts/job.sh"]
