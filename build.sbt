// The simplest possible sbt build file is just one line:
scalaVersion := "2.12.12"

name := "spark-kafka-streaming"
version := "1.0"

val logbackVersion = "1.3.0-alpha10"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val scalacticVersion = "3.2.9"
val sparkVersion = "2.4.4"
val awsJavaSdkVersion = "1.12.89"
val akkaVersion = "2.5.23"
val akkaKafkaStreamingVersion = "1.0.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.1"

// Include Akka
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % akkaKafkaStreamingVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion

// Include dependencies for Spark-Kafka
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.4"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % awsJavaSdkVersion
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % awsJavaSdkVersion

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "org.scalactic" %% "scalactic" % scalacticVersion,
  "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test,
  "com.typesafe" % "config" % typesafeConfigVersion,
)