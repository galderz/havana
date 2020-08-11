all: run

clean:
	rm -f jcstress-results-*
	mvn clean

run: clean
	mvn install
	java -jar target/jcstress.jar
	open results/index.html


