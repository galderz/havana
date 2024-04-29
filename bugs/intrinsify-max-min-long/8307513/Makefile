TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run

CLASS ?= Test
JDK_HOME ?= $(HOME)/1/jdk-intrinsify-max-min-long
BOOT_JDK_HOME ?= $(opt)/java-22
include $(TOPDIR)/make/common/OpenJDK.gmk

AVX ?=
ifdef AVX
  jvm_args += -XX:UseAVX=$(AVX)
endif

run: clear-logs $(java)
> $(call_java) \
>   $(jvm_args) \
>   -Xbatch \
>   -XX:LoopUnrollLimit=250 \
>   -XX:CompileCommand=exclude,$(CLASS)::main \
>   $(shell pwd)/$(CLASS).java
.PHONY: run

# >   -ea \
# >   -XX:-BackgroundCompilation \
# >   -XX:+LogCompilation \
# >   -XX:+PrintLIRWithAssembly \
# >   -XX:+PrintIdeal \
# >   -XX:+PrintIR \
# >   -XX:+PrintNMethods \
# >   -XX:+PrintOptoAssembly \
# >   -XX:-UseOnStackReplacement \
