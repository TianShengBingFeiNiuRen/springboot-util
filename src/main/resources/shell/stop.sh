#!/bin/sh
ps -ef | grep springboot-util-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}' | xargs kill -9
