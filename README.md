Wrapping Web Services : Step 4
=====================

### Contents

1. Maven project setup
1. Implementing Lappsgrid service
1. Service metadata
1. [Packaging a service](#pack-things-up)
    - [Configure WEB-INF](cConfigure-web-inf)
    - [Maven packing](#maven-packing)
    - [Deploying on tomcat server](#deploying-on-tomcat-server)
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

1. Java 1.7 (or later)
1. Maven 3.0.x
1. an IDE such as IntelliJ or Eclipse
1. to have have completed Step three
1. about 10 minutes

It is assumed that you know how to create a Maven project either using your IDE or via the command line.  Maven usage is beyond the scope of this tutorial.

# Pack things up

A service needs to be pakced into a .war file before being deployed to the Lappsgrid. 
To pack things up, a developer first needs to configure manifest files for maven compilation as a web application.
We provide an [archive](https://github.com/lapps/org.lappsgrid.examples/blob/step4/webapp.zip) that contains templates of necessary manifest files.

## Configure WEB-INF

In the compressed file, one can find these files

```
webapp
└───WEB-INF
    │   server-config.wsdd
    │   web.xml
    │
    └───serviceimpl
            YOUR_CLASS_NAME.xml
```

Files we need to edit are:

1. `web.xml` in `webapp/WEB-INF/`
1. `YOUR_CLASS_NAME.xml` in `webapp/WEB-INF/serviceimpl/`

### Updating web.xml

At the top of web.xml file, you will find `display-name` tag. Put your service name at here.

```xml
<display-name>===YOUR SERVICE NAME HERE===</display-name>
```

With the whitespace tokenizer example, `web.xml` will look like:

```xml
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
    <display-name>Lapps Grid Whitespace Tokenizer (Java) Example</display-name>
    <context-param>
        <param-name>servicesPath</param-name>
        <param-value>WEB-INF/serviceimpl</param-value>
    </context-param>
    ...
```

### Adding services configuration to serviceimpl

In `webapp/WEB-INF/serviceimpl` directory you need to create corresponding xml files for each service class that has `execute()` and `getMetadata()` methods. In this exmaple, we only have one such class, `WhitespaceTokenizer`, so all we need is a file `WhitespaceTokenizer.xml`. Make a copy of the template file `YOUR_CLASS_NAME.xml` and name it `WhitespaceTokenizer.xml`.
Then we need to edit the new xml file: change the `class` attribute of the inner `bean` tag, which now is

```xml
<bean class="===YOUR CLASS NAME HERE===">
```

After editing, `WhitespaceTokenizer.xml` should look like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
  <bean id="target" class="jp.go.nict.langrid.servicecontainer.handler.TargetServiceFactory" >
    <property name="service">
      <!-- CHANGE HERE -->
      <bean class="org.lappsgrid.example.WhitespaceTokenizer" />
    </property>
  </bean>
</beans>
```

## Maven packing

We have now almost set up all necessary configurations for packing things up. The last step is to put those configurations in the right place by making sure that the `WEB-inf` directory from the archive in in the directory `src/main/webapp`.

Finally, it's time to compile. Use maven to compile the package into a war file.

```bash
mvn clean package
```

This will run all your Junit test code, if any, before compilation. Writing tests for a Lapps service is covered in the next step.

The maven-generated .war file will be located in the `target` directory in the project root.


## Deploying on tomcat server

Simply put the artifact war file in the `webapps` directory under a tomcat server set up for the service. The Tomcat web-app manager will automatically recognize a new app on next re-start.

# Up Next

In Step five of the tutorial, we will write some test methods to test out services

To advance to step five, run the command:

```bash
> git checkout step5
```
Or follow the link: [step5](https://github.com/lapps/org.lappsgrid.examples/tree/step5)
