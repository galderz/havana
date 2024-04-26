TOPDIR = ../../..
include $(TOPDIR)/make/common/MakeBase.gmk

default: run-clone

include $(TOPDIR)/make/common/AsyncProfiler.gmk

os = $(shell uname | tr A-Z a-z)
cpu = $(shell uname -m | tr A-Z a-z)

BOOT_JDK_HOME ?= $(opt)/java-21
CONF_FAST ?= fast
CONF_RELEASE ?= release
CONF_SLOW ?= slow
JDK_HOME ?= $(HOME)/1/jdk

args_clone += -XX:CompileOnly=TestArrayClone.test
args_clone += $(current_dir)/$(test_clone)
args_copy += -XX:CompileOnly=TestArrayCopy.test
args_copy += $(current_dir)/$(test_copy)
args_list_clone += -XX:CompileOnly=TestArrayListClone.test
args_list_clone += $(current_dir)/$(test_list_clone)
args_object_clone += -XX:CompileOnly=TestObjectClone.test
args_object_clone += $(current_dir)/$(test_object_clone)
args_uuid_array_clone += -XX:CompileOnly=TestUUIDArrayClone.test
args_uuid_array_clone += $(current_dir)/$(test_uuid_array_clone)
args_array_clone_oome += -XX:CompileOnly=TestArrayCloneOOME.test
args_array_clone_oome += $(current_dir)/$(test_array_clone_oome)
args_multi_clone += -XX:CompileOnly=TestMultiClone.test1,TestMultiClone.test2,
args_multi_clone += $(current_dir)/$(test_multi_clone)
capstone_home = $(opt)/capstone
capstone_makefile = $(capstone_home)/Makefile
capstone_lib = $(capstone_home)/libcapstone.a
conf_args += --with-boot-jdk=$(BOOT_JDK_HOME)
conf_args += --with-jmh=build/jmh/jars
conf_fast_dir = $(CONF_FAST)-$(os)-$(cpu)
conf_release_dir = $(CONF_RELEASE)-$(os)-$(cpu)
conf_686_release_dir = $(CONF_RELEASE)-$(os)-686
conf_slow_dir = $(CONF_SLOW)-$(os)-$(cpu)
conf_686_slow_dir = $(CONF_SLOW)-$(os)-686
current_dir = $(shell pwd)
dot_jmh = $(JDK_HOME)/build/jmh/jars/.jmh
java_fast = $(JDK_HOME)/build/$(conf_fast_dir)/jdk/bin/java
java_release = $(JDK_HOME)/build/$(conf_release_dir)/jdk/bin/java
java_slow = $(JDK_HOME)/build/$(conf_slow_dir)/jdk/bin/java
java_686_slow = $(JDK_HOME)/build/$(conf_686_slow_dir)/jdk/bin/java
jtreg_bin = $(jtreg_home)/bin/jtreg
jtreg_opt = $(opt)/jtreg
jtreg_home = $(jtreg_opt)/build/images/jtreg
jvm_args += -XX:-UseOnStackReplacement
jvm_args += -XX:-BackgroundCompilation
jvm_args += -XX:TieredStopAtLevel=1
#jvm_args += -XX:TraceLinearScanLevel=4
jvm_args += -XX:+PrintIR
jvm_args += -XX:+PrintIRWithLIR
jvm_args += -XX:+PrintLIRWithAssembly
#jvm_args += -XX:+PrintNMethods
jvm_args += -XX:+Verbose
jvm_args += -Xlog:os
#jvm_args += -Xlog:jit*=debug
lldbinit = $(TOPDIR)/make/common/.lldbinit

test_copy = TestArrayCopy.java
test_clone = TestArrayClone.java
test_list_clone = TestArrayListClone.java
test_object_clone = TestObjectClone.java
test_uuid_array_clone = TestUUIDArrayClone.java
test_multi_clone = TestMultiClone.java

test_array_clone_oome = TestArrayCloneOOME.java

run-clone: $(java_slow) $(test_clone) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_clone)
.PHONY: run-clone

run-686-clone: $(java_686_slow) $(test_clone)
> rm -f *.log
> $< $(jvm_args) $(args_clone)
.PHONY: run-686-clone

run-copy: $(java_slow) $(test_copy) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_copy)
.PHONY: run-copy

run-list-clone: $(java_slow) $(test_list_clone) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_list_clone)
.PHONY: run-list-clone

run-object-clone: $(java_slow) $(test_object_clone) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_object_clone)
.PHONY: run-object-clone

run-uuid-array-clone: $(java_slow) $(test_uuid_array_clone) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_uuid_array_clone)
.PHONY: run-uuid-array-clone

run-oome-array-clone: $(java_slow) $(test_array_clone_oome) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) -Xmx32m $(args_array_clone_oome)
.PHONY: oome-array-clone

run-multi-clone: $(java_slow) $(test_multi_clone) $(capstone_lib)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) $(args_multi_clone)
.PHONY: run-multi-clone

all: run-clone run-copy run-list-clone run-object-clone run-uuid-array-clone
.PHONY: all

