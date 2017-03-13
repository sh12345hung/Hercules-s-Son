#!/bin/sh
#
# @Author: Thao Nguyen Manh
# @Date: Mar 13th, 2017
# @Description: Script file that control a java application (jar file) process
#
SCRIPTNAME=Server
OUTPUT_FILE=data/output.txt
LOG_FILE=data/log.txt
ERROR_FILE=data/error.txt
PID_FILE=data/pid.txt
JAR_PATH=/usr/local/bin/MultiConnServer.jar

# Start server process and get PID to store in data/pid.txt
start() {
    PID=$(cat $PID_FILE)
    if [ $PID -eq -1 ]
    then
        echo "$(date): Server starting..." >> $LOG_FILE
        nohup java -jar $JAR_PATH >> $OUTPUT_FILE 2>$ERROR_FILE < /dev/null & PID=$!
        echo "$(date): Server started, process id: $PID" >> $LOG_FILE
    else
        echo "$(date): Server process is already existed, process id: $PID" >> $LOG_FILE
    fi
    echo $PID > $PID_FILE
}

# Stop server process with PID store in data/pid.txt
stop() {
    PID=$(cat $PID_FILE)
    if [ $PID -eq -1 ]
    then
        echo "$(date): Server isn't running." >> $LOG_FILE
    else
        kill $PID
        echo -1 > $PID_FILE
        echo "$(date): Server is stopped." >> $LOG_FILE
    fi
}

# Restart the server process
restart() {
    start
}

# Get the server process status
status() {
    cat data/log.txt
}

# Clear log, error, output file
clear() {
  echo "" > $LOG_FILE
  echo "" > $ERROR_FILE
  echo "" > $OUTPUT_FILE
}

### MAIN ###
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart|reload|reboot)
        restart
        ;;
    status)
        status
        ;;
    clear|clean)
        clear
        ;;
    *)
        echo "Usage: $SCRIPTNAME {start|stop|restart|reload|reboot|status|clear|clean}"
        exit 1
esac
exit 0
