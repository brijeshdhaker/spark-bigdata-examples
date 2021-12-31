package com.spark.rdd.sumOfNumbers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object SumOfNumbersProblem {

  def main(args: Array[String]) {

    /* Create a Spark program to read the first 100 prime numbers from in/prime_nums.text,
       print the sum of those numbers to console.

       Each row of the input file contains 10 prime numbers separated by spaces.
     */
    Logger.getLogger("org").setLevel(Level.OFF)

    val session = SparkSession.builder()
      .appName("SumOfNumbersProblem")
      .master("local[*]")
      .getOrCreate()

    val sc = session.sparkContext

    val rdd = sc.textFile("file:/E:/apps/hostpath/spark/in/prime_nums.text")
    val strNumberRDD = rdd.flatMap(l => l.split("\\s+"))

    // Eliminate Blank Numbers
    val validNumbers = strNumberRDD.filter(number => !number.isEmpty)

    // Conver String to Int
    val validIntNumbers = validNumbers.map(validNumbers => validNumbers.toInt)

    val totalSum = validIntNumbers.reduce(_+_);
    println("Sum is: " + totalSum)

  }
}
