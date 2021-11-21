#!/bin/sh
ps -ef | grep stock-market-macd-calculate-60min-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}' | xargs kill -9
