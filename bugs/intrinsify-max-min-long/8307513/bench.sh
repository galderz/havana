#!/usr/bin/env bash

set -e -x

CONFIGURE="true"
CLEAN="true"

if [ -z "$TEST" ]; then
    echo "TEST is not defined"
    exit 1
fi

if [ -z "$PATCH_CID" ]; then
    echo "PATCH_COMMIT_ID is not defined"
    exit 1
fi

if [ -z "$BASE_CID" ]; then
    echo "BASE_CID is not defined"
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

check_commit_id()
{
    local jdk_home=$1
    local unresolved_commit_id=$2

    pushd $jdk_home
    latest_commit_id=$(git rev-parse HEAD)
    expected_commit_id=$(git rev-parse "$unresolved_commit_id" 2>/dev/null)
    if [ "$latest_commit_id" != "$expected_commit_id" ]; then
        echo "The latest commit ID does not match the given commit ID."
        echo "Git log for the latest commit (HEAD):"
        GIT_PAGER="cat" git log --oneline -1 "$latest_commit_id"
        echo "Git log for the given commit:"
        GIT_PAGER="cat" git log --oneline -1 "$expected_commit_id"
        exit 1
    fi
    popd
}

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

check_commit_id "$HOME/1/jdk-intrinsify-max-min-long" $PATCH_CID
check_commit_id "$HOME/1/jdk" $BASE_CID
bench $CLEAN $CONFIGURE "$HOME/1/jdk-intrinsify-max-min-long"
bench $CLEAN $CONFIGURE "$HOME/1/jdk"
