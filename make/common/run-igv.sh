#!/bin/bash
set -eux

pushd $1

PATH=$HOME/opt/boot-maven-3/bin:$PATH \
JAVA_HOME=$HOME/opt/java-21 \
sh igv.sh &

popd
