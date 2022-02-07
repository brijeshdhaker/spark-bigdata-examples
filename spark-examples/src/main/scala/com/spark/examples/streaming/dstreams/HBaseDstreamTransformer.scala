package com.spark.examples.streaming.dstreams

import org.apache.hadoop.hbase.spark.datasources.HBaseTableCatalog
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import java.util.UUID

object HBaseDstreamTransformer extends App {

  def catalog = s"""{
                   |"table": {"namespace": "default", "name": "tweeter_tweets", "tableCoder": "PrimitiveType"},
                   |"rowkey": "key",
                   |"columns": {
                   |"uuid": {"cf": "rowkey", "col": "key", "type": "string"},
                   |"text": {"cf": "Sentence", "col": "text", "type": "string"},
                   |"words": {"cf": "Measure", "col": "words", "type": "string"},
                   |"length": {"cf": "Measure", "col": "length", "type": "string"}
                   |}
                   |}""".stripMargin

  def processRecord(x:Row): Unit ={

  }

  def processRecord(rdd: RDD[(String,String)]): Unit ={
    if(rdd.isEmpty()) {
      // 1 - Create a SchemaRDD object from the rdd and specify the schema
      val recordsRDD = rdd.map(x => (Row(UUID.randomUUID().toString, x._2, x._2.split(" ").size, x._2.size)))
      val schema = StructType( Array(
        StructField("uuid", StringType, true),
        StructField("text", StringType, true),
        StructField("words", IntegerType, true),
        StructField("length", IntegerType, true)
      ))
      val recordsDF = ss.createDataFrame(recordsRDD, schema)
      //recordsDF.show()
      recordsDF.write.option("hbase.config.resources", "file:///opt/spark-3.1.2/conf/hbase-site.xml")
        .option("hbase.spark.config.location", "/opt/spark-3.1.2/conf")
        .option("hbase.spark.use.hbasecontext", false)
        .options(Map(HBaseTableCatalog.tableCatalog -> catalog, HBaseTableCatalog.newTable -> "4"))
        .format("org.apache.hadoop.hbase.spark")
        .save()

      println("Provide RDD records written into Hbase.")

    }else{

      println("Provide RDD is empty.")

    }
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

  ssc.start()
  ssc.awaitTermination()


}
