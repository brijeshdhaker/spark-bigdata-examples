package com.spark.examples.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**

%SPARK_HOME%\bin\spark-submit ^
 --properties-file %SPARK_HOME%\conf\spark-local-yarn.conf ^
 --class com.sparkTutorial.sparkSql.HousePriceSolution target\spark-training-scala-1.0.jar


 %SPARK_HOME%\bin\spark-submit ^
 --class com.sparkTutorial.sparkSql.HousePriceSolution target\spark-training-scala-1.0.jar


*/

/*  Create a Spark program to read the house data from in/RealEstate.csv,
        group by location, aggregate the average price per SQ Ft and sort by average
        price per SQ Ft.

        The houses dataset contains a collection of recent real estate listings in
        San Luis Obispo  county and around it.
        The dataset contains the following fields:
        1. MLS: Multiple listing service number for the house (unique ID).
        2. Location: city/town where the house is located. Most locations are in San Luis Obispo county and northern Santa Barbara county (Santa Maria Orcutt, Lompoc, Guadelupe, Los Alamos), but there some out of area locations as well.
        3. Price: the most recent listing price of the house (in dollars).
        4. Bedrooms: number of bedrooms.
        5. Bathrooms: number of bathrooms.
        6. Size: size of the house in square feet.
        7. Price/SQ.ft: price of the house per square foot.
        8. Status: type of sale. Thee types are represented in the dataset: Short Sale, Foreclosure and Regular.

        Each field is comma separated.

        Sample output:

        +----------------+-----------------+
        |        Location| avg(Price SQ Ft)|
        +----------------+-----------------+
        |          Oceano|             95.0|
        |         Bradley|            206.0|
        | San Luis Obispo|            359.0|
        |      Santa Ynez|            491.4|
        |         Cayucos|            887.0|
        |................|.................|
        |................|.................|
        |................|.................|

*/

object HousePriceSolution {

  Logger.getLogger("org").setLevel(Level.ERROR)

  val PRICE_SQ_FT = "Price SQ Ft"


  def main(args: Array[String]) {

    //Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("HousePriceSolution")
      .master("local[*]")
      .config("spark.sql.codegen.wholeStage", "false")
      .config("spark.sql.warehouse.dir","spark-warehouse")
      .getOrCreate()
    session.sparkContext.setLogLevel("WARN")

    val realEstate = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("/datasets/RealEstate.csv")

    realEstate.groupBy("Location")
      .avg(PRICE_SQ_FT)
      .orderBy("avg(Price SQ Ft)")
      .show(false)

    realEstate.explain(true)

    Thread.sleep(86400000) //throws InterruptedException.

    session.stop()
  }
}
