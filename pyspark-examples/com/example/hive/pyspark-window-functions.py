import sys
from pyspark.sql import SparkSession
from pyspark.sql.window import Window
from pyspark.sql.functions import *


# create Spark context with Spark configuration
spark = SparkSession \
    .builder \
    .appName("Hive Session Data Frame Join") \
    .enableHiveSupport() \
    .getOrCreate()

import sys
from pyspark.sql.window import Window
import pyspark.sql.functions as func

#
windowSpec = Window.partitionBy(df['category']).orderBy(df['revenue'].desc()).rangeBetween(-sys.maxsize, sys.maxsize)

#
dataFrame = sqlContext.table("productRevenue")

revenue_difference = (func.max(dataFrame['revenue']).over(windowSpec) - dataFrame['revenue'])

dataFrame.select(
    dataFrame['product'],
    dataFrame['category'],
    dataFrame['revenue'],
    revenue_difference.alias("revenue_difference"))
