#!/usr/bin/env bash

set -e -x

pushd jdk
bash configure \
  --disable-warnings-as-errors \
  --with-boot-jdk=/opt/jdk-21+35 \
  --with-extra-cflags=-fcommon \
  --with-jvm-variants=server \
  --with-conf-name=slow \
  --with-debug-level=slowdebug \
  --with-hsdis=capstone \
  --enable-hsdis-bundling \
  --with-capstone=/usr \
  --with-native-debug-symbols=internal
CONF=slow make LONG=info
popd
