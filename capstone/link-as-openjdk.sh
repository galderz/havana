#!/usr/bin/env bash

set -x -e

/usr/bin/clang \
  -mmacosx-version-min=11.00.00 \
  -arch arm64 \
  -L/Users/galder/1/jdk/build/fast/support/modules_libs/java.base \
  -L/Users/galder/1/jdk/build/fast/support/modules_libs/java.base/server \
  -dynamiclib \
  -compatibility_version 1.0.0 \
  -current_version 1.0.0 \
  -L/Users/galder/opt/capstone/lib \
  -dynamiclib \
  -compatibility_version 1.0.0 \
  -current_version 1.0.0 \
  -Wl,-install_name,@rpath/libhsdis.dylib \
  -isysroot /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.0.sdk \
  -iframework /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.0.sdk/System/Library/Frameworks \
  -o /Users/galder/1/jdk/build/fast/support/hsdis/libhsdis.dylib \
  /Users/galder/1/jdk/build/fast/support/hsdis/hsdis-capstone.o \
  -lcapstone
