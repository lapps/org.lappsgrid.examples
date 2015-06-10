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

```xml
<parent>
</parent>
```

# Lapps Interfaces

Lapps Grid Service Wrapping Examples


Stanford NLP Core Tagger Example.

