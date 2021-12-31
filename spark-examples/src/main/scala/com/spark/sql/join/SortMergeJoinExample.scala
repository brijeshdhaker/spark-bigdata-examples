package com.spark.sql.join

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object SortMergeJoinExample {

  @transient lazy  val logger : Logger = Logger.getLogger(getClass.getName)

  def main (args: Array[String]): Unit = {

    //Logger.getLogger("org").setLevel(Level.WARN)
    //spark.sparkContext.setLogLevel("OFF")

    val spark: SparkSession = SparkSession.builder()
      .appName("ShuffleJoinExample")
      .master("local[3]")
      .getOrCreate()

    // Disable Broadcast Join
    spark.conf.set("spark.sql.autoBroadcastJoinThreshold",-1)

    val df1 = spark.read.json("hdfs://namenode:9000/datasets/github.json")
    val df2 = spark.read.json("hdfs://namenode:9000/datasets/github-top20.json")

    spark.conf.set("spark.sql.shuffle.partitions", 3)

    val joinExper = df1.col("actor.login") === (df2.col("login"))
    val joinDF = df1.join(df2, joinExper, "inner")

    // Apply Dummy Action
    joinDF.foreach(_ => ())

    //  Hold Spark
    scala.io.StdIn.readLine()

  }


}
