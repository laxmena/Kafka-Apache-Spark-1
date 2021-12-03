// import spark kafka utils
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
// Import SparkConf
import org.apache.spark.SparkConf
// Import SparkContext
import org.apache.spark.SparkContext
// Import SparkSession
import org.apache.spark.sql.SparkSession
// Import Spark Streaming
import org.apache.spark.streaming.{Seconds, StreamingContext}
// Import Spark Streaming Context
import org.apache.spark.streaming.StreamingContext
// Import Spark Streaming Receiver
import org.apache.spark.streaming.receiver.Receiver
// Import Spark Streaming Context
import org.apache.spark.streaming.StreamingContext
// Import Spark Streaming DStream
import org.apache.spark.streaming.dstream.DStream
import java.util.UUID
import scala.collection.mutable.ListBuffer

object Main extends App {
  println("Hello, World!")

  val conf = new SparkConf().setAppName("Spark Streaming").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val streamingContext = new StreamingContext(sc, Seconds(1))

  // Create kafka params
  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    // set group id config
    "group.id" -> UUID.randomUUID().toString,
    
  )

  val kafkaStream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Set("test"), kafkaParams)
  )

  val lines = kafkaStream.map(record => record.value)
  val words = lines.flatMap(_.split(" "))
  val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)

  // Create a Hashtable of timestamp and count

  def isWarningLog(log: String): Int = {
    val logSplit = log.split(" ")
    val logType = logSplit(2)
    if (logType == "WARN") {
      return 1
    }
    return 0
  }

  def isErrorLog(log: String): Int = {
    val logSplit = log.split(" ")
    val logType = logSplit(2)
    if (logType == "ERROR") {
      return 1
    }
    return 0
  }

  def isInfoLog(log: String): Int = {
    val logSplit = log.split(" ")
    val logType = logSplit(2)
    if (logType == "INFO") {
      return 1
    }
    return 0
  }

  def isDebugLog(log: String): Int = {
    val logSplit = log.split(" ")
    val logType = logSplit(2)
    if (logType == "DEBUG") {
      return 1
    }
    return 0
  }

  def isError = (log: String, prevCount: Int) => isErrorLog(log) + prevCount
  def isWarning = (log: String, prevCount: Int) => isWarningLog(log) + prevCount
  def isDebug = (log: String, prevCount: Int) => isDebugLog(log) + prevCount
  def isInfo = (log: String, prevCount: Int) => isInfoLog(log) + prevCount
  def combinePartitions = (p1: Int, p2: Int) => p1 + p2

  kafkaStream.foreachRDD { rdd =>
    if(!rdd.isEmpty()) {
      // Aggregation
      val errorCount = rdd.map(record => record.value).map(isErrorLog).reduce(combinePartitions)
      val warnCount = rdd.map(record => record.value).map(isWarningLog).reduce(combinePartitions)
      val debugCount = rdd.map(record => record.value).map(isDebugLog).reduce(combinePartitions)
      val infoCount = rdd.map(record => record.value).map(isInfoLog).reduce(combinePartitions)


    }
  }

  streamingContext.start()
  streamingContext.awaitTermination()
}