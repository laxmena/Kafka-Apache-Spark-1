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
  
  // print resulting rdd
  kafkaStream.foreachRDD(rdd => {
    val errorList = new ListBuffer[String]()
    val warnList = new ListBuffer[String]()

    if (!rdd.isEmpty()) {
      // Get each record and print it

      // Create a mutable list
      rdd.foreach(record => {
        val logLevel = record.value().split(" ")(2)
        print("|" + logLevel + "| - " + "\n")
        if(logLevel == "ERROR") {
          // Append to ListBuffer
          print("Err here!\n")
          errorList += logLevel
        }
        else if(logLevel == "WARN") {
          print("Warnn here!\n")
          warnList += logLevel
        }
        
      })

      print("SSSSSSMMMMMMMMMRRRRRRRRRRRIIIIIIIITTTTTTTTTHHHHHHHIIIIIII\n")
      print(errorList.toString() + "\n")
      // ListBuffer length
      val errorListLength = errorList.toList
      val warnListLength = warnList.toList
      println(errorList)

      println("Error: " + errorListLength.length + "\n")
      println("Warn: " + warnListLength.length + "\n")
      print("SSSSSSMMMMMMMMMRRRRRRRRRRRIIIIIIIITTTTTTTTTHHHHHHHIIIIIII\n")
    }
  })

  streamingContext.start()
  streamingContext.awaitTermination()
}