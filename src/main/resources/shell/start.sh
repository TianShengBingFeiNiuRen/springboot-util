#!/bin/sh
source /etc/profile
cd /data/springboot-util
./stop.sh
nohup java -jar -XX:+UseConcMarkSweepGC springboot-util-0.0.1-SNAPSHOT.jar >nohup.out 2>&1 &
