package j.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public class EmbeddedBasicTree
{
    private enum Relationship implements RelationshipType
    {
        LEFT, RIGHT
    }

    private enum Type implements Label {
        Node
    }

    public static void main(String[] args) throws InterruptedException
    {
        final GraphDatabaseService graph = EmbeddedNeo4j.create();
        try
        {
            try (Transaction tx = graph.beginTx())
            {
                final Node a = graph.createNode(Type.Node);
                a.setProperty("name", "A");

                final Node b = graph.createNode(Type.Node);
                b.setProperty("name", "B");

                final Node c = graph.createNode(Type.Node);
                c.setProperty("name", "C");

                a.createRelationshipTo(b, Relationship.LEFT);
                a.createRelationshipTo(c, Relationship.RIGHT);

                tx.success();
            }

            while (true) {
                Thread.sleep(1000);
            }
        }
        finally
        {
            graph.shutdown();
        }
    }

}
