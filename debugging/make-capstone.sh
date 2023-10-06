#!/usr/bin/env bash

set -e -x

pushd capstone
./make.sh
sudo ./make.sh install
popd
