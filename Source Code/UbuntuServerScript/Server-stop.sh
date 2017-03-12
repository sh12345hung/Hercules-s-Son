#!/bin/sh
LOG_FILE=data/log.txt
PID_FILE=data/pid.txt

PID=$(cat $PID_FILE)
if [ $PID -eq -1 ]
then
	echo "Server isn't running." > $LOG_FILE
else
	kill $PID > $LOG_FILE
	echo -1 > $PID_FILE
	echo "Server is stopped." > $LOG_FILE
fi
