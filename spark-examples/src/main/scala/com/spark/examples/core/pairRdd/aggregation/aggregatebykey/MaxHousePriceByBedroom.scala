package com.spark.examples.core.pairRdd.aggregation.aggregatebykey

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/*
Problem :: Create a Spark program to read the house data from in/RealEstate.csv,
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

object MaxHousePriceByBedroom {

  // 1st Argument : specify what to do with value of the key if the same key appears inside same partition
  def seqMax(accumulator : Double, element:(Int, Double)) : Double ={
      if(accumulator > element._2){
        return accumulator;
      }else{
        return element._2
      }
  }

  // 2nd Argument : specify what to do with the values of same key across all other partitions
  def comMax(accumulator1 : Double, accumulator2 : Double) : Double ={
    return accumulator1.max(accumulator2)
  }

  def main(args: Array[String]) {


    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf().setAppName("avgHousePrice").setMaster("local[4]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("/datasets/RealEstate.csv")
    val cleanedLines = lines.filter(line => !line.contains("Bedrooms"))

    val housePricePairRdd = cleanedLines.map(line => (line.split(",")(3), (1, line.split(",")(2).toDouble)))

    // 1st Argument : specify what to do with value of the key if the same key appears inside same partition
    val seq_max = (accu:Double, v:(Int, Double)) => (if(accu > v._2) accu else v._2)

    // 2nd Argument : specify what to do with the values of same key across all other partitions
    val comb_max = (accu1:Double, accu2:Double) => accu1.max(accu2)

    val housePriceMax = housePricePairRdd.aggregateByKey(0.00)(seq_max, comb_max)

    println("housePriceTotal: ")
    for ((bedroom, max) <- housePriceMax.collect()) println(bedroom + " : " + max)

  }
}
