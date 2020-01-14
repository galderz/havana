package j.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

public class EmbeddedNeo4j
{
    private static final File databaseDirectory = new File("target/neo4j-hello-db");

    public static GraphDatabaseService create() {
        deleteRecursively();
        GraphDatabaseSettings.BoltConnector bolt = GraphDatabaseSettings.boltConnector( "0" );

        return new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(databaseDirectory)
            .setConfig( bolt.type, "BOLT" )
            .setConfig( bolt.enabled, "true" )
            .setConfig( bolt.address, "localhost:7787" )
            .newGraphDatabase();
    }

    private static void deleteRecursively()
    {
        try
        {
            FileUtils.deleteRecursively(databaseDirectory);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
