package com.spark.examples.core.rdd.sumOfNumbers

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object SumOfNumbersSolution {

    def main(args: Array[String]) {
        Logger.getLogger("org").setLevel(Level.OFF)

        val conf = new SparkConf().setAppName("primeNumbers")
          .setMaster("local[*]")
        val sc = new SparkContext(conf)

        val lines = sc.textFile("file:/E:/apps/hostpath/spark/in/prime_nums.text")

        val numbers = lines.flatMap(line => line.split("\\s+"))

        val validNumbers = numbers.filter(number => !number.isEmpty)

        val intNumbers = validNumbers.map(number => number.toInt)

        println("Sum is: " + intNumbers.reduce((x, y) => x + y))
    }
}