gdb: $(java_slow) $(test_copy) $(test_clone) $(capstone_lib)
#> LD_LIBRARY_PATH=$(capstone_home) gdb --args $< $(jvm_args) $(args_copy)
> LD_LIBRARY_PATH=$(capstone_home) gdb --args $<
.PHONY: gdb

$(java_fast): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_fast): $(shell find $(JDK_HOME)/src -type f -name '*pp')
$(java_fast): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_fast): $(shell find $(JDK_HOME)/test -type f -name '*pp')
$(java_fast):
> pushd $(JDK_HOME)
> CONF=$(conf_fast_dir) make
> popd
> cp $(lldbinit) $(JDK_HOME)/build/$(conf_fast_dir)
> touch $@

$(java_slow): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_slow): $(shell find $(JDK_HOME)/src -type f -name '*pp')
$(java_slow): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_slow): $(shell find $(JDK_HOME)/test -type f -name '*pp')
$(java_slow):
> pushd $(JDK_HOME)
> CONF=$(conf_slow_dir) make
> popd
> cp $(lldbinit) $(JDK_HOME)/build/$(conf_slow_dir)
> touch $@

$(java_release): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_release): $(shell find $(JDK_HOME)/src -type f -name '*pp')
$(java_release): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_release): $(shell find $(JDK_HOME)/test -type f -name '*pp')
$(java_release):
> pushd $(JDK_HOME)
> CONF=$(conf_release_dir) make
> popd
> cp $(lldbinit) $(JDK_HOME)/build/$(conf_release_dir)
> touch $@

$(java_686_slow): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_686_slow): $(shell find $(JDK_HOME)/src -type f -name '*pp')
$(java_686_slow): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_686_slow): $(shell find $(JDK_HOME)/test -type f -name '*pp')
$(java_686_slow):
> pushd $(JDK_HOME)
> CONF=$(conf_686_slow_dir) make
> popd
> touch $@

$(java_686_release): $(shell find $(JDK_HOME)/src -type f -name '*.java')
$(java_686_release): $(shell find $(JDK_HOME)/src -type f -name '*pp')
$(java_686_release): $(shell find $(JDK_HOME)/test -type f -name '*.java')
$(java_686_release): $(shell find $(JDK_HOME)/test -type f -name '*pp')
$(java_686_release):
> pushd $(JDK_HOME)
> CONF=$(conf_686_release_dir) make
> popd
> cp $(lldbinit) $(JDK_HOME)/build/$(conf_686_release_dir)
> touch $@

config-fast: $(dot_jmh) $(capstone_lib) $(jtreg_bin)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --enable-hsdis-bundling \
>   --with-capstone=$(capstone_home) \
>   --with-conf-name=$(conf_fast_dir) \
>   --with-debug-level=fastdebug \
>   --with-hsdis=capstone \
>   --with-jtreg=$(jtreg_home)
.PHONY: config-fast

config-release: $(dot_jmh) $(jtreg_bin)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --with-conf-name=$(conf_release_dir) \
>   --with-jtreg=$(jtreg_home)
.PHONY: config-release

config-slow: $(dot_jmh) $(capstone_lib) $(jtreg_bin)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --enable-hsdis-bundling \
>   --disable-precompiled-headers \
>   --with-capstone=$(capstone_home) \
>   --with-conf-name=$(conf_slow_dir) \
>   --with-debug-level=slowdebug \
>   --with-hsdis=capstone \
>   --with-jtreg=$(jtreg_home)
.PHONY: config-slow

config-686-slow: $(dot_jmh) $(jtreg_bin)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --disable-precompiled-headers \
>   --with-conf-name=$(conf_686_slow_dir) \
>   --with-debug-level=slowdebug \
>   --with-jtreg=$(jtreg_home) \
>   --with-target-bits=32
.PHONY: config-686-slow

config-686-release: $(dot_jmh) $(jtreg_bin)
> cd $(JDK_HOME)
> bash configure \
>   $(conf_args) \
>   --with-conf-name=$(conf_686_release_dir) \
>   --with-jtreg=$(jtreg_home)
.PHONY: config-686-release

reconfig-slow:
> cd $(JDK_HOME)
> CONF=$(conf_slow_dir) make reconfigure
.PHONY: config-slow

$(dot_jmh):
> cd $(JDK_HOME)
> sh make/devkit/createJMHBundle.sh
> touch $@

$(capstone_lib): $(capstone_makefile)
> cd $(capstone_home)
> ./make.sh

$(capstone_makefile):
> cd $(opt)
> git clone https://github.com/capstone-engine/capstone

$(jtreg_bin): $(jtreg_opt)
> cd $<
> sh make/build.sh --jdk $(opt)/java-21

$(jtreg_opt):
> cd $(opt)
> git clone https://github.com/openjdk/jtreg

commands:
> cd $(JDK_HOME)
> CONF=$(conf_slow_dir) make compile-commands
.PHONY: commands

build:
> cd $(JDK_HOME)
> CONF=$(conf_slow_dir) make
.PHONY: build

