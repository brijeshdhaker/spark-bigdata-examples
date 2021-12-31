package com.spark.rdd.airports

import com.spark.commons.Utils
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
 *
 * %SPARK_HOME%\bin\spark-submit ^
 *--master yarn ^
 *--deploy-mode cluster ^
 *--properties-file %SPARK_HOME%\conf\spark-yarn.conf ^
 *--class com.sparkTutorial.rdd.airports.AirportsInUsaProblem target\spark-training-1.0.jar
 *
 */
object AirportsInUsaProblem {
  def main(args: Array[String]) {

    /* Create a Spark program to read the airport data from in/airports.text,
       find all the airports which are located in United States
       and output the airport's name and the city's name to out/airports_in_usa.text.

       Each row of the input file contains the following columns:
       Airport ID, Name of airport, Main city served by airport, Country where airport is located,
       IATA/FAA code, ICAO Code, Latitude, Longitude, Altitude, Timezone, DST,
       Timezone in Olson format

       Sample output:
       "Putnam County Airport", "Greencastle"
       "Dowagiac Municipal Airport", "Dowagiac"
       ...
     */
      val session: SparkSession = SparkSession.builder()
        .appName("AirportsInUsaProblem")
        .getOrCreate()

      val sc:SparkContext = session.sparkContext;

      val fileRDD = sc.textFile("in/airports.text")
      val filterRDD = fileRDD.filter(l => {l.split(Utils.COMMA_DELIMITER)(3) == "\"United States\""})
      filterRDD.foreach(f => {
        var s = f.split(Utils.COMMA_DELIMITER)
        println(s(1) + ", " + s(3))
      })

  }
}
