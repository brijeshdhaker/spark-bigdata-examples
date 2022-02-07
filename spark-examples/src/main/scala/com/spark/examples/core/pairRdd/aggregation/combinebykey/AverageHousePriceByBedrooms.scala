package com.spark.examples.core.pairRdd.aggregation.combinebykey

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/*
Create a Spark program to read the house data from in/RealEstate.csv,
output the average price for houses with different number of bedrooms.

The houses data set contains a collection of recent reale state listings in SanLuis Obispo county and
around it.

The data set contains the following fields:
1.MLS: Multiple listing Service number for the house(uniqueID).
2.Location: city/town where the house is located.
3.Price: the most recent listing price of the house(in dollars).
4.Bedrooms: Number of bedrooms.
5.Bathrooms: Number of bathrooms.
6.Size: Size of the house in square feet.
7.Price/SQ.ft: price of the house per square foot.
8.Status:type of sale.Thee type sarerepresented in the dataset: ShortSale,Foreclosureand Regular.

Each field is comma separated.

Sample output:

 (3, 325000)
 (1, 266356)
 (2, 325000)
 ...

 3, 1 and 2 mean the number of bedrooms. 325000 means the average price of houses with 3 bedrooms is 325000.
*/

object AverageHousePriceByBedrooms {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("AverageHousePriceByBedrooms").setMaster("local[3]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("/datasets/RealEstate.csv")

    //
    val cleanedLines = lines.filter(line => !line.contains("Bedrooms"))

    val housePricePairRdd = cleanedLines.map(line => (line.split(",")(3), line.split(",")(2).toDouble))

    //1st Argument : specify the what to do with value of the key when the first time key appears in partition.
    val initialize = (x: Double) => (1, x)

    //2nd Argument : specify what to do with value of the key if the same key appears inside same partition
    val combiner = (avgCount: AvgCount, x: Double) => (avgCount._1 + 1, avgCount._2 + x)

    //3rd Argument : specify what to do with the values of key across  other partitions
    val mergeCombiners = (avgCountA: AvgCount, avgCountB: AvgCount) => (avgCountA._1 + avgCountB._1, avgCountA._2 + avgCountB._2)

    val housePriceTotal = housePricePairRdd.combineByKey(initialize, combiner, mergeCombiners)

    val housePriceAvg = housePriceTotal.mapValues(avgCount => avgCount._2 / avgCount._1)
    for ((bedrooms, avgPrice) <- housePriceAvg.collect()) println(bedrooms + " : " + avgPrice)

  }

  type AvgCount = (Int, Double)

}
