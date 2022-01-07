#!/usr/bin/python2

from kafka import KafkaProducer
from random import randint
from time import sleep
import sys
from com.example.models.Transaction import Transaction

BROKER = 'quickstart-bigdata:9092'
TOPIC = 'tweeter-tweets'

WORD_FILE = '/usr/share/dict/words'
WORDS = open(WORD_FILE).read().splitlines()

try:
    p = KafkaProducer(bootstrap_servers=BROKER)
except Exception as e:
    print("ERROR --> {e}")
    sys.exit(1)

while True:
    message = ''
    for _ in range(randint(2, 7)):
        message += WORDS[randint(0, len(WORDS)-1)] + ' '
    print(">>> '{}'".format(message))
    p.send(TOPIC, message)
    sleep(randint(1, 4))

