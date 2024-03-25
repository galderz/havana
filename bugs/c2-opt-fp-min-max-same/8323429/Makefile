TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run

CLASS ?= Test
JDK_HOME ?= $(HOME)/1/jdk-c2-opt-fp-min-max-same
BOOT_JDK_HOME ?= $(opt)/java-21
include $(TOPDIR)/make/common/OpenJDK.gmk

current_dir = $(shell pwd)

run: clear-logs $(java)
> $(call_java) \
>   -ea \
>   -XX:-BackgroundCompilation \
>   -XX:CompileCommand=PrintIdealPhase,$(CLASS).test,MATCHING,FINAL_CODE \
>   -XX:CompileOnly=$(CLASS).test \
>   -XX:+LogCompilation \
>   -XX:+PrintLIRWithAssembly \
>   -XX:+PrintIdeal \
>   -XX:+PrintIR \
>   -XX:+PrintNMethods \
>   -XX:+PrintOptoAssembly \
>   -XX:-UseOnStackReplacement \
>   $(current_dir)/$(CLASS).java
.PHONY: run
