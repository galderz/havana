#!/usr/bin/env bash

set -e -x

CONFIGURE="true"
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

# Check for configure parameter
if [[ "$2" == "--configure=false" ]]; then
    CONFIGURE="false"
fi

bench()
{
    local clean=$1
    local configure=$2
    local jdk_home=$3

    if [[ $configure == "true" ]]; then
      CONF=release \
        JDK_HOME=$jdk_home \
        make configure
    fi

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

for jdk_home in \
    "$HOME/1/jdk-intrinsify-max-min-long" \
    "$HOME/1/jdk"
do
    bench $CLEAN $CONFIGURE $jdk_home
done
