Wrapping Web Services
=====================

# Sections To Be Written

- [ ] Maven setup
- [ ] Project setup
- [ ] Lapps interfaces
- [ ] Serializing Lapps objects
- [ ] Implementing required methods
  - [ ] Metadata and annotations
  - [ ] `execute` method
  

# Introduction

This is a short introduction to creating web services that can be deployed to
any Language Grid server.  The [Service Grid](http://servicegrid.net/en/index.html) uses
SOAP for inter-service communications, however the complexities of working with SOAP
have been abstracted away by the Service Grid and Language Grid layers.

# Setup

For this tutorial you will require:

1. Java 1.7
1. Maven 3.0.x
1. An IDE such as IntelliJ or Eclipse

It is assumed that you know how to create a Maven project either using your IDE or via
the command line.  Maven usage is beyond the scope of this tutorial.

## Maven

If you have not done so already, please follow [these instructions](http://lapps.github.io/Maven.html)
for the Maven repository settings used by the Lappsgrid.  Otherwise Maven will not be able to resolve
any of the Lappsgrid artifacts.

***Notes***

1. Please check the [ANC's Nexus repository](http://www.anc.org:8080/nexus) for the 
latest parent pom version.
1. In the future the Lappsgrid artifacts will be deployed to Maven Central.  However,
until that time Lappsgrid artifacts are deployed to the ANC's Nexus instance.

# Project Setup

Create a new Maven project.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.lappsgrid.tutorial</groupId>
  <artifactId>whitespace_tokenizer</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>war</packaging>
  <name>Java Whitespace Tokenizer Example</name>
  <description>
      Lapps Web Services wrapping example for Whitespace Tokenizer
  </description>

  <parent>
      <artifactId>war-parent-pom</artifactId>
      <groupId>org.lappsgrid.maven</groupId>
      <version>2.0.1</version>
  </parent>
```

It is important to use the `org.lappsgrid.maven:war-parent-pom` as it defines the
Service Grid and Lappsgrid dependencies required to implement a service on the Language 
Application Grid.

Also be sure to set the `packaging` to `war`.

# Lappsgrid Interfaces


# Service Implementation

## Serializing to LIF (Lappsgrid Interchange Format)

# Service Metadata

# The war File and web.xml

# Deployment


