package com.spark.examples.streaming.dstreams

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import java.util.UUID

object CassandraDstreamTransformer extends App {

  def processRecord(x:Row): Unit ={

  }

  def processRecord(rdd: RDD[(String,String)]): Unit ={
    if(rdd.isEmpty()) {
      // 1 - Create a SchemaRDD object from the rdd and specify the schema
      val recordsRDD = rdd.map(x => (Row(UUID.randomUUID().toString, x._2, x._2.split(" ").size, x._2.size)))

      val schema = StructType(Array(
        StructField("uuid", StringType, true),
        StructField("text", StringType, true),
        StructField("words", IntegerType, true),
        StructField("length", IntegerType, true)
      ))
      val recordsDF = ss.createDataFrame(recordsRDD, schema)
      recordsDF.show()

      // Write To Cassandra
      recordsDF.write.format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "tweeter_tweets", "keyspace" -> "spark_stream"))
        .mode(SaveMode.Append)
        .save()

      println("Provide RDD records written into Cassandra.")

    }else{
      println("Provide RDD is empty.")
    }

  }

  val conf = new SparkConf()
    .setAppName("hive-stream-transformer")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .setMaster("local[4]")

  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  val ss = SparkSession.builder().enableHiveSupport().getOrCreate()
  val ssc = new StreamingContext(sc, Durations.seconds(10))

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "kafka-broker:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "hive-dstream-cg",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val topics = Array("tweeter-tweets")
  val stream = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

  val lines = stream.map(record => (record.key, record.value))

  lines.foreachRDD(rdd => processRecord(rdd))

  ssc.start()
  ssc.awaitTermination()


}
