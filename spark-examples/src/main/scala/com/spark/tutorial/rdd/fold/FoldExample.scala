package com.spark.tutorial.rdd.fold

import org.apache.spark.sql.SparkSession

object FoldExample {

  val spark = SparkSession.builder().getOrCreate();
  val sc = spark.sparkContext

  val rdd1 = sc.parallelize(List(("maths", 80), ("science", 90)))
  val additionalMarks = ("extra", 4)

  //val sum = rdd1.fold(additionalMarks){ (acc, marks) => val add = acc._2 + marks._2("total", add) }
}