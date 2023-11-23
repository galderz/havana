#!/usr/bin/env bash

set -x -e

cd $HOME/1/jdk

bash configure \
  --with-boot-jdk=$HOME/opt/java-21 \
  --enable-hsdis-bundling \
  --with-capstone=$HOME/opt/capstone \
  --with-conf-name=fast \
  --with-debug-level=fastdebug \
  --with-hsdis=capstone

CONF=fast make build-hsdis
