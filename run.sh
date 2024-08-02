#!/bin/bash

JAR=food_recipe-0.0.1-SNAPSHOT.jar
LOG=/home/ubuntu/food/data/back.log

mkdir -p /home/ubuntu/food/data/

echo "BACK started."

nohup java -jar $JAR > $LOG 2>&1 &

echo "loop start"
while :
do
    sleep 1
done