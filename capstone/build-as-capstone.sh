#!/usr/bin/env bash

set -x -e

cc \
  -Wall \
  -I/Users/galder/opt/capstone/include \
  -DCAPSTONE_HAS_ARM \
  -DCAPSTONE_HAS_AARCH64 \
  -DCAPSTONE_HAS_M68K \
  -DCAPSTONE_HAS_MIPS \
  -DCAPSTONE_HAS_POWERPC \
  -DCAPSTONE_HAS_SPARC \
  -DCAPSTONE_HAS_SYSZ \
  -DCAPSTONE_HAS_X86 \
  -DCAPSTONE_HAS_XCORE \
  -DCAPSTONE_HAS_TMS320C64X \
  -DCAPSTONE_HAS_M680X \
  -DCAPSTONE_HAS_EVM \
  -DCAPSTONE_HAS_RISCV \
  -DCAPSTONE_HAS_WASM \
  -DCAPSTONE_HAS_MOS65XX \
  -DCAPSTONE_HAS_BPF \
  -DCAPSTONE_HAS_TRICORE \
  -DCAPSTONE_HAS_SH \
  -c /Users/galder/opt/capstone/tests/test_aarch64.c \
  -o test_aarch64.o
