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

  def checkLogLevel(log: String, logLevel: String): Int = {
    val logSplit = log.split(" ")
    val logType = logSplit(2)
    if (logType == logLevel) {
      return 1
    }
    return 0
  }

  def isError = (log: String) => checkLogLevel(log, "ERROR")
  def isWarning = (log: String) => checkLogLevel(log, "WARN")
  def isDebug = (log: String) => checkLogLevel(log, "DEBUG")
  def isInfo = (log: String) => checkLogLevel(log, "INFO")
  def combinePartitions = (p1: Int, p2: Int) => p1 + p2

  kafkaStream.foreachRDD { rdd =>
    if(!rdd.isEmpty()) {
      // Aggregation
      val errorCount = rdd.map(record => record.value).map(isError).reduce(combinePartitions)
      val warnCount = rdd.map(record => record.value).map(isWarning).reduce(combinePartitions)
      val debugCount = rdd.map(record => record.value).map(isDebug).reduce(combinePartitions)
      val infoCount = rdd.map(record => record.value).map(isInfo).reduce(combinePartitions)

      // TODO: Send result to AWS Mail
    }
  }

  streamingContext.start()
  streamingContext.awaitTermination()
}