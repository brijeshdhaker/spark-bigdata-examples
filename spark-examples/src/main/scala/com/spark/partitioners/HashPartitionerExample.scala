package com.spark.partitioners

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object HashPartitionerExample {

  def countByPartition(rdd: RDD[(Int, None.type)]) {
    rdd.mapPartitions(x => Iterator(x.length))
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark Pi")
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(for{
      x <- 1 to 2
      y <- 1 to 3
    } yield(1, None),8)

    rdd.collect.foreach(println)
    rdd.mapPartitions(x => Iterator(x.length)).collect().foreach(println)
    println("P1-------------------")
    val rddP1 = rdd.partitionBy(new HashPartitioner(1))
    rddP1.mapPartitions(x => Iterator(x.length)).collect().foreach(println)
    println("P2-------------------")
    val rddP2 = rdd.partitionBy(new HashPartitioner(6))
    rddP2.mapPartitions(x => Iterator(x.length)).collect().foreach(println)
    //countByPartition(rdd)

  }

}
