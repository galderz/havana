clean:
	rm -drf mods

build-separate:
	mkdir -p mods/org.astro mods/com.greetings || true

	javac \
	-d mods/org.astro \
	src/org.astro/module-info.java \
	src/org.astro/org/astro/World.java

	javac \
	--module-path mods \
	-d mods/com.greetings \
	src/com.greetings/module-info.java \
	src/com.greetings/com/greetings/Main.java

build:
	mkdir mods || true

	javac \
	-d mods \
	--module-source-path src \
	$$(find src -name "*.java")

run:
	java --module-path mods -m com.greetings/com.greetings.Main

package:
	jar \
	--create \
	--file=mlib/org.astro@1.0.jar \
	--module-version=1.0 \
	-C mods/org.astro .

	jar \
	--create \
	--file=mlib/com.greetings.jar \
	--main-class=com.greetings.Main \
	-C mods/com.greetings .

run-jar:
	java -p mlib -m com.greetings

describe:
	jar --describe-module --file=mlib/org.astro@1.0.jar
	jar --describe-module --file=mlib/com.greetings.jar
