#!/bin/sh
ps -ef | grep springboot-util-release.jar | grep -v grep | awk '{print $2}' | xargs kill -9
