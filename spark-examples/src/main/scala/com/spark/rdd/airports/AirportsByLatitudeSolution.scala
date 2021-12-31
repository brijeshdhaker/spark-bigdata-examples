package com.spark.rdd.airports

import com.spark.commons.Utils
import org.apache.spark.{SparkConf, SparkContext}
/**

%SPARK_HOME%\bin\spark-submit ^
 --master yarn ^
 --deploy-mode cluster ^
 --properties-file %SPARK_HOME%\conf\spark-yarn.conf ^
 --class com.sparkTutorial.rdd.airports.AirportsByLatitudeSolution target\spark-training-1.0.jar

 */
object AirportsByLatitudeSolution {

  def main(args: Array[String]) {

    val COMMA_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
    var SPARK_YARN_JARS = "hdfs://node-master:9000/spark-lib/*.jar"
    val SPARK_YARN_ARCHIVE = "hdfs://node-master:9000/spark-archive/spark-assembly-2.2.0.zip"
    val YARN_APP_CLASSPATH = "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_YARN_HOME,$HADOOP_YARN_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*"


    System.setProperty("SPARK_YARN_MODE", "true")
    System.setProperty("SPARK_USER", "Spark")
    System.setProperty("MAX_APP_ATTEMPTS", "1")

    val conf = new SparkConf()
      .setAppName("AirportsByLatitudeSolution").setMaster("yarn")
      .set("spark.local.dir","/tmp")
      .set("spark.driver.memory", "512m")
      .set("spark.yarn.am.memory", "512m")
      .set("spark.executor.memory", "512m")
      .set("spark.files.overwrite", "true")
      .set("spark.hadoop.yarn.application.classpath",YARN_APP_CLASSPATH)

    val sc = new SparkContext(conf)

    val airports = sc.textFile("/datasets/airports-small.text")
    val airportsInUSA = airports.filter(line => line.split(Utils.COMMA_DELIMITER)(6).toFloat > 40)

    val airportsNameAndCityNames = airportsInUSA.map(line => {
      val splits = line.split(Utils.COMMA_DELIMITER)
      splits(1) + ", " + splits(6)
    })

    airportsNameAndCityNames.saveAsTextFile("/outputs/airports_by_latitude")

    System.out.println("    Brijesh K Dhaker    ")
    System.out.println("=== Print out schema ===")

    System.out.println("=== Print records where the response is from Afghanistan ===")
    System.out.println("=== Print the count of occupations ===")
    System.out.println("=== Print records with average mid age less than 20 ===")
    System.out.println("=== Print the result by salary middle point in descending order ===")
    System.out.println("=== Group by country and aggregate by average salary middle point ===")
    System.out.println("=== With salary bucket column ===")

    System.out.println("=== Stop Spark Context ===")
    sc.stop()

  }

}
