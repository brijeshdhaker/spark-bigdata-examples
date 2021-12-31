package com.spark.rdd.airports

import com.spark.commons.Utils
import org.apache.spark.{SparkConf, SparkContext}

/**

 %SPARK_HOME%\bin\spark-submit ^
 --master yarn ^
 --deploy-mode cluster ^
 --properties-file %SPARK_HOME%\conf\spark-yarn.conf ^
 --class com.spark.rdd.airports.AirportsByLatitudeProblem target\spark-training-1.0.jar

 docker exec spark-master /usr/local/spark/bin/spark-submit \
    --master spark://spark-master:7077 \
    --deploy-mode cluster \
    --class com.spark.rdd.airports.AirportsByLatitudeProblem \
    /e/apps/hostpath/spark/apps/spark-training-scala.jar /e/apps/hostpath/spark/in /e/apps/hostpath/spark/out
    
 */
object AirportsByLatitudeProblem {

  def main(args: Array[String]) {

    System.out.println("    Brijesh K Dhaker    ")
    System.out.println("=== Steup Spark Context ===")

    val conf = new SparkConf().setAppName("AirportsByLatitudeProblem")
    val sc = new SparkContext(conf)

    //val base_datadir = args(0)
    //println(" Input Path :: " + base_datadir );
    //val output_path = args(1)
    //println(" Output Path :: " + output_path );
    //val input_path = base_datadir+"in/airports-small.text";
    
    val airports = sc.textFile("in/airports.text")
    val airportsInUSA = airports.filter(line => line.split(Utils.COMMA_DELIMITER)(6).toFloat > 40)

    val airportsNameAndCityNames = airportsInUSA.map(line => {
      val splits = line.split(Utils.COMMA_DELIMITER)
      splits(1) + ", " + splits(6)
    })

    println("Total No. of Partitions - " + airportsNameAndCityNames.getNumPartitions)
    
    println("Saving results into  hdfs at location : " + "outputs/airports_by_latitude")
    airportsNameAndCityNames.saveAsTextFile("outputs/airports_by_latitude")

    println("=== Stop Spark Context ===")
    sc.stop()

  }
}
