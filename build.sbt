// The simplest possible sbt build file is just one line:
scalaVersion := "2.12.12"

name := "spark-kafka-streaming"
organization := "com.laxmena"
version := "1.0"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// Include Akka
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.23"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "1.0.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.23"

// Include dependencies for Spark-Kafka
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.4"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.12.89"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.12.89"
