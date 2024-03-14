TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run

JDK_HOME ?= $(HOME)/1/jdk
BOOT_JDK_HOME ?= $(opt)/java-21
include $(TOPDIR)/make/common/OpenJDK.gmk

current_dir = $(shell pwd)

run: clear-logs $(java)
> $(call_java) \
>   -XX:CompileOnly=Test.test \
>   -XX:+PrintIdeal \
>   -XX:CompileCommand=PrintIdealPhase,Test.test,MATCHING,FINAL_CODE \
>   -XX:+PrintOptoAssembly \
>   $(current_dir)/Test.java
.PHONY: run
