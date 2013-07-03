How to Use
----------

To build the projects:

    mvn clean install


To start server the example server:

    cd ./trecgenomics.datasource/
    mvn jetty:run

To run a simple client-side test querying the example server:

    cd ./trecgenomics.client/
    mvn compile exec:java -Dexec.mainClass=org.lapps.datasource.trecgenomics.HelloDatasourceTest
