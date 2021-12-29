package com.spark.tutorial.sql.join

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


object InnerJoinExample {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.OFF)
    //spark.sparkContext.setLogLevel("OFF")
    
    val spark: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("InnerJoinExample")
      .getOrCreate()

    val employee = Seq(
      (101,"Chloe",-1,"2018",8,"M",3000),
      (102,"Paul",101,"2010",3,"M",4000),
      (103,"John",101,"2010",1,"M",1000),
      (104,"Lisa",102,"2005",2,"F",2000),
      (105,"Evan",102,"2010",7,"",-1),
      (106,"Amy",102,"2010",9,"",-1)
    )
    val empColumns = Seq("id","name","superior_emp_id","year_joined","deptno","gender","salary")

    val department = Seq(
      ("Marketing",1),
      ("Sales",2),
      ("Engineering",3),
      ("Finance",4),
      ("IT",5),
      ("HR",6)
    )
    val deptColumns = Seq("dept_name","deptno")
    import spark.sqlContext.implicits._

    val employeeDF = employee.toDF(empColumns:_*)
    employeeDF.show(false)

    val departmentDF = department.toDF(deptColumns:_*)
    departmentDF.show(false)
    
    println("1. Inner Join Using Expression : join(right: Dataset[_]): DataFrame")
    employeeDF.join(departmentDF).show()
    
    println("2. Inner Join Using Expression : join(right: Dataset[_], usingColumn: String): DataFrame")
    employeeDF.join(departmentDF, "deptno").show()
    
    println("3. Inner Join Using Expression : join(right: Dataset[_], usingColumns: Seq[String], joinType: String): DataFrame")
    employeeDF.join(departmentDF, Seq("deptno")).show()
    
    println("4. Inner Join Using Expression : join(right: Dataset[_], usingColumns: Seq[String], joinType: String): DataFrame")
    employeeDF.join(departmentDF, Seq("deptno"), "inner").show()

    println("5. Inner Join Using Expression : join(right: Dataset[_], joinExprs: Column): DataFrame")
    employeeDF.as("E").join(departmentDF.as("D"), $"E.deptno" === $"D.deptno").show()
    
    
    println("6. Inner Join Using Columns : join(right: Dataset[_], usingColumns: Seq[String], joinType: String): DataFrame")
    employeeDF.join(departmentDF, employeeDF("deptno") ===  departmentDF("deptno"), "inner").show(false)
    
    
    println("7. Inner Join Using Columns : SQL Query Expression")
    employeeDF.createOrReplaceTempView("EMP")
    departmentDF.createOrReplaceTempView("DEPT")
    val joinDF = spark.sql("select * from EMP e, DEPT d where e.deptno == d.deptno")
    joinDF.show(false)
    
  }

}
