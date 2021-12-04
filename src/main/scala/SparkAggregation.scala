import org.apache.spark.streaming.kafka010._
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import scala.collection.mutable.ListBuffer
import java.util.UUID

import HelperUtils.{Constants, SparkUtil, CreateLogger, ObtainConfigReference}
import HelperUtils.Constants.{sparkConfig, kafkaConfig}

class SparkAggregation
object SparkAggregation extends App {
  val logger = CreateLogger(classOf[SparkAggregation])

  logger.info("Creating new Spark Configuration")
  val conf = new SparkConf()
                  .setAppName(sparkConfig.getString(Constants.SPARK_APP_NAME))
                  .setMaster(sparkConfig.getString(Constants.SPARK_MASTER))

  logger.info("Creating new Spark Context")
  val sc = new SparkContext(conf)

  logger.info("Creating new Spark Streaming Context")
  val streamingContext = new StreamingContext(
    sc, 
    Seconds(sparkConfig.getInt(Constants.SPARK_STREAMING_WINDOW_DURATION))
  )

  logger.info("Create new Kafka Configuration with Kafka Params")
  val kafkaParams = Map[String, Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> kafkaConfig.getString(Constants.KAFKA_BROKER),
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.GROUP_ID_CONFIG -> kafkaConfig.getString(Constants.KAFKA_GROUP_ID),
  )

  logger.info("Create new Kafka Direct Stream")
  val kafkaStream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Set(kafkaConfig.getString(Constants.KAFKA_TOPIC)), kafkaParams)
  )

  kafkaStream.foreachRDD { rdd =>
    if(!rdd.isEmpty()) {
      logger.info("Processing new RDD")
      // Aggregation
      val errorCount = rdd.map(record => record.value)
                          .map(SparkUtil.isError)
                          .reduce(SparkUtil.combinePartitions)
      val warnCount = rdd.map(record => record.value)
                          .map(SparkUtil.isWarning)
                          .reduce(SparkUtil.combinePartitions)
      val debugCount = rdd.map(record => record.value)
                          .map(SparkUtil.isDebug)
                          .reduce(SparkUtil.combinePartitions)
      val infoCount = rdd.map(record => record.value)
                          .map(SparkUtil.isInfo)
                          .reduce(SparkUtil.combinePartitions)
      
      logger.info("Aggregated counts: E:" + errorCount + " W:" + warnCount + " D:" + debugCount + " I:" + infoCount)

      // TODO: Remove this code AWS Mail is implemented
      println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSMMMMMMMMMMMMMMMMMMMMRRRRRRRRRRRRR")
      println("Error Count: " + errorCount)
      println("Warning Count: " + warnCount)
      println("Debug Count: " + debugCount)
      println("Info Count: " + infoCount)
      println("IIIIIIIIIIIIIIIIIIIIIITTTTTTTTTTTTTHHHHHHHHHHHHHIIIIIIIIIIIIIIII")

      // TODO: Send result to AWS Mail
      logger.info("Sending result to AWS Mail")
      HelperUtils.AwsEmailService.email(errorCount)

    }
  }

  // Start the Streaming Context.
  logger.info("Starting Spark Streaming Context")
  streamingContext.start()
  streamingContext.awaitTermination()
  logger.info("Spark Streaming Context has been terminated")
  
}