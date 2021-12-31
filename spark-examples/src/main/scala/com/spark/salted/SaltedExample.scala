package com.spark.salted

import java.util.Random

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object SaltedExample {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  def main(args:Array[String]): Unit = {

    val jsonPath = "C:\\Neha\\personal\\Adobe\\Code\\spark_training-master\\data\\datamodeling\\Salted.json"//args(0)

    val sparkSession = SparkSession.builder
      .master("local")
      .appName("my-spark-app")
      .config("spark.sql.crossJoin.enabled", "true")
      .getOrCreate()

    val jsonDfLeft = sparkSession.read.json(jsonPath)

    val saltedLeft = jsonDfLeft.rdd.flatMap(r => {
      val group = r.getAs[String]("group")
      val value = r.getAs[Long]("value")

      Seq((group + "_" + 0, value),(group + "_" + 1, value))
    })

    val jsonDfRight = sparkSession.read.json(jsonPath)

    val saltedRight = jsonDfRight.rdd.mapPartitions(it => {

      val random = new Random()

      it.map(r => {
        val group = r.getAs[String]("group")
        val value = r.getAs[Long]("value")

        (group + "_" + random.nextInt(2), value)
      })
    })

    jsonDfLeft.join(jsonDfRight).collect().foreach(r => {
      println("Normal.result:" + r)
    })
    println("----")
    saltedLeft.join(saltedRight).collect().foreach(r => {
      println("Salted.result:" + r)
    })
  }
}
