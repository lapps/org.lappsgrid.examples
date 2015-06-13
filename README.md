Wrapping Web Services : Step 2
=====================

# Sections To Be Written

- [x] Maven setup
- [x] Project setup
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

# Requirements

For this tutorial you will require:

1. Java 1.7
1. Maven 3.0.x
1. An IDE such as IntelliJ or Eclipse
1. About 15 minues

It is assumed that you know how to create a Maven project either using your IDE or via
the command line.  Maven usage is beyond the scope of this tutorial.


# Up Next

In Step three of the tutorial we add annotations to our implementation class to
automatically generate the JSON metadata returned by the `getMetadata()` method.

To advance to step two run the command:

```bash
> git checkout Step3-Metadata
```