package com.spark.streaming

import org.apache.spark.streaming.{Duration, Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

// import org.apache.kafka.common.serialization.StringDeserializer

object HBaseDStream {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Spark Pi")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Durations.seconds(10))
    /*
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092,anotherhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("topicA", "topicB")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value))
*/
  }

}
