#!/usr/bin/env bash

set -e -x

pushd /home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/scratch/0 && \
DBUS_SESSION_BUS_ADDRESS=unix:path=/run/user/1000/bus \
HOME=/home/g \
LANG=en_GB.UTF-8 \
LC_ALL=C \
PATH=/bin:/usr/bin:/usr/sbin \
TEST_IMAGE_DIR=/home/g/1/jdk/build/slow-linux-x86_64/images/test \
XDG_RUNTIME_DIR=/run/user/1000 \
XDG_SESSION_CLASS=user \
XDG_SESSION_ID=3 \
XDG_SESSION_TYPE=tty \
_JVM_DWARF_PATH=/home/g/1/jdk/build/slow-linux-x86_64/images/symbols \
CLASSPATH=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d:/home/g/1/jdk/test/hotspot/jtreg/compiler/interpreter:/ext/opt/jtreg/build/images/jtreg/lib/javatest.jar:/ext/opt/jtreg/build/images/jtreg/lib/jtreg.jar \
    /home/g/1/jdk/build/slow-linux-x86_64/images/jdk/bin/java \
        -Dtest.vm.opts='-XX:MaxRAMPercentage=1.5625 -Dtest.boot.jdk=/home/g/opt/java-21 -Djava.io.tmpdir=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp' \
        -Dtest.tool.vm.opts='-J-XX:MaxRAMPercentage=1.5625 -J-Dtest.boot.jdk=/home/g/opt/java-21 -J-Djava.io.tmpdir=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp' \
        -Dtest.compiler.opts= \
        -Dtest.java.opts= \
        -Dtest.jdk=/home/g/1/jdk/build/slow-linux-x86_64/images/jdk \
        -Dcompile.jdk=/home/g/1/jdk/build/slow-linux-x86_64/images/jdk \
        -Dtest.timeout.factor=4.0 \
        -Dtest.nativepath=/home/g/1/jdk/build/slow-linux-x86_64/images/test/hotspot/jtreg/native \
        -Dtest.root=/home/g/1/jdk/test/hotspot/jtreg \
        -Dtest.name=compiler/interpreter/Test6833129.java \
        -Dtest.file=/home/g/1/jdk/test/hotspot/jtreg/compiler/interpreter/Test6833129.java \
        -Dtest.src=/home/g/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -Dtest.src.path=/home/g/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -Dtest.classes=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d \
        -Dtest.class.path=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d \
        -Dtest.class.path.prefix=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/classes/0/compiler/interpreter/Test6833129.d:/home/g/1/jdk/test/hotspot/jtreg/compiler/interpreter \
        -XX:MaxRAMPercentage=1.5625 \
        -Dtest.boot.jdk=/home/g/opt/java-21 \
        -Djava.io.tmpdir=/home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/tmp \
        -Djava.library.path=/home/g/1/jdk/build/slow-linux-x86_64/images/test/hotspot/jtreg/native \
        -Xbatch \
        -XX:+IgnoreUnrecognizedVMOptions \
        -XX:+DeoptimizeALot -XX:ActiveProcessorCount=8 -XX:+PrintNMethods -XX:TieredStopAtLevel=1 -XX:CompileOnly=compiler/interpreter/Test6833129.clone_and_verify -XX:+TraceDeoptimization -XX:+TraceBytecodes \
        com.sun.javatest.regtest.agent.MainWrapper /home/g/1/jdk/build/slow-linux-x86_64/test-support/jtreg_test_hotspot_jtreg_compiler_interpreter_Test6833129_java/compiler/interpreter/Test6833129.d/main.0.jta
