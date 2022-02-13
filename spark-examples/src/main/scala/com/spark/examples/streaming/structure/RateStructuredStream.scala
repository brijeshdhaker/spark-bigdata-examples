package com.spark.examples.streaming.structure

import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{SparkSession, functions}

import scala.concurrent.duration.DurationInt

/*

mvn exec:exec@run-local -Drunclass=com.spark.streaming.structure.KafkaStructuredStream -Dparams="50"

*/
object RateStructuredStream extends App {

  val spark: SparkSession = SparkSession.builder()
    .master("local[4]")
    .appName("RateStructuredStream")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  spark.conf.set("spark.sql.streaming.checkpointLocation", "/structured-stream/checkpoints/")
  spark.conf.set("spark.sql.shuffle.partitions", "1")
  spark.conf.set("spark.sql.hive.convertMetastoreParquet", "false")
  spark.conf.set("spark.sql.parquet.writeLegacyFormat", "true")
  spark.conf.set("spark.sql.parquet.binaryAsString", "true")

  // Create Schema for Dataframes
  val schema = new StructType()
    .add("id", IntegerType)
    .add("uuid", StringType)
    .add("cardtype", StringType)
    .add("website", StringType)
    .add("product", StringType)
    .add("amount", DoubleType)
    .add("city", StringType)
    .add("country", StringType)
    .add("addts", LongType)


  val structureStreamDf = spark
    .readStream
    .format("rate")
    .option("rowsPerSecond", 2)
    .load()

  // Returns True for DataFrames that have streaming sources
  print("structureStreamDf.isStreaming : " + structureStreamDf.isStreaming)
  print("Schema for structureStreamDf  : ")
  structureStreamDf.printSchema()

  val recordsDF = structureStreamDf.withColumn("result", col("value") + lit(1))
  //# Group the data by window and word and compute the count of each group

  recordsDF.printSchema()


  // Writing to console sink (for debugging)
  recordsDF.writeStream
    .outputMode("append")
    .option("truncate", false)
    .format("console")
//    .trigger(Trigger.ProcessingTime("30 seconds"))
    .start()
    .awaitTermination()

}