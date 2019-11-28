clean:
	rm -drf mods/com.greetings

build:
	mkdir -p mods/com.greetings || true
	javac -d mods/com.greetings \
	src/com.greetings/module-info.java \
	src/com.greetings/com/greetings/Main.java

run:
	java --module-path mods -m com.greetings/com.greetings.Main
