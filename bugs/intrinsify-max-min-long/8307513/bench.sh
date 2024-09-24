#!/usr/bin/env bash

set -e -x

CLEAN="true"

if [ -z "$TEST" ]; then
    echo "TEST is not defined"
    exit 1
fi

# Check for clean parameter
if [[ "$1" == "--clean=false" ]]; then
    read -p "Are you sure you want to run without cleaning first? (yes/no): " RESPONSE
    if [[ "$RESPONSE" == "yes" ]]; then
        CLEAN="false"
    else
        echo "Exiting because you don't want to apply the changes."
        exit 1
    fi
fi

bench()
{
    local clean=$1
    local jdk_home=$2

    CONF=release \
      JDK_HOME=$jdk_home \
      make configure

    if [[ $clean == "true" ]]; then
      CONF=release \
        JDK_HOME=$jdk_home \
        make clean-jdk
    fi

    CONF=release \
      TEST="micro:lang.$TEST" \
      MICRO="OPTIONS=$MICRO" \
      JDK_HOME=$jdk_home \
      make test

    CONF=release \
      TEST="micro:lang.$TEST" \
      MICRO="OPTIONS=$MICRO -f 1 -prof perfnorm -prof perfasm" \
      JDK_HOME=$jdk_home \
      make test
}

# Skip ad changes for now
# "$HOME/1/jdk-intrinsify-max-min-long" \

for jdk_home in \
    "$HOME/1/jdk-intrinsify-max-min-long.intrinsic" \
    "$HOME/1/jdk"
do
    bench $CLEAN $jdk_home
done
