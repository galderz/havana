#!/usr/bin/env bash

set -e -x

if [ -z "$TEST" ]; then
    echo "TEST is not defined"
    exit 1
fi

bench()
{
    local jdk_home=$1

    CONF=release \
      JDK_HOME=$jdk_home \
      make configure clean-jdk

    CONF=release \
      TEST="micro:lang.$TEST" \
      MICRO="OPTIONS=-p size=10000" \
      JDK_HOME=$jdk_home \
      make test

    CONF=release \
      TEST="micro:lang.$TEST" \
      MICRO="OPTIONS=-p size=10000 -f 1 -prof perfnorm -prof perfasm" \
      JDK_HOME=$jdk_home \
      make test
}

for jdk_home in \
    "$HOME/1/jdk-intrinsify-max-min-long" \
    "$HOME/1/jdk-intrinsify-max-min-long.intrinsic" \
    "$HOME/1/jdk"
do
    bench $jdk_home
done
