package com.spark.hdp

import org.apache.spark.sql.SparkSession

object SparkOnHDPTest {
  def main(args: Array[String]): Unit = {

    // WinUtils Exe Path (Hadoop Home)
    //System.setProperty("hadoop.home.dir", "C:/Java/")

    val spark = SparkSession.builder()
      .appName("Cloudera Spark Job")
      .master("yarn")
      .config("spark.hadoop.fs.defaultFS","hdfs://quickstart-bigdata:8020")
      .config("spark.hadoop.yarn.resourcemanager.address","quickstart-bigdata:8032")
      .config("spark.yarn.archive","hdfs:///user/brijeshdhaker/archives/spark-2.4.0.zip")
      //.config("spark.yarn.jars","hdfs://quickstart-bigdata:8020/user/brijeshdhaker/jars/spark-2.4.0//*.jar")
      .config("spark.executor.memory","512m")
      .config("spark.executor.instances","2")
      //.config("spark.executor.cores","1")
      //.config("spark.hadoop.yarn.application.classpath", "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*,$HADOOP_YARN_HOME/*,$HADOOP_YARN_HOME/lib/*")
      //.config("spark.hadoop.yarn.application.classpath", "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*,$HADOOP_YARN_HOME/*,$HADOOP_YARN_HOME/lib/*")
      .getOrCreate()

    println("====== Spark Sample Job ======")
    println("====== Spark Sample Job ======")
    println("====== Spark Sample Job ======")
    println("====== Spark Sample Job ======")
    println("====== Spark Sample Job ======")

    //val employees = spark.read.csv("/user/data/sample.txt").toDF("id", "name", "salary")
    //employees.show(false)

  }
}
