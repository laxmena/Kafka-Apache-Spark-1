# Kafka -> Spark Aggregation -> AWS Email Notification

Part 2 of the CS441 Final Project. Spark program connects to the Kafka stream, reads the messages and performs aggregation. Aggregated messages are sent as an email to the stakeholders if the ERROR/WARN messages matches a specific pattern.

## Specifications
- Scala 2.12.12
- Akka Kafka Streaming - 1.0.1
- Spark - 2.4.4
- SLF4S - 2.0.0
- Typesafe Config - 1.4.1
- Logback - 1.3.0

## Project Structure
```
src
├── main
│   ├── resources
│   │   ├── application.conf
│   │   └── logback.xml
│   └── scala
│       ├── HelperUtils
│       │   ├── Constants.scala
│       │   ├── CreateLogger.scala
│       │   ├── EmailService.scala
│       │   ├── ObtainConfigReference.scala
│       │   └── SparkUtil.scala
│       └── SparkAggregation.scala
└── test
    └── scala
        └── DataStreaming
            ├── ConfigTestSuite.scala
            ├── EmailTestSuite.scala
            └── SparkTestSuite.scala
```

