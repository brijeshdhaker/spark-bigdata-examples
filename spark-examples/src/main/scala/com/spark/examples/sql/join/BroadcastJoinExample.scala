package com.spark.examples.sql.join

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object BroadcastJoinExample {

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

    val df1 = spark.read.json("/datasets/github.json")
    val df2 = spark.read.json("/datasets/github-top20.json")

    spark.conf.set("spark.sql.shuffle.partitions", 3)

    val joinExper = df1.col("actor.login") === (df2.col("login"))
    val joinDF = df1.join(broadcast(df2), joinExper, "inner")

    // Apply Dummy Action
    joinDF.foreach(_ => ())

    //  Hold Spark
    scala.io.StdIn.readLine()

  }


}
