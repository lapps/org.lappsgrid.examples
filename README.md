Wrapping Web Services : Step 1
=====================

### Contents

1. [Maven](#maven)
    - [Project Setup](#project-setup)
    - [API Dependency](#api-dependency)
1. Implementing Lappsgrid service
1. Service metadata
1. Packaging a service
1. Testing a service
1. Wrapping Java package
1. Wrapping Python package

# Introduction

This is a short introduction to creating web services that can be deployed to
any Language Grid server.  The [Service Grid](http://servicegrid.net/en/index.html) uses
SOAP for inter-service communications, however the complexities of working with SOAP
have been abstracted away by the Service Grid and Language Grid layers.

# Requirements

For this tutorial you will require:

1. Java 1.7
1. Maven 3.0.x
1. An IDE such as IntelliJ or Eclipse
1. About 15 minues

# Maven

It is assumed that you know how to create a Maven project either using your IDE or via
the command line.  Maven usage is beyond the scope of this tutorial.

## Project Setup

Let's start with creating a new maven project using the following maven coordinates:

A basic template for a maven project should look like this:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    ...
</project>
```

As a maven project, our service will have these.

Field | Value
---|---
**groudID**|org.lappsgrid.tutorial
**artifactId**|lappsgrid_example
**version**|1.0.0-SNAPSHOT
**packaging**|war (`.war` is for web apps)
**name**|Lappsgrid Service Example

Which inherits from the following Parent POM:

Field | Value
---|---
**groudID**|org.lappsgrid.maven
**artifactId**|war-parent-pom
**version**|2.0.4

At this point `pom.xml` for our example service should look like this:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- current project definition -->
    <groupId>org.lappsgrid</groupId>
    <artifactId>lappsgrid_example</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Lappsgrid Service Example</name>
    <description>Lapps Web Services wrapping example</description>
    <!-- inheritance info -->
    <parent>
        <artifactId>war-parent-pom</artifactId>
        <groupId>org.lappsgrid.maven</groupId>
        <version>2.0.4</version>
    </parent>

</project>
```

***Note*** 

1. To get the right version number, please check the [ANC's Nexus repository](http://www.anc.org:8080/nexus/index.html#nexus-search;gav~org.lappsgrid.maven~war-parent-pom~~~).
(As of October 2015, the latest is 2.0.4 .)

## API Dependency

To make it easier for anyone to create a Lappsgrid web service, Lappsgrid dev team provides various API's. Currently packages of those APIs are hosted on ANC's Nexus repository.

### Repository set-up

Typically users will follow [these instructions](http://lapps.github.io/Maven.html) to add the ANC's Nexus repositories to their settings.xml file as a global Maven configuration.  However, in this tutorial we will add the `<respoitories>` section directly to the `pom.xml` file **locally**.

Here's how to add additional maven repositories.
We can add additional repositories using `<repositories>` tag inside the `<project>`.

```xml
<project ...>
    <repositories>
        <repository>
            <id>anc-releases</id>
            <url>http://www.anc.org:8080/nexus/content/repositories/releases/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>anc-snapshots</id>
            <url>http://www.anc.org:8080/nexus/content/repositories/snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
</project>
```
***Notes***

1. In the future the Lappsgrid artifacts will be deployed to Maven Central.  However, until that time Lappsgrid artifacts are deployed to the ANC's Nexus instance.


### Adding dependencies

All of core APIs for Lappsgrid services are included `org.lappsgrid.all` package. Add a dependency to it to the current project:

```xml
...
<project ...>
    <dependencies>
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>all</artifactId>
            <version>2.1.1</version>
        </dependency>
    </dependencies>
</project>
...
```

***Notes***

1. To get the right version number, please check the [ANC's Nexus repository](http://www.anc.org:8080/nexus/index.html#nexus-search;gav~org.lappsgrid~all~~~). 
(As of October 2015, the latest is 2.1.1 .)

After all, your `pom.xml` file should look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- current project definition -->
    <groupId>org.lappsgrid</groupId>
    <artifactId>lappsgrid_example</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Lappsgrid Service Example</name>
    <description>Lapps Web Services wrapping example</description>
    <!-- inheritance info -->
    <parent>
        <artifactId>war-parent-pom</artifactId>
        <groupId>org.lappsgrid.maven</groupId>
        <version>2.0.4</version>
    </parent>
    <!-- API dependency -->
    <dependencies>
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>all</artifactId>
            <version>2.1.1</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>anc-releases</id>
            <url>http://www.anc.org:8080/nexus/content/repositories/releases/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>anc-snapshots</id>
            <url>http://www.anc.org:8080/nexus/content/repositories/snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
</project>
```

Again, it is important to use the `org.lappsgrid.maven:war-parent-pom` as it defines the Service Grid and Lappsgrid dependencies required to implement a service on the Language Application Grid. 
Check the [ANC's Nexus Repository](http://www.anc.org:8080/nexus/index.html#nexus-search;gav~org.lappsgrid.maven~war-parent-pom~~~) to find the latest version of the Lappsgrid Maven artifacts.

Also be sure to set the `packaging` to war (Web App Archive) to make it a web-app.

# Up Next

In Step two of the tutorial we will define the Java class that will implement the 
whitespace tokenzier.

To advance to step two run the command:

```bash
> git checkout step2
```

Or follow the link: [step2](https://github.com/lapps/org.lappsgrid.examples/tree/step2)
