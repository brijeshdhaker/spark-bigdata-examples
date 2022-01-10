# If running on local, use below package to allow kafka source/sink
# pyspark --packages org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.5

# Create the kafka topics

PATH=$PATH:/home/ec2-user/confluent-5.5.0/bin
kafka-topics \
--create \
--bootstrap-server ec2-52-66-45-236.ap-south-1.compute.amazonaws.com:9092 \
--replication-factor 1 \
--partitions 1 \
--topic lines

# 1581098400 ==> Feb  7, 2020 18:00:00 GMT
# 1581098420 ==> Feb  7, 2020 18:00:20 GMT

# Produce some messages

kafka-console-producer \
--broker-list ec2-52-66-45-236.ap-south-1.compute.amazonaws.com:9092 \
--topic lines

{"ts": 1581098400, "content": "apple orange"}
{"ts": 1581098403, "content": "melon pear"}
{"ts": 1581098406, "content": "apple orange"}
{"ts": 1581098409, "content": "melon pear"}
{"ts": 1581098412, "content": "apple orange"}
{"ts": 1581098415, "content": "melon pear"}


"""
If the max ts of message is M
and late threshold is L
then window till (M - L) is finalized

1. So far the window till 18:00:10 should have been finalized (also the watermark is 1581098415 - 5 == 18:00:10)

Expected windows in the output:
17:59:55 - 18:00:05
18:00:00 - 18:00:10

2. Window till 18:00:15 will be finalized when watermark moves there or crosses it
3. Below records will have no impact

{"ts": 1581098417, "content": "apple orange"}
{"ts": 1581098419, "content": "melon pear"}

4. Also, this record will have no impact on the word count of window 18:00:00 - 18:00:10 (as it is before the watermark)
This will only affect the window 18:00:05 - 18:00:15 (once that window is finalized)

{"ts": 1581098409, "content": "melon pear"}

5. But this record will cause the finalization
{"ts": 1581098420, "content": "apple orange"}

New window:
18:00:05 - 18:00:15
"""

# Delete the kafka topic so as a cleanup activity

kafka-topics \
--bootstrap-server ec2-52-66-45-236.ap-south-1.compute.amazonaws.com:9092 \
--delete \
--topic lines