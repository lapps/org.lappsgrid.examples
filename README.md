Wrapping a LAPPS service
=====================

# Introduction

This is a short introduction to creating web services that can be deployed to
any Language Grid server.  The [Service Grid](http://servicegrid.net/en/index.html) uses
SOAP for inter-service communications, however the complexities of working with SOAP
have been abstracted away by the Service Grid and Language Grid layers.

# Requirements

For this tutorial you will require:

1. Java 1.7 (or later)
1. Maven 3.0.x
1. an IDE such as IntelliJ or Eclipse
1. about < 2 hours

# Overview
We will go through a step-by-step tutorial to create a simple Lappsgrid service, `WhitespaceTokenizer` implementing all requirement for a language grid service. The tutorial includes writing java programs, packaging to Maven WAR package, and testing the package.
Each step covers a single topic, and will take 15 - 30 minutes 

## Step1: Maven project setup
In step #1, we will set up a Maven project (POM) with lappgrid API dependencies for Lappsgrid services. (Goto [step1](https://github.com/lapps/org.lappsgrid.examples/tree/step1))
## Step2: Implementing Lappsgrid service
In step #2, we will take a close look at implementation requirements for a Lappsgrid service - especially I/O specifications, and then write a simple toknization method satisfying partial requirements in the `WhitespaceTokenizer` service implementation. (Goto [step2](https://github.com/lapps/org.lappsgrid.examples/tree/step2))
## Step3: Service metadata
In step #3, we will see what should be implemented as metadata for our `WhitespaceTokenizer` service, and then go through two example ways to implement `getMetadata()`, another required method for a Lappsgrid service. (Goto [step3](https://github.com/lapps/org.lappsgrid.examples/tree/step3))
## Step4: Packaging a service
In step #4, we will see how to compile a service into a Maven WAR package with service manifests. We also taks a quick look at how to deploy compiled service onto a tomcat server. (Goto [step4](https://github.com/lapps/org.lappsgrid.examples/tree/step4))
## Step5: Testing a service
In step #5, we will test out `WhitespaceTokenizer` service, first as a java program, then as a web-service. (Goto [step5](https://github.com/lapps/org.lappsgrid.examples/tree/step5))
## Step6: Wrapping existing Java program
Coming soon.
## Step7: Wrapping Python program
Coming soon.

# Up Next
To start the tutorial from the first step, run the command:

```bash
> git checkout step1
```

Or follow the link: [step1](https://github.com/lapps/org.lappsgrid.examples/tree/step1)