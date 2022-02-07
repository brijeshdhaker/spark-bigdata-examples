package com.spark.examples.core.pairRdd.aggregation.reducebykey.housePrice

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

object AverageHousePriceSolution {

  def main(args: Array[String]) {


    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf().setAppName("avgHousePrice").setMaster("local[4]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("/datasets/RealEstate.csv")
    val cleanedLines = lines.filter(line => !line.contains("Bedrooms"))

    val housePricePairRdd = cleanedLines.map(line => (line.split(",")(3), (1, line.split(",")(2).toDouble)))

    val housePriceTotal = housePricePairRdd.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

    println("housePriceTotal: ")
    for ((s, (bedroom, total)) <- housePriceTotal.collect()) println(bedroom + " : " + total)

    val housePriceAvg = housePriceTotal.mapValues(avgCount => avgCount._2 / avgCount._1)

    println("housePriceAvg: ")
    for ((bedroom, avg) <- housePriceAvg.collect()) println(bedroom + " : " + avg)
  }
}
case class AHCount(count: Any, total: Any)