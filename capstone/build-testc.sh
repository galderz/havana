#!/usr/bin/env bash

set -x -e

/usr/bin/clang \
  -Daarch64 \
  -D_LP64=1 \
  -I/Users/galder/opt/capstone/include/capstone \
  -DCAPSTONE_ARCH=CS_ARCH_AARCH64 \
  -DCAPSTONE_MODE=CS_MODE_ARM \
  -g \
  -gdwarf-4 \
  -gdwarf-aranges \
  -Wno-unknown-warning-option \
  -Wno-unused-parameter \
  -Wno-unused \
  -Wno-undef \
  -Wno-format-nonliteral \
  -Werror \
  -isysroot /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.0.sdk \
  -iframework /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.0.sdk/System/Library/Frameworks \
  -c \
  -o test.o \
  test.c \
  -frandom-seed="hsdis-capstone.c"
