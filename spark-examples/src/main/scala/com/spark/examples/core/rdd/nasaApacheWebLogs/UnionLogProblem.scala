package com.spark.examples.core.rdd.nasaApacheWebLogs

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object UnionLogProblem {

  def main(args: Array[String]) {

    /* "in/nasa_19950701.tsv" file contains 10000 log lines from one of NASA's apache server for July 1st, 1995.
       "in/nasa_19950801.tsv" file contains 10000 log lines for August 1st, 1995
       Create a Spark program to generate a new RDD which contains the log lines from both July 1st and August 1st,
       take a 0.1 sample of those log lines and save it to "out/sample_nasa_logs.tsv" file.

       Keep in mind, that the original log files contains the following header lines.
       host	logname	time	method	url	response	bytes

       Make sure the head lines are removed in the resulting RDD.
     */

      Logger.getLogger("org").setLevel(Level.OFF)

      val sparkConf = new SparkConf().setMaster("local[*]").setAppName("UnionLogProblem")
      val sc = new SparkContext(sparkConf);

      val julyLogs = sc.textFile("file:/E:/apps/hostpath/spark/in/nasa_19950701.tsv")
      val augLogs = sc.textFile("file:/E:/apps/hostpath/spark/in/nasa_19950801.tsv")
      val aggregatedLogs = julyLogs.union(augLogs)

      val filteredLogs = aggregatedLogs.filter(line => isNotHeader(line))

      val logs = filteredLogs.sample(withReplacement = true, fraction = 0.1)
      logs.foreach(log => println(log))

  }

  def isNotHeader(line: String): Boolean = !(line.startsWith("host") && line.contains("bytes"))

}
