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
>   -XX:CompileCommand=compileonly,$(CLASS)::test \
>   -XX:+TraceNewVectors \
>   -XX:+TraceSuperWord \
>   $(shell pwd)/$(CLASS).java
.PHONY: run

test-perf: $(java)
> cd $(JDK_HOME)
> CONF=$(CONF) make test TEST="test/hotspot/jtreg/compiler/loopopts/superword/ReductionPerf.java"
.PHONY: test-perf

# >   -ea \
# >   -XX:-BackgroundCompilation \
# >   -XX:+LogCompilation \
# >   -XX:+PrintLIRWithAssembly \
# >   -XX:+PrintIdeal \
# >   -XX:+PrintIR \
# >   -XX:+PrintNMethods \
# >   -XX:+PrintOptoAssembly \
# >   -XX:-UseOnStackReplacement \
