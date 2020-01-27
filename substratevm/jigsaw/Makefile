all: clean build run

clean:
	rm -drf mods || true
	rm -drf mlib || true
	mkdir -p mods/io.mendrugo || true
	mkdir mlib || true

# Note how mdep module-path/upgrade-path and add-exports are shared with build target.
# --add-exports is needed because the graal's maven artifacts do not include module-info!
# You can verify this with `jar --describe-module`
build:
	javac \
	--module-path mdep \
	--upgrade-module-path mdep \
	--add-exports=jdk.internal.vm.compiler/org.graalvm.compiler.serviceprovider=io.mendrugo \
	-d mods/io.mendrugo \
	-verbose \
	src/io.mendrugo/module-info.java \
	src/io.mendrugo/io/mendrugo/Main.java

# Note how mdep module-path/upgrade-path and add-exports are shared with build target.
# --add-exports is needed because the graal's maven artifacts do not include module-info!
# You can verify this with `jar --describe-module`
run:
	java \
	-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI \
	--module-path mdep \
	--upgrade-module-path mdep \
	--add-exports=jdk.internal.vm.compiler/org.graalvm.compiler.serviceprovider=io.mendrugo \
	--module-path mods \
	-m io.mendrugo/io.mendrugo.Main

download-modules:
	rm -drf mdep || true; \
	mkdir mdep || true; \
	cd mdep; \
	wget https://repo1.maven.org/maven2/org/graalvm/compiler/compiler/19.3.1/compiler-19.3.1.jar; \
	wget https://repo1.maven.org/maven2/org/graalvm/sdk/graal-sdk/19.3.1/graal-sdk-19.3.1.jar; \
	wget https://repo1.maven.org/maven2/org/graalvm/truffle/truffle-api/19.3.1/truffle-api-19.3.1.jar

describe:
	jar --describe-module --file=mdep/compiler-19.3.1.jar
