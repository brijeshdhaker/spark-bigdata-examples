package com.spark.rdd.collect

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

/*
// com.spark.rdd.collect.CollectExample
%SPARK_HOME%\bin\spark-submit.cmd ^
    --master local ^
    --class com.spark.rdd.collect.CollectExample ^
    file:///e:/apps/hostpath/spark/spark-training-scala.jar 'file:///E:/apps/hostpath/spark/' 'file:///E:/apps/hostpath/spark/outputs'
*/
object CollectExample {
  def main(args: Array[String]) {
    
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val conf = new SparkConf().setMaster("local[*]").setAppName("collect")
                
    val sc = new SparkContext(conf)

    val inputWords = List("spark", "hadoop", "spark", "hive", "pig", "cassandra", "hadoop")
    val wordRdd = sc.parallelize(inputWords)

    val words = wordRdd.collect()

    for (word <- words) println(word)
    
  }
}
