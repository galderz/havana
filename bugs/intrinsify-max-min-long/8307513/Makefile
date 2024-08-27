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

SVE ?=
ifdef SVE
  jvm_args += -XX:UseSVE=$(SVE)
endif

jvm_args += -Xbatch
jvm_args += -Xlog:os
jvm_args += -XX:CompileCommand=compileonly,$(CLASS)::test
jvm_args += -XX:CompileCommand=TraceAutoVectorization,$(CLASS)::test,ALL
jvm_args += -XX:CompileCommand=PrintIdealPhase,$(CLASS)::test,"OPTIMIZE_FINISHED"
jvm_args += -XX:-BackgroundCompilation
jvm_args += -XX:+LogCompilation
jvm_args += -XX:+PrintAssembly
jvm_args += -XX:+TraceNewVectors

# Other args
#jvm_args += -XX:+PrintCompilation

OSR ?= true
ifeq ($(OSR),false)
  jvm_args += -XX:-UseOnStackReplacement
endif

IGV ?=
ifdef IGV
  jvm_args += -XX:PrintIdealGraphLevel=$(IGV)
endif

run: clear-logs $(java)
> $(call_java) \
>   $(jvm_args) \
>   $(shell pwd)/$(CLASS).java
.PHONY: run

perf: $(java)
> cd $(JDK_HOME)
> CONF=$(conf_name) make test TEST="test/hotspot/jtreg/compiler/loopopts/superword/ReductionPerf.java"
> grep -E -i -w '^(long max|long min)' $(java_build_dir)/test-support/jtreg_test_hotspot_jtreg_compiler_loopopts_superword_ReductionPerf_java/compiler/loopopts/superword/ReductionPerf.jtr
.PHONY: perf

rr: $(java)
> rr $(java) \
>   -XX:CompileCommand=printcompilation,$(CLASS)::test \
>   -XX:CompileCommand=compileonly,$(CLASS)::test \
>   -Xbatch \
>   -XX:+TraceLoopOpts \
>   -XX:+TraceSuperWord \
>   -XX:LoopMaxUnroll=5 \
>   $(shell pwd)/$(CLASS).java
.PHONY: rr

# Other flags used:
# >   -ea \
# >   -XX:CompileCommand=print,$(CLASS)::test \
# >   -XX:+LogCompilation \
# >   -XX:+PrintCFG \
# >   -XX:+PrintIdeal \
# >   -XX:+PrintIR \
# >   -XX:+PrintLIRWithAssembly \
# >   -XX:+PrintNMethods \
# >   -XX:+PrintOptoAssembly \
# >   -XX:+PrintIdeal \
# >   -XX:+PrintIntrinsics \
# >   -XX:+TraceLoopOpts \
# >   -XX:+TraceNewVectors \
# >   -XX:+TraceOptoParse \
# >   -XX:+TraceSuperWord \
# >   -XX:CompileCommand=PrintIdealPhase,$(CLASS)::test,"BEFORE_MACRO_EXPANSION,AFTER_MACRO_EXPANSION_STEP"

# Other env variables used:
> DYLD_PRINT_LIBRARIES=1 \
