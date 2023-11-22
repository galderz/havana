#!/usr/bin/env bash

set -x -e

/usr/bin/clang \
  -MMD \
  -MF /Users/galder/1/jdk/build/fast/support/hsdis/hsdis-capstone.d.tmp \
  -I/Users/galder/1/jdk/build/fast/support/modules_include/java.base \
  -I/Users/galder/1/jdk/build/fast/support/modules_include/java.base/darwin \
  -I/Users/galder/1/jdk/src/java.base/share/native/libjava \
  -I/Users/galder/1/jdk/src/java.base/unix/native/libjava \
  -I/Users/galder/1/jdk/src/hotspot/share/include \
  -I/Users/galder/1/jdk/src/hotspot/os/posix/include \
  -DMAC_OS_X_VERSION_MIN_REQUIRED=110000 \
  -mmacosx-version-min=11.00.00 \
  -DLIBC=default \
  -D_ALLBSD_SOURCE \
  -D_DARWIN_UNLIMITED_SELECT \
  -DMACOSX \
  -DDEBUG \
  -Wall \
  -Wextra \
  -Wformat=2 \
  -Wpointer-arith \
  -Wsign-compare \
  -Wreorder \
  -Wunused-function \
  -Wundef \
  -Wunused-value \
  -Woverloaded-virtual \
  -g \
  -gdwarf-4 \
  -gdwarf-aranges \
  -std=c11 \
  -arch arm64 \
  -D_LITTLE_ENDIAN \
  -DARCH='"aarch64"' \
  -Daarch64 \
  -D_LP64=1 \
  -I/Users/galder/opt/capstone/include/capstone \
  -DCAPSTONE_ARCH=CS_ARCH_AARCH64 \
  -DCAPSTONE_MODE=CS_MODE_ARM \
  -I/Users/galder/1/jdk/src/utils/hsdis/capstone \
  -I/Users/galder/1/jdk/src/utils/hsdis \
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
  -o /Users/galder/1/jdk/build/fast/support/hsdis/hsdis-capstone.o \
  /Users/galder/1/jdk/src/utils/hsdis/capstone/hsdis-capstone.c \
  -frandom-seed="hsdis-capstone.c"
