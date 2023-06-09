#!/bin/sh
source /etc/profile

APPLICATION="springboot-util"
APPLICATION_JAR="${APPLICATION}.jar"

JAVA_OPT="-server -XX:NewRatio=2 -XX:SurvivorRatio=8 -Xms8g -Xmx16g"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"

JVM_REMOTE_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7766"

nohup java ${JAVA_OPT} ${JVM_REMOTE_OPTS} -jar ./boot/${APPLICATION_JAR} > nohup.out 2>&1 &