micro: $(java_release)
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make test TEST="micro:java.lang.ArrayClone" MICRO="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
.PHONY: micro

micro-686: $(java_686_release)
> cd $(JDK_HOME)
> CONF=$(conf_686_release_dir) make test TEST="micro:java.lang.ArrayClone" MICRO="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
.PHONY: micro-686

micro-c2: $(java_release)
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make test TEST="micro:java.lang.ArrayClone"
.PHONY: micro-c2

test-hotspot-compiler: $(java_release)
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make test TEST="hotspot_compiler" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
.PHONY: test-hotspot-compiler

test-hotspot-compiler-686: $(java_686_release)
> cd $(JDK_HOME)
> CONF=$(conf_686_release_dir) make test TEST="hotspot_compiler" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
.PHONY: test-hotspot-compiler-686

test-all: $(java_release)
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make test TEST="hotspot_compiler hotspot_gc hotspot_serviceability hotspot_runtime tier1 tier2 tier3" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1 -Xcomp"
.PHONY: test-hotspot-compiler

test-single: $(java_slow)
> cd $(JDK_HOME)
# > CONF=$(conf_slow_dir) make test TEST="test/hotspot/jtreg/compiler/arraycopy/TestArrayCopyAsLoadsStores.java" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
# > CONF=$(conf_slow_dir) make test TEST="test/hotspot/jtreg/compiler/codecache/stress/UnexpectedDeoptimizationAllTest.java" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1"
# > CONF=$(conf_slow_dir) make test TEST="test/hotspot/jtreg/compiler/c2/irTests/scalarReplacement/AllocationMergesTests.java"
# > CONF=$(conf_slow_dir) make test TEST="test/hotspot/jtreg/compiler/interpreter/Test6833129.java" JTREG="JAVA_OPTIONS=-XX:CompileOnly=compiler/interpreter/Test6833129.clone_and_verify"
> CONF=$(conf_slow_dir) make test TEST="test/hotspot/jtreg/compiler/c1/TestNullArrayClone.java" JTREG="JAVA_OPTIONS=-XX:+PrintLIR -XX:+PrintCompilation"
.PHONY: test-single

#    TEST                                              TOTAL  PASS  FAIL ERROR
# >> jtreg:test/jdk:tier1                               2369  2368     1     0 <<
# jdk/jfr/event/compiler/TestCompilerCompile.java
regression_tests += test/jdk/jdk/jfr/event/compiler/TestCompilerCompile.java
# >> jtreg:test/jdk:tier2                               4100  4098     1     1 <<
regression_tests += test/jdk/java/security/AccessController/DoPrivAccompliceTest.java
# >> jtreg:test/langtools:tier2                           11    10     1     0 <<
regression_tests += test/langtools/jdk/jshell/FailOverDirectExecutionControlTest.java
# >> jtreg:test/jdk:tier3                               1413  1406     4     3 <<
regression_tests += test/jdk/java/rmi/server/RemoteServer/AddrInUse.java
regression_tests += test/jdk/java/rmi/server/RMISocketFactory/useSocketFactory/unicast/TCPEndpointReadBug.java
regression_tests += test/jdk/jdk/jfr/event/gc/stacktrace/TestMetaspaceParallelGCAllocationPendingStackTrace.java
regression_tests += test/jdk/jdk/jfr/event/gc/stacktrace/TestMetaspaceSerialGCAllocationPendingStackTrace.java
test-regressions: $(java_release)
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make test TEST="$(regression_tests)" JTREG="JAVA_OPTIONS=-XX:TieredStopAtLevel=1 -Xcomp"
.PHONY: test-regressions

ap-clone: $(java_slow) $(test_clone) $(capstone_lib) $(ap_agent)
> rm -f *.log
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) -agentpath:$(ap_agent)=start,event=cpu,file=cpu.html $(args_clone)
.PHONY: ap-clone

ap-copy: $(java_slow) $(test_copy) $(capstone_lib) $(ap_agent)
> rm -f *.log
#> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) -agentpath:$(ap_agent)=start,event=cpu,file=cpu.html $(args_copy)
> LD_LIBRARY_PATH=$(capstone_home) $< $(jvm_args) -agentpath:$(ap_agent)=start,event=LIRGenerator::do_ArrayCopy,file=cpu.html $(args_copy)
.PHONY: ap-copy

clean:
> cd $(JDK_HOME)
> CONF=$(conf_slow_dir) make clean
.PHONY: clean

clean-release:
> cd $(JDK_HOME)
> CONF=$(conf_release_dir) make clean
.PHONY: clean-release

clean-fast:
> cd $(JDK_HOME)
> CONF=$(conf_fast_dir) make clean
.PHONY: clean-fast

clean-686:
> cd $(JDK_HOME)
> CONF=$(conf_686_slow_dir) make clean
.PHONY: clean-686

clean-686-release:
> cd $(JDK_HOME)
> CONF=$(conf_686_release_dir) make clean
.PHONY: clean-686-release
