NEO4J_HOME ?= /opt/neo4j

set-password:
	curl -H "Content-Type: application/json" -X POST -d '{"password":"neo"}' -u neo4j:neo4j http://localhost:7474/user/neo4j/password

basic-tree:
	cat basic-tree.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p p

clean:
	cat clean.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p p
