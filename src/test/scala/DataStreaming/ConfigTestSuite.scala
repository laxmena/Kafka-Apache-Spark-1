package LogProcessor

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import HelperUtils.{ObtainConfigReference, Constants}
import HelperUtils.Constants.{sparkConfig, kafkaConfig}

import com.typesafe.config.{Config, ConfigFactory}

class ConfigTestSuite extends AnyFlatSpec with Matchers {
  behavior of "Spark Config Test"
  // App Config Tests
  it should "contain Spark app name" in {
    assert(sparkConfig.hasPath(Constants.SPARK_APP_NAME))
  }

  it should "contain spark master configuration" in {
    assert(sparkConfig.hasPath(Constants.SPARK_MASTER))
  }

  it should "contain Log Regex pattern" in {
    assert(sparkConfig.hasPath(Constants.LOG_REGEX_PATTERN))
  }

  it should "contain TimeStamp pattern" in {
    assert(sparkConfig.hasPath(Constants.TIMESTAMP_PATTERN))
  }

  it should "contain Log level group" in {
    assert(sparkConfig.hasPath(Constants.LOG_LEVEL_GROUP))
  }

  it should "contain Spark Streaming window duration" in {
    assert(sparkConfig.hasPath(Constants.SPARK_STREAMING_WINDOW_DURATION))
  }


  behavior of "Kafka Config Test"

  it should "contain kafka broker" in {
    assert(kafkaConfig.hasPath(Constants.KAFKA_BROKER))
  }
  it should "contain kafka topic" in {
    assert(kafkaConfig.hasPath(Constants.KAFKA_TOPIC))
  }
  it should "contain kafka group id" in {
    assert(kafkaConfig.hasPath(Constants.KAFKA_GROUP_ID))
  }
  it should "contain kafka auto commit" in {
    assert(kafkaConfig.hasPath(Constants.KAFKA_AUTO_COMMIT))
  }
  it should "contain logging level constants" in {
    assert(Constants.ERROR_LOG_LEVEL.equals("ERROR"))
    assert(Constants.DEBUG_LOG_LEVEL.equals("DEBUG"))
    assert(Constants.INFO_LOG_LEVEL.equals("INFO"))
    assert(Constants.WARN_LOG_LEVEL.equals("WARN"))
    assert(Constants.VERBOSE_LOG_LEVEL.equals("VERBOSE"))
  }

}
