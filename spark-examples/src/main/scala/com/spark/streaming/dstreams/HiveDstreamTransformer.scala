package com.spark.streaming.dstreams

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import java.util.UUID

object HiveDstreamTransformer extends App {

  def processRecord(x:Row): Unit ={

  }

  def processRecord(rdd: RDD[(String,String)]): Unit ={

    // 1 - Create a SchemaRDD object from the rdd and specify the schema
    val recordsRDD = rdd.map(x => (Row(UUID.randomUUID().toString, x._2, x._2.split(" ").size, x._2.size)))

    val schema = StructType( Array(
      StructField("uuid", StringType, true),
      StructField("text", StringType, true),
      StructField("words", IntegerType, true),
      StructField("length", IntegerType, true)
    ))
    val recordsDF = ss.createDataFrame(recordsRDD, schema)
    recordsDF.show()
    // 2 - register it as a spark sql table
    //recordsDF.registerTempTable("sparktable")
    // 3 - qry sparktable to produce another SchemaRDD object of the data needed 'finalParquet'. and persist this as parquet files
    //val finalParquet = ss.sql("sql")
    //finalParquet.write.saveAsTable("")
    // Turn on flag for Hive Dynamic Partitioning
    ss.conf.set("hive.exec.dynamic.partition", "true")
    ss.conf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    //recordsDF.write.mode("overwrite").saveAsTable("default.tweeter_tweets")
    recordsDF.write.mode(SaveMode.Append).saveAsTable("tweeter_tweets")
  }

  val conf = new SparkConf().setAppName("hive-stream-transformer").setMaster("local[4]")
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

  /*
  lines.foreachRDD(rdd => {

    // 1 - Create a SchemaRDD object from the rdd and specify the schema
    val recordsRDD = rdd.map(x => (Row(UUID.randomUUID().toString, x._2, x._2.split(" ").size, x._2.size)))
    recordsRDD.map(processRecord)

    val schema = StructType( Array(
      StructField("uuid", StringType, true),
      StructField("text", StringType, true),
      StructField("words", IntegerType, true),
      StructField("length", IntegerType, true)
    ))

    val recordsDF = ss.createDataFrame(recordsRDD, schema)
    recordsDF.show()

    // 2 - register it as a spark sql table
    //recordsDF.registerTempTable("sparktable")

    // 3 - qry sparktable to produce another SchemaRDD object of the data needed 'finalParquet'. and persist this as parquet files
    //val finalParquet = ss.sql("sql")
    //finalParquet.write.saveAsTable("")
    recordsDF.write.format("orc").mode(SaveMode.Append).saveAsTable("default.tweeter_tweets")

  })
  */

  ssc.start()
  ssc.awaitTermination()


}
