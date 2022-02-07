package com.spark.examples.core.pairRdd

import com.spark.examples.commons.Utils
import com.spark.examples.core.pairRdd.CardTransactionAnalysis.totalAmountByUser
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.Set

/*
    Read the file from storage (/FileStore/tables/card_transactions.json)
    File has json records
    Each record has fields:
      amount
      card_num
      category
      merchant
      ts
      user_id

*/
object CarLoanApplicationAnalysis extends App {


  //Logger.getLogger("org").setLevel(Level.ERROR)
  val conf = new SparkConf().setAppName("CarLoanApplicationAnalysis").setMaster("local[3]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  /**
   * Analysis Datasource
   *
   */
  val lines = sc.textFile("/datasets/auto_loan.csv")
  val cleanedLines = lines.filter(line => !line.contains("car_price"))
  //cleanedLines.take(5).foreach(println)

  val rawPairRDD = cleanedLines.map(line => (
    line.split(Utils.COMMA_DELIMITER)(0),                       // Application ID
    line.split(Utils.COMMA_DELIMITER)(1),                       // Customer_ID
    line.split(Utils.COMMA_DELIMITER)(2).toInt,                 // Amount
    line.split(Utils.COMMA_DELIMITER)(3),                       // Car Type
    line.split(Utils.COMMA_DELIMITER)(4),                       // Location
    line.split(Utils.COMMA_DELIMITER)(5),                       // Loan Date
    line.split(Utils.COMMA_DELIMITER)(6)                        // Loan Status
  ))
  //rawPairRDD.take(5).foreach(println)

  /*
   1. The month in which maximum loan requests were submitted in the last one year [2019-04-01 to 2020-03-31]
   */
  val resultPairRDD = rawPairRDD.filter(x => (("2019-04-01" <= x._6) && (x._6 < "2020-04-01"))).map(x => (x._6.split("-")(1), 1)).reduceByKey((x,y) => x+y)
  val countMonthRdd = resultPairRDD.map(x => (x._1.toInt, x._2.toInt))

  println("The month in which maximum loan requests were submitted in the last one year [2019-04-01 to 2020-03-31] : ")
  //resultPairRDD.takeOrdered(12)(Ordering[Int].reverse.on(x => x._2)).foreach(println)
  val sortedRdd = countMonthRdd.sortBy[Int](_._2, false)
  //Just to display results from RDD
  sortedRdd.collect().toList.foreach(println)


  /*
   2. Max, Min and Average number of applications submitted per customer id
   */


  /*
   3. Top 10 highest car price against which applications got approved
   */


   /*
    4. For each customer location, top 5 car models which have most loan applications in the last month
   */

  Thread.sleep(3600000)

}
