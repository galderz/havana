```bash
$ ./mvnw io.quarkus:quarkus-maven-plugin:0.16.1:create
$ ./mvnw compile quarkus:dev
$ curl http://localhost:8080
...
$ curl http://localhost:8080/hello
hello
```

Modify HelloResource and reload endpoint:

```bash
$ curl http://localhost:8080/hello
hola
```

Add more extensions:

```bash
./mvnw quarkus:list-extensions
...
./mvnw quarkus:add-extension -Dextensions=jdbc-postgresql,panache,openapi,resteasy-jsonb,swagger-ui
```

Create developer resource:

```java
@Path("/developer")
public class DeveloperResource {}
```

Return list of developers:

```java
@GET
@Produces(MediaType.APPLICATION_JSON)
public List<Developer> developers()
{
    return Developer.listAll();
}
```

Start DB:

```bash
docker run \
  --ulimit memlock=-1:-1 \
  -it \
  --rm=true \
  --memory-swappiness=0 \
  --name sparksjoy \
  -e POSTGRES_USER=sparksjoy \
  -e POSTGRES_PASSWORD=sparksjoy \
  -e POSTGRES_DB=sparksjoy \
  -p 5432:5432 \
  postgres:10.5
```

Add DB details to `application.properties`:

```
quarkus.datasource.url=jdbc:postgresql:sparksjoy
quarkus.datasource.driver=org.postgresql.Driver
quarkus.datasource.username=sparksjoy
quarkus.datasource.password=sparksjoy
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true
```

Connect to DB externally:

```bash
docker exec -it sparksjoy bash
psql -U sparksjoy
\dt
Did not find any relations.
```

Go to Firefox and open `http://localhost:8080/developer` (Firefox presents JSON very nicely).

If you go to DB, you should see table created:

```bash
\dt
           List of relations
 Schema |   Name    | Type  |   Owner
--------+-----------+-------+-----------
 public | developer | table | sparksjoy
(1 row)
```

You can describe the table and see that an `id` has been added:

```bash
\d developer
                     Table "public.developer"
 Column |          Type          | Collation | Nullable | Default
--------+------------------------+-----------+----------+---------
 id     | bigint                 |           | not null |
 name   | character varying(255) |           |          |
Indexes:
    "developer_pkey" PRIMARY KEY, btree (id)
```

Checking the database contents are empty:

```bash
SELECT * from developer;
 id | name
----+------
(0 rows)
```

Add an endpoint to store new developers:

```java
@Transactional
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Developer newDeveloper(Developer developer)
{
    developer.id = null;
    developer.persist();
    return developer;
}
```

Using OpenAPI, all our JSON endpoints are documented.
Using Swagger, we can exercise those endpoints easily.
Go to `http://localhost:8080/swagger-ui`.
Use `POST /developer` endpoint to store a developer named `Galder`.

Then, check `http://localhost:8080/developer` to verify the developer is there.

Also check the DB:

```bash
SELECT * from developer;
 id |  name
----+--------
  1 | Galder
(1 row)
```

Add more developers via swagger and verify they're there.

Use repository approach to find developers that are Spanish.
Create a method that returns only developer whose name is `Galder`.
Next, add a boolean `@QueryParam` to `/developer` to find Spanish developers.

Try `http://localhost:8080/developer` and `http://localhost:8080/developer?spanish=true` URLs,
and notice the difference on what is returned.
