package com.spark.examples.core.rdd.airports

import org.apache.spark.{SparkConf, SparkContext}
/**

%SPARK_HOME%\bin\spark-submit ^
 --master yarn ^
 --deploy-mode cluster ^
 --properties-file %SPARK_HOME%\conf\spark-yarn.conf ^
 --class com.sparkTutorial.rdd.airports.AirportsInUsaSolution target\spark-training-1.0.jar

 */
object AirportsInUsaSolution {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("AirportsInUsaSolution").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val airports = sc.textFile("/datasets/airports.text")
    val COMMA_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
    val airportsInUSA = airports.filter(line =>
      line.split(COMMA_DELIMITER)(3) == "\"United States\"")

    val airportsNameAndCityNames = airportsInUSA.map(line => {
      val splits = line.split(COMMA_DELIMITER)
      splits(1) + ", " + splits(2)
    })

    airportsNameAndCityNames.saveAsTextFile("/outputs/airports_in_usa")
  }
}
