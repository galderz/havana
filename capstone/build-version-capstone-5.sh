#!/usr/bin/env bash

set -x -e

cc \
  -Wall \
  -I/Users/galder/opt/capstone-5/include/capstone \
  -c version-test.c \
  -o version-test.o
