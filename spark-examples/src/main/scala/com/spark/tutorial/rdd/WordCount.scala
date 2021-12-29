package com.spark.tutorial.rdd

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark._

object WordCount {

  def main(args: Array[String]) {

    //Check whether sufficient params are supplied
    if (args.length < 2) {
      println("Usage: WordCount <input> <output>")
      System.exit(1)
    }

    val src_path = args(0)
    val tgt_path = args(1)

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("WordCounts").setMaster("local[3]")
    val sc = new SparkContext(conf)

    //Read file and create RDD
    val lines = sc.textFile(src_path+"in/word_count.text")

    //convert the lines into words using flatMap operation
    val words = lines.flatMap(line => line.split(" "))

    val wordCounts = words.countByValue()
    for ((word, count) <- wordCounts) println(word + " : " + count)

  }
}
