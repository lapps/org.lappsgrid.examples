Wrapping Web Services : Step 1
=====================

### Contents

1. [Maven](#maven)
    - [Project Setup](#project-setup)
1. Implementing Lappsgrid service
1. Service metadata
1. Testing a service
1. Packaging a service
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

It is assumed that you know how to create a Maven project either using your IDE or via
the command line.  Maven usage is beyond the scope of this tutorial.

# Maven

Typically users will follow [these instructions](http://lapps.github.io/Maven.html)
to add the ANC's Nexus repositories to their settings.xml file.  However, it this
tutorial we will add the `<respoitories>` section directly to the **pom.xml** file.

***Notes***

1. Please check the [ANC's Nexus repository](http://www.anc.org:8080/nexus) for the 
latest parent pom version.
1. In the future the Lappsgrid artifacts will be deployed to Maven Central.  However,
until that time Lappsgrid artifacts are deployed to the ANC's Nexus instance.

## Project Setup

Create a new Maven project using the following Maven coordinates:

<table>
    <tbody>
        <tr>
            <th align="left">groupId</th>
            <td>org.lappsgrid.tutorial</td>
        </tr>
        <tr>
            <th align="left">artifactId</th>
            <td>whitespace-tokenizer</td>
        </tr>
        <tr>
            <th align="left">version</th>
            <td>1.0.0-SNAPSHOT</td>
        </tr>
    </tbody>
</table>

Add the following Parent POM:

<table>
    <tbody>
        <tr>
            <th align="left">groupId</th>
            <td>org.lappsgrid.maven</td>
        </tr>
        <tr>
            <th align="left">artifactId</th>
            <td>war-parent-pom</td>
        </tr>
        <tr>
            <th align="left">version</th>
            <td>2.0.4</td>
        </tr>
    </tbody>
</table>

Add a dependency to the `org.lappsgrid.all` module:

```xml
<dependency>
    <groupId>org.lappsgrid</groupId>
    <artifactId>all</artifactId>
    <version>2.0.4</version>
</dependency>
```
When you are done your pom file should look like:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.lappsgrid</groupId>
    <artifactId>whitespace_tokenizer</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Java Whitespace Tokenizer Example</name>
    <description>Lapps Web Services wrapping example for Whitespace Tokenizer</description>

    <parent>
        <artifactId>war-parent-pom</artifactId>
        <groupId>org.lappsgrid.maven</groupId>
        <version>2.0.4</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>all</artifactId>
            <version>2.0.4</version>
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

It is important to use the `org.lappsgrid.maven:war-parent-pom` as it defines the
Service Grid and Lappsgrid dependencies required to implement a service on the Language 
Application Grid. Check the [ANC's Nexus Repository](http://www.anc.org:8080/nexus/index.html#nexus-search;quick~org.lappsgrid.maven) to
find the latest version of the Lappsgrid Maven artifacts.

Also be sure to set the `packaging` to `war`.

# Up Next

In Step two of the tutorial we will define the Java class that will implement the 
whitespace tokenzier.

To advance to step two run the command:

```bash
> git checkout Step2-Classes
```
