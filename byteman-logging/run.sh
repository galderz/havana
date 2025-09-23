#!/usr/bin/env bash

set -x -e

mvn clean package
java -javaagent:$HOME/opt/byteman/lib/byteman.jar=script:src/main/resources/logging.btm \
    -jar target/byteman-logging-1.0-SNAPSHOT.jar
