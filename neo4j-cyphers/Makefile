NEO4J_HOME ?= /opt/neo4j

start-neo4j:
	${NEO4J_HOME}/bin/neo4j console

set-password:
	curl -H "Content-Type: application/json" -X POST -d '{"password":"neo"}' -u neo4j:neo4j http://localhost:7474/user/neo4j/password

basic-tree:
	cat basic-tree.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p neo

# Batch updates to increase performance:
# https://medium.com/neo4j/efficient-neo4j-data-import-using-cypher-scripts-7d1268b0747
batch:
	cat batch.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p neo

# Batch and benefit from script caching using parameters
# https://medium.com/neo4j/efficient-neo4j-data-import-using-cypher-scripts-7d1268b0747
batch-parameters:
	cat batch-parameters.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p neo

clean:
	cat clean.cypher | ${NEO4J_HOME}/bin/cypher-shell -u neo4j -p neo
