#!/usr/bin/env bash

set -e -x

bench()
{
    local jdk_home=$1

    CONF=release \
      JDK_HOME=$jdk_home \
      make configure clean-jdk

    CONF=release \
      TEST="micro:lang.MinMaxReductionBench.singleLongMax" \
      MICRO="OPTIONS=-p size=10000" \
      JDK_HOME=$jdk_home \
      make test

    CONF=release \
      TEST="micro:lang.MinMaxReductionBench.singleLongMax" \
      MICRO="OPTIONS=-p size=10000 -f 1 -prof perfnorm" \
      JDK_HOME=$jdk_home \
      make test

    CONF=release \
      TEST="micro:lang.MinMaxReductionBench.singleLongMax" \
      MICRO="OPTIONS=-p size=10000 -f 1 -prof perfasm" \
      JDK_HOME=$jdk_home \
      make test
}

for jdk_home in \
    "$HOME/1/jdk" \
    "$HOME/1/jdk/jdk-intrinsify-max-min-long.intrinsic" \
    "$HOME/1/jdk/jdk-intrinsify-max-min-long"
do
    bench $jdk_home
done