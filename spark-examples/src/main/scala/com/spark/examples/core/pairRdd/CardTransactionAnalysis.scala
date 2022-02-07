package com.spark.examples.core.pairRdd

import com.spark.examples.commons.Utils
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
object CardTransactionAnalysis extends App {


  //Logger.getLogger("org").setLevel(Level.ERROR)
  val conf = new SparkConf().setAppName("CardTransactionAnalysis").setMaster("local[3]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  /**
   * Analysis Datasource
   *
   */
  val lines = sc.textFile("/datasets/card_transactions.csv")
  val cleanedLines = lines.filter(line => !line.contains("card_num"))
  //cleanedLines.take(5).foreach(println)

  val rawPairRDD = cleanedLines.map(line => (
    line.split(Utils.COMMA_DELIMITER)(0).toInt,
    line.split(Utils.COMMA_DELIMITER)(1),
    line.split(Utils.COMMA_DELIMITER)(2),
    line.split(Utils.COMMA_DELIMITER)(3),
    line.split(Utils.COMMA_DELIMITER)(4).toLong,
    line.split(Utils.COMMA_DELIMITER)(5)
  ))

  rawPairRDD.take(5).foreach(println)
  val inputPairRDD = rawPairRDD.filter(x => {
    (1580515200 <= x._5) && (x._5 < 1583020800)
  })
  println(inputPairRDD.count())

  /**
   * # Total amount spent by each user
   */
  val totalAmountByUser = inputPairRDD.map(x => (x._6, x._1)).reduceByKey((x, y) => (x + y))
  println()
  println("Total amount spent by each user : ")
  totalAmountByUser.take(4).foreach(println)

  /**
   * # Total amount spent by each user for each of their cards
   */
  val totalAmountByCardTypeUser = inputPairRDD.map(x => ((x._6, x._2), x._1)).reduceByKey((x, y) => (x + y))
  println()
  println("Total amount spent by each user for each of their cards : ")
  totalAmountByCardTypeUser.take(4).foreach(println)

  /**
   * # Total amount spend by each user for each of their cards on each category
   */
  val amountByCardAndCatorgoyforUser = inputPairRDD.map(x => ((x._6, x._2, x._3), x._1)).reduceByKey((x, y) => (x + y))
  println()
  println("Total amount spent by each user for each of their cards : ")
  amountByCardAndCatorgoyforUser.take(4).foreach(println)

  /**
   * # Distinct list of categories in which the user has made expenditure
   */

  def initialize(value: String): Set[String] = {
    var set = Set(value)
    return set
  }

  def add(agg: Set[String], value: String): Set[String] = {
    agg.add(value)
    return agg
  }

  def merge(agg1: Set[String], agg2: Set[String]): Set[String] = {
    var set = agg1.union(agg2)
    return set
  }

  val userCategoryRDD = inputPairRDD.map(x => (x._6, x._3))
  val userCategories = userCategoryRDD.combineByKey(initialize, add, merge)
  println()
  println("Distinct list of categories in which the user has made expenditure : ")
  userCategories.take(4).foreach(println)


  /**
   * # Category in which the user has made the maximum expenditure
   */
  val userCategoryAmount = inputPairRDD.map(x => ((x._6, x._3), x._1))
  val user_category_expense_rdd = userCategoryAmount.map(x => (x._1._1, (x._1._2, x._2)))

  def getMaxAmount(tuple1: (String, Int), tuple2: (String, Int)): (String, Int) = {
    if (tuple1._2 > tuple2._2) {
      return tuple1
    } else {
      return tuple2
    }
  }

  val userCategoryWiseMaxExpenseRDD = user_category_expense_rdd.reduceByKey((x, y) => getMaxAmount(x, y))
  println()
  println("Category in which the user has made the maximum expenditure ")
  userCategoryWiseMaxExpenseRDD.sortByKey().take(10).foreach(println)

  Thread.sleep(3600000)

}
