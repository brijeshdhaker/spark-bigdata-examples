package com.spark.tutorial.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**

%SPARK_HOME%\bin\spark-submit ^
 --properties-file %SPARK_HOME%\conf\spark-local-yarn.conf ^
 --class com.sparkTutorial.sparkSql.HousePriceSolution target\spark-training-scala-1.0.jar


 %SPARK_HOME%\bin\spark-submit ^
 --class com.sparkTutorial.sparkSql.HousePriceSolution target\spark-training-scala-1.0.jar


 */
object HousePriceSolution {

  val PRICE_SQ_FT = "Price SQ Ft"

  def main(args: Array[String]) {

    //Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("HousePriceSolution")
      .master("local[*]")
      .config("spark.sql.codegen.wholeStage", "false")
      .config("spark.sql.warehouse.dir","spark-warehouse")
      .getOrCreate()

    val realEstate = session.read
      .option("header", "true")
      .option("inferSchema", value = true)
      .csv("/datasets/RealEstate.csv")

    realEstate.groupBy("Location")
      .avg(PRICE_SQ_FT)
      .orderBy("avg(Price SQ Ft)")
      .show()

    realEstate.explain(true)

    //Thread.sleep(86400000) //throws InterruptedException.

    session.stop()
  }
}
