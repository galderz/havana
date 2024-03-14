#!/usr/bin/env bash

set -e -x

pushd /Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/scratch/0 && \
HOME=/Users/galder \
LANG=en_GB.UTF-8 \
LC_ALL=C \
PATH=/bin:/usr/bin:/usr/sbin \
TEST_IMAGE_DIR=/Users/galder/1/jdk/build/slow-darwin-arm64/images/test \
CLASSPATH=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d:/Users/galder/1/jdk/test/hotspot/jtreg/compiler/interpreter:/Users/galder/opt/jtreg/build/images/jtreg/lib/javatest.jar:/Users/galder/opt/jtreg/build/images/jtreg/lib/jtreg.jar \
    /Users/galder/1/jdk/build/slow-darwin-arm64/images/jdk/bin/java \
        -Dtest.vm.opts='-XX:MaxRAMPercentage=5 -Dtest.boot.jdk=/Users/galder/opt/java-21 -Djava.io.tmpdir=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp' \
        -Dtest.tool.vm.opts='-J-XX:MaxRAMPercentage=5 -J-Dtest.boot.jdk=/Users/galder/opt/java-21 -J-Djava.io.tmpdir=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp' \
        -Dtest.compiler.opts= \
        -Dtest.java.opts=-XX:CompileOnly=compiler/interpreter/Test6833129.clone_and_verify \
        -Dtest.jdk=/Users/galder/1/jdk/build/slow-darwin-arm64/images/jdk \
        -Dcompile.jdk=/Users/galder/1/jdk/build/slow-darwin-arm64/images/jdk \
        -Dtest.timeout.factor=4.0 \
        -Dtest.nativepath=/Users/galder/1/jdk/build/slow-darwin-arm64/images/test/hotspot/jtreg/native \
        -Dtest.root=/Users/galder/1/jdk/test/hotspot/jtreg \
        -Dtest.name=compiler/interpreter/Test6833129.java \
        -Dtest.file=/Users/galder/1/jdk/test/hotspot/jtreg/compiler/interpreter/Test6833129.java \
        -Dtest.src=/Users/galder/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -Dtest.src.path=/Users/galder/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -Dtest.classes=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d \
        -Dtest.class.path=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d \
        -Dtest.class.path.prefix=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d:/Users/galder/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -XX:MaxRAMPercentage=5 \
        -Dtest.boot.jdk=/Users/galder/opt/java-21 \
        -Djava.io.tmpdir=/Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp \
        -XX:CompileOnly=compiler/interpreter/Test6833129.clone_and_verify \
        -Djava.library.path=/Users/galder/1/jdk/build/slow-darwin-arm64/images/test/hotspot/jtreg/native \
        -Xbatch \
        -XX:+IgnoreUnrecognizedVMOptions \
        -XX:+DeoptimizeALot \
        -XX:+DeoptimizeALot -XX:ActiveProcessorCount=8 -XX:+PrintNMethods -XX:TieredStopAtLevel=1 -XX:CompileOnly=compiler/interpreter/Test6833129.clone_and_verify -XX:+TraceDeoptimization -XX:+TraceBytecodes \
        com.sun.javatest.regtest.agent.MainWrapper /Users/galder/1/jdk/build/slow-darwin-arm64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/compiler/interpreter/Test6833129.d/main.0.jta
