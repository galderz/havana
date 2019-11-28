clean:
	rm -drf mods

build:
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

run:
	java --module-path mods -m com.greetings/com.greetings.Main
