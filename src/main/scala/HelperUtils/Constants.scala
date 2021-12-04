package HelperUtils

class Constants
object Constants {
    val SPARK_CONFIG = "sparkConfig"
    val KAFKA_CONFIG = "kafkaConfig"

    val SPARK_APP_NAME = "sparkAppName"
    val SPARK_MASTER = "sparkMaster"
    val TIMESTAMP_PATTERN = "timestampPattern"
    val LOG_REGEX_PATTERN = "logRegexPattern"
    val LOG_LEVEL_GROUP = "logLevelGroup"
    val TIMESTAMP_GROUP = "timestampGroup"

    val SPARK_STREAMING_WINDOW_DURATION = "sparkStreamingWindowDuration"
    
    val KAFKA_BROKER = "kafkaBroker"
    val KAFKA_TOPIC = "kafkaTopic"
    val KAFKA_GROUP_ID = "kafkaGroupId"
    val KAFKA_AUTO_OFFSET_RESET = "KafkaConsumerOffset"
    val KAFKA_AUTO_COMMIT = "KafkaConsumerAutoCommit"
    
    val ERROR_LOG_LEVEL = "ERROR"
    val DEBUG_LOG_LEVEL = "DEBUG"
    val INFO_LOG_LEVEL = "INFO"
    val WARN_LOG_LEVEL = "WARN"
    val VERBOSE_LOG_LEVEL = "VERBOSE"

    // Get Configs
    val sparkConfig = ObtainConfigReference(SPARK_CONFIG) match {
        case Some(value) => value.getConfig(SPARK_CONFIG)
        case None => throw new RuntimeException("Cannot obtain a reference to the userInput config data.")
    }
    val kafkaConfig = ObtainConfigReference(KAFKA_CONFIG) match {
        case Some(value) => value.getConfig(KAFKA_CONFIG)
        case None => throw new RuntimeException("Cannot obtain a reference to the userInput config data.")
    }
}