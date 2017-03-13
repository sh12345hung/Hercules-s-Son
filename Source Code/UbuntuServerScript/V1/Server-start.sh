#!/bin/sh
OUTPUT_FILE=data/output.txt
LOG_FILE=data/log.txt
ERROR_FILE=data/error.txt
PID_FILE=data/pid.txt
JAR_FILE=/usr/local/bin/MultiConnServer.jar

PID=$(cat $PID_FILE)
if [ $PID -eq -1 ]
then
	echo "Server starting..." > $LOG_FILE
	nohup java -jar $JAR_FILE >> $OUTPUT_FILE 2>$ERROR_FILE < /dev/null & PID=$!
	echo "Server started, process id: $PID" > $LOG_FILE
else
	echo "Server process is already existed, process id: $PID" > $LOG_FILE
fi
echo $PID > $PID_FILE
