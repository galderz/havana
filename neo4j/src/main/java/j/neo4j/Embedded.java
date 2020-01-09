package j.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

public class Embedded
{

    private static final File databaseDirectory = new File("target/neo4j-hello-db");

    private enum RelTypes implements RelationshipType
    {
        KNOWS
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        FileUtils.deleteRecursively(databaseDirectory);

        GraphDatabaseService graphDb;
        Node firstNode;
        Node secondNode;
        Relationship relationship;

        GraphDatabaseSettings.BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );

        graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(databaseDirectory)
            .setConfig( bolt.type, "BOLT" )
            .setConfig( bolt.enabled, "true" )
            .setConfig( bolt.address, "localhost:7787" )
            .newGraphDatabase();
        ;

        registerShutdownHook(graphDb);

        try (Transaction tx = graphDb.beginTx())
        {
            firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");
            secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "brave Neo4j ");

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.print(secondNode.getProperty("message"));

            tx.success();
        }

        while (true)
            Thread.sleep(1000);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread(graphDb::shutdown));
    }
}
