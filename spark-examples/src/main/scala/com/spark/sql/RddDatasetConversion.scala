package com.spark.sql

import com.spark.commons.Utils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object RddDatasetConversion {

  def main(args: Array[String]) {
    
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val session = SparkSession.builder()
                  .appName("StackOverFlowSurvey")
                  .getOrCreate()

    val lines = session.sparkContext.textFile("in/2016-stack-overflow-survey-responses.csv")

    val responseRDD = lines
      .filter(line => !line.split(Utils.COMMA_DELIMITER, -1)(2).equals("country"))
      .map(line => {
        val splits = line.split(Utils.COMMA_DELIMITER, -1)
        com.spark.sql.Response(splits(2), toInt(splits(6)), splits(9), toInt(splits(14)))
      })

    import session.implicits._
    val responseDataset = responseRDD.toDS()

    System.out.println("=== Print out schema ===")
    responseDataset.printSchema()

    System.out.println("=== Print 20 records of responses table ===")
    responseDataset.show(20)

    for (response <- responseDataset.rdd.collect()) println(response)
  }

  def toInt(split: String): Option[Double] = {
    if (split.isEmpty) None else Some(split.toDouble)
  }
}
