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
