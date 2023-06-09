#!/bin/sh

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{print $2}')
kill -9 ${PID}
