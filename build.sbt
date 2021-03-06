name := "spark-template"

version := "0.1"

scalaVersion := "2.12.10"

val sparkVersion = "3.0.1"
val vegasVersion = "0.3.11"
val postgresVersion = "42.2.2"
val typesafeVersion = "1.4.1"
val scoptVersion = "4.0.0"

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  // logging
  "org.apache.logging.log4j" % "log4j-api" % "2.4.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.4.1",
  // postgres for DB connectivity
  "org.postgresql" % "postgresql" % postgresVersion,

  // Tools
  "com.typesafe" % "config" % typesafeVersion,
  "com.github.scopt" %% "scopt" % scoptVersion,

  //Test
  "org.mockito" %% "mockito-scala" % "1.14.8",
  "org.scalatest" %% "scalatest" % "3.0.4",
  "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_1.0.0"
)