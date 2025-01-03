#!/bin/sh
source /etc/profile

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

workspace=$(pwd)

JAVA_OPT="-server -XX:NewRatio=2 -XX:SurvivorRatio=8 -Xms8g -Xmx16g"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"

JVM_REMOTE_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7766"

echo "$(date +'%Y-%m-%d %H:%M:%S') 等待启动 ${APPLICATION}..."
nohup java ${JAVA_OPT} ${JVM_REMOTE_OPTS} -jar ${workspace}/lib/${APPLICATION_JAR} > /dev/null 2>&1 &
sleep 5s
echo "$(date +'%Y-%m-%d %H:%M:%S') ${APPLICATION} started..."

# sed -i 's/\r$//' filename.sh