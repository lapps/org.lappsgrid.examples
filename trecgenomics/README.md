How to Use
----------

To build the projects:

    mvn clean install


To start server the example server:

    cd ./datasource-trecgenomics/
    mvn jetty:run

To run a simple client-side test querying the example server:

    cd ./datasource-client/
    mvn compile exec:java -Dexec.mainClass=org.lapps.datasource.trecgenomics.HelloDatasourceTest
