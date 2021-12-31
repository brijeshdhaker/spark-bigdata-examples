package com.spark.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object StackOverFlowSurvey {

  val AGE_MIDPOINT = "age_midpoint"
  val SALARY_MIDPOINT = "salary_midpoint"
  val SALARY_MIDPOINT_BUCKET = "salary_midpoint_bucket"

  val COMMA_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
  var SPARK_YARN_JARS = "hdfs://node-master:9000/spark-lib/*.jar"
  val SPARK_YARN_ARCHIVE = "hdfs://node-master:9000/spark-archive/spark-assembly-2.2.0.zip"
  val YARN_APP_CLASSPATH = "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_YARN_HOME,$HADOOP_YARN_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*"

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.INFO)

    val spark: SparkSession = SparkSession.builder()
      .appName("StackOverFlowSurvey")
      .config("spark.sql.warehouse.dir", "file:///e:/apps/hostpath/spark/spark-warehouse")
      //.master("local[*]")
      //.config("mapred.remote.os", "Linux")
      //.config("os.name", "Linux")
      //.config("spark.local.dir","/tmp")
      //.config("spark.files.overwrite", "true")
      //.config("spark.driver.memory", "1024m")
      //.config("spark.yarn.am.memory", "1024m")
      //.config("spark.executor.memory", "1024m")
      //.config("spark.yarn.jars", SPARK_YARN_JARS)
      //.config("spark.yarn.archive", SPARK_YARN_ARCHIVE)
      //.config("spark.hadoop.yarn.application.classpath",YARN_APP_CLASSPATH)
      .getOrCreate()

    val dataFrameReader = spark.read
    val responses = dataFrameReader.option("header", "true")
                    .option("inferSchema", value = true)
                    .csv("in/2016-stack-overflow-survey-responses.csv")

    System.out.println("=== Print out schema ===")
    responses.printSchema()

    System.out.println("=== Print records where the response is from Afghanistan ===")
    val selectedColumns = responses.select("country","occupation",AGE_MIDPOINT, SALARY_MIDPOINT)
    selectedColumns.filter(selectedColumns.col("country").===("Afghanistan")).show()

    System.out.println("=== Print the count of occupations ===")
    selectedColumns.count()

    System.out.println("=== Print records with average mid age less than 20 ===")
    selectedColumns.filter(selectedColumns.col(SALARY_MIDPOINT) < 20).show()

    System.out.println("=== Print the result by salary middle point in descending order ===")
    selectedColumns.orderBy(selectedColumns.col(SALARY_MIDPOINT).desc).show()

    System.out.println("=== Group by country and aggregate by average salary middle point ===")
    val groupByCountry = selectedColumns.groupBy(selectedColumns.col("country"))
    groupByCountry.avg(SALARY_MIDPOINT).show()

    System.out.println("=== With salary bucket column ===")

    System.out.println("=== Group by salary bucket ===")

    spark.stop()
  }
}
