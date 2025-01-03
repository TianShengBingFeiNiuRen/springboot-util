#!/bin/sh

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

workspace=$(pwd)

PID=$(ps -ef | grep "${workspace}/lib/${APPLICATION_JAR}" | grep -v grep | awk '{print $2}')
kill -9 ${PID}
