#!/bin/sh

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

workspace=$(cd $(dirname $0); pwd)

PID=$(ps -ef | grep "${workspace}/lib/${APPLICATION_JAR}" | grep -v grep | awk '{print $2}')
if [ -n "${PID}" ]; then
    echo "Stopping process:${APPLICATION_JAR} [${PID}]"
    kill -9 ${PID}
    echo "Process ${APPLICATION_JAR} [${PID}] stopped."
else
    echo "No process found for ${APPLICATION_JAR}"
fi
