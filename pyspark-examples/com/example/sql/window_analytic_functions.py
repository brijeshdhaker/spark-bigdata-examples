import sys
from pyspark.sql import SparkSession
from pyspark.sql.window import Window
from pyspark.sql.functions import *


# create Spark context with Spark configuration
spark = SparkSession \
    .builder \
    .appName("PySpark Window Functions") \
    .enableHiveSupport() \
    .getOrCreate()


# Create Data Frame From Hive table
df = spark.table("PRODUCT_REVENUE")
df.show()


windowSpec = Window.partitionBy(df['category']).orderBy(df['revenue'].desc())

""" cume_dist """
from pyspark.sql.functions import cume_dist
df.withColumn("cume_dist", cume_dist().over(windowSpec)).show()

"""lag"""
from pyspark.sql.functions import lag
df.withColumn("lag", lag("revenue", 2).over(windowSpec)).show()

"""lead"""
from pyspark.sql.functions import lead
df.withColumn("lead", lead("revenue", 2).over(windowSpec)).show()

#
spark.stop()