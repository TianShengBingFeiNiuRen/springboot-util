#!/bin/sh
source /etc/profile
cd /data/springboot-util
./stop.sh
nohup java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8868 -jar -XX:+UseConcMarkSweepGC springboot-util-release.jar >nohup.out 2>&1 &
