Wrapping Web Services : Step 5
=====================

# Sections To Be Written

 - [x] Generating the .war file
     - [x] Update web.xml
     - [x] Adding configuration file to serviceimpl directory
     - [x] Package into war file using maven

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

It is assumed that you know how to create a Maven project either using your IDE or via the command line.  Maven usage is beyond the scope of this tutorial.

# Pack things up

A service needs to be packaged into a .war file before deployed to Lappsgrid. 
To pack things up, a developer first needs to config manifest files.
We provide here [webapp.zip](https://github.com/lapps/org.lappsgrid.example.java.whitespacetokenizer/blob/Step5-Packaing/webapp.zip)
 file contains templates of necessary manifest files.

## Templates

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

Only files that need changes are 

1. `web.xml` in `webapp/WEB-INF/`
1. `YOUR_CLASS_NAME.xml` in `webapp/WEB-INF/serviceimpl/`

## Updating web.xml

At the top of web.xml file, you will find `display-name` tag. Put your service name at here.

```xml
<display-name>===YOUR SERVICE NAME HERE===</display-name>
```

With the whitespace tokenizer example, `web.xml` will look like

```xml
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
    <display-name>Lapps Grid Whitespace Tokenizer (Java) Example</display-name>
    <context-param>
        <param-name>servicesPath</param-name>
        <param-value>WEB-INF/serviceimpl</param-value>
    </context-param>
    ...
```

## Adding services configuration to serviceimpl

In `webapp/WEB-INF/serviceimpl` directory, There is `YOUR_CLASS_NAME.xml`. 
Here you need to create corresponding xml files for each class that has `execute()` and `getMetadata()` methods.
Since we only have one such class, `WhitespaceTokonizer`, all we need is `WhitespaceTokenizer.xml`. Rename it.
Then we also need to edit the xml file, change `class` attribute of inner `bean` tag

```xml
<bean class="===YOUR CLASS NAME HERE===">
```

Final `WhitespaceTokenizer.xml` would be this:

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

Now we set up all necessary configuration to pack things up. 
One last step before we pack is to put those configurations in the right place. 
Move `webapp` directory in source `src/main/`.
Final command is to use maven to compile the package into a war file:

```bash
mvn clean package
```

If you want to run all tests before compilation, use `test` flag

```bash
mvn clean test package
```

The maven-generated .war file will be located in `target` directory in project root.