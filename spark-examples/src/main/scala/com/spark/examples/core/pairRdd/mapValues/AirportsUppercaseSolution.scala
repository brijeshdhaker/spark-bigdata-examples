package com.spark.examples.core.pairRdd.mapValues

import com.spark.examples.commons.Utils
import org.apache.spark.{SparkConf, SparkContext}
/*
Create a Spark program to read the airport data from in/airports.text, generate a pair RDD with airport name
   being the key and country name being the value. Then convert the country name to uppercase and
   output the pair RDD to out/airports_uppercase.text

   Each row of the input file contains the following columns:

   Airport ID, Name of airport, Main city served by airport, Country where airport is located, IATA/FAA code,
   ICAO Code, Latitude, Longitude, Altitude, Timezone, DST, Timezone in Olson format

   Sample output:

   ("Kamloops", "CANADA")
   ("Wewak Intl", "PAPUA NEW GUINEA")
   ...
 */

object AirportsUppercaseSolution {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("airports").setMaster("local")
    val sc = new SparkContext(conf)

    val airportsRDD = sc.textFile("/datasets/airports.text")

    val airportPairRDD = airportsRDD.map((line: String) => (line.split(Utils.COMMA_DELIMITER)(1), line.split(Utils.COMMA_DELIMITER)(3)))

    val upperCase = airportPairRDD.mapValues(countryName => countryName.toUpperCase)
    for( e <- upperCase.collect()) println(e)

    //upperCase.saveAsTextFile("out/airports_uppercase.text")

  }
}
