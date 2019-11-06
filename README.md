# etl-service

[![Build status](https://ci.appveyor.com/api/projects/status/krj976ld8acm5ukd/branch/master?svg=true)](https://ci.appveyor.com/project/ramacasturi/etl-service/branch/master)

Continuous Build - https://ci.appveyor.com/project/ramacasturi/etl-service

# Run the service locally
1. Install maven and JDK 1.8.
2. Set JAVA_HOME environment variable to point to the JDK
3. From the root of the repo, build the project from the command line as follows

    <b>mvn clean install</b>
4. The above will also run unit tests. If you just want to run tests, use the following

    <b>mvn test</b>

5. To start the service locally, use the following

    <b>mvn spring-boot:run</b>

    This will keep the service running in the console. To stop the service, use Ctrl-C
6. While service is running, ingest a new catalog upate file using the following API

    <b>curl -X POST http://localhost:8080/stores/{any-store-id}/catalog?catalogFileUri="{fully-qualified-file-name}"</b>

    This will ingest the file within the next 10 secs.
7. To retrieve all the products ingested for a given store, use the following API

    <b>curl http://localhost:8080/stores/{store-id-from-the-post}/products</b>

8. To retrieve a single product ingested for a given store, use the following API

    <b>curl http://localhost:8080/stores/{store-id-from-the-post}/products/{any-product-id-from-the-file}</b>
