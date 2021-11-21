#!/bin/sh
cd /data/stock-market-macd-calculate-60min/
./stop.sh
nohup java -jar -XX:+UseConcMarkSweepGC stock-market-macd-calculate-60min-0.0.1-SNAPSHOT.jar >nohup.out 2>&1 &
