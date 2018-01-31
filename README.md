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

1. Java 1.7 or later
1. Maven 3.0.x
1. An IDE such as IntelliJ or Eclipse
1. About 15 minues

# Maven

It is assumed that you know how to create a Maven project either using your IDE or via
the command line. Maven usage is beyond the scope of this tutorial.

## Project Setup

A basic template for a maven project should look like this:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    ...
</project>
```

Let's start with creating a new maven project using the following properties:

Field | Value
---|---
**groudID**|org.lappsgrid.tutorial
**artifactId**|lappsgrid_example
**version**|1.0.0-SNAPSHOT
**packaging**|war
**name**|Lappsgrid Service Example

Setting the `packaging` to `war` (Web Application Archive) ensures that Maven creates a web application from the specifications in the pom file.

The project inherits from the following Parent POM:

Field | Value
---|---
**groudID**|org.lappsgrid.maven
**artifactId**|war-parent-pom
**version**|2.0.5

It is important to use the `org.lappsgrid.maven:war-parent-pom` parent as it defines the Service Grid and Lappsgrid dependencies required to implement a service on the Language Application Grid. To get the right version number, please check the [Maven Central repository](http://mvnrepository.com/artifact/org.lappsgrid.maven/war-parent-pom). As of may 2017, the latest version is 2.0.5.

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
        <version>2.0.5</version>
    </parent>

</project>
```

## API Dependency

To make it easier for anyone to create a Lappsgrid web service, the Lappsgrid development team provides various API's.

### Repository set-up

All public APIs to help writing Lappsgrid web services are released on [Maven Central](http://mvnrepository.com/artifact/org.lappsgrid). Thus developers don't have to set up additional repositories. 

### Adding dependencies

All core APIs for Lappsgrid services are included in the `org.lappsgrid.all` package. 

Add a dependency to it to the current project:

```xml
...
<project ...>
    <dependencies>
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>all</artifactId>
            <version>2.3.1</version>
        </dependency>
    </dependencies>
</project>
...
```

Note that the Lappsgrid core APIs require java 7 features. Hence, to use those APIs, one needs to configure the compiler to use the proper java version by adding `maven-compiler-plugin` plugin as follows: 

```xml
...
<project ...>
    <dependencies>
    ...
    </dependencies>

    <build>
    ...
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>1.7</source>
                    <target>1.7</target>
                </configuration>
            </plugin>
        </plugins>>
        ...
    </build>
    ...
</project>

```

After this, your `pom.xml` file should look like:

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
        <version>2.0.5</version>
    </parent>
    <!-- API dependency -->
    <dependencies>
        <dependency>
            <groupId>org.lappsgrid</groupId>
            <artifactId>all</artifactId>
            <version>2.3.1</version>
        </dependency>
    </dependencies>
    <!-- plugin used at the compile time -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>1.7</source>
                    <target>1.7</target>
                </configuration>
            </plugin>
        </plugins>>
    </build>
</project>
```


# Up Next

In Step two of the tutorial we will define the Java class that will implement the 
whitespace tokenzier.

To advance to step two run the command:

```bash
> git checkout step2
```

Or follow the link: [step2](https://github.com/lapps/org.lappsgrid.examples/tree/step2)
