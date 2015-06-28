Wrapping Web Services : Step 4
=====================

# Sections To Be Written

 - [ ] Testing
     - [x] Unit testing methods
     - [ ] Service testing (SoapUI, mvn jetty:run)
 
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
1. About 15 minutes

It is assumed that you know how to create a Maven project either using your IDE or via the command line.  Maven usage is beyond the scope of this tutorial.


# Testing

## Testing methods

Regular Java unit test using `junit` should work for a LAPPS grid service. 
First of all, prepare a separate test class in `src/test/java/YOUR/PACKAGE/STRUCTURE/TestClass.java`
We start with following skeleton.

```java
    package org.lappsgrid.example;

    import org.junit.After;
    import org.junit.Before;
    import org.junit.Test;

    import static org.junit.Assert.*;

    public class TestWhitespaceTokenizer {

        @Test
        public void testMetadata() { ... }
        
        @Test
        public void testExecute() { ... }
    }
```

### Test `getMetadata()` method

In the previous step, we gave the following metadata to our `WhitespaceTokenizer` service.

Key|Value|Notes
---|---|---
$schema|"http://vocab.lappsgrid.org/schema/service-schema-1.0.0.json"|default
name|"WhitespaceTokenizer"| 
vendor|"http://www.anc.org"| 
version|"1.0.0-SNAPSHOT"| 
description|"Whitespace tokenizer"| 
vendor|"http://www.anc.org"| | 
allow|"http://vocab.lappsgrid.org/ns/allow#any"|"any"
license|"http://vocab.lappsgrid.org/ns/license#apache-2.0"|"apache2"
url| (not specified)
requires encoding|"UTF-8"|default
requires language|[ "en" ]| 
requires format" | [ "http://vocab.lappsgrid.org/ns/media/text" ]|"text"
produces encoding|"UTF-8"|default
produces language|[ "en" ]
produces format" | [ "http://vocab.lappsgrid.org/ns/media/jsonld#lif" ] | "lapps" 
produces annotation | [ "http://vocab.lappsgrid.org/Token" ]| "token" 

Now, we will see the program generates correct metadata.

```java

    @Test
    public void testMetadata() {

        WebService service = new WhitespaceTokenizer();
        
        // Retrieve metadata, remember `getMetadata()` returns a serialized JSON string
        String json = service.getMetadata();
        assertNotNull("service.getMetadata() returned null", json);
        
        // Instantiate `Data` object with returned JSON string
        Data data = Serializer.parse(json, Data.class);
        assertNotNull("Unable to parse metadata json.", data);
        assertNotSame(data.getPayload().toString(), Uri.ERROR, data.getDiscriminator());
        
        // Then, convert it into `Metadata` datastructure
        ServiceMetadata metadata = new ServiceMetadata((Map) data.getPayload());
        IOSpecification produces = metadata.getProduces();
        IOSpecification requires = metadata.getRequires();
        
        // Now, see each field has correct value
        assertEquals("Name is not correct", WhitespaceTokenizer.class.getName(), metadata.getName());
        
            ...
        
        List<String> list = requires.getFormat();
        assertEquals("Too many formats accepted", 1, list.size());
        assertTrue("Text not accepted", list.contains(Uri.TEXT));
        
        assertEquals("Too many annotation types produced", 1, produces.getAnnotations().size());
        assertEquals("Tokens not produced", Uri.TOKEN, produces.getAnnotations().get(0));
    }
```

### Test `execute()` method

We will test `execute()` as well. Since our `WhitespaceTokenizer` produces *LIF* format, `execute()` will return a JSON serialization. We need to handle this string, just like we did in `testMetadata()`. See the folowing test method.

```java
    
    import static org.lappsgrid.discriminator.Discriminators.Uri;
    import org.lappsgrid.metadata.*;
    import org.lappsgrid.serialization.*;
    import org.lappsgrid.serialization.lif.*;
    import org.lappsgrid.vocabulary.Features;

    ...

    @Test
    public void testExecute() {

        // set up test material
        WebService service = new WhitespaceTokenizer();
        final String text = "   abc def";

        // call `execute()`, store returned a JSON string into a `Container` datastructure, the main wrapper for LIF
        Container container = execute(text);
        assertEquals("Text not set correctly", text, container.getText());

        // Now, see all annotations in curretn view is correct
        List<View> views = container.getViews();
        if (views.size() != 1) {
            fail(String.format("Expected 1 view. Found: %d", views.size()));
        }
        View view = views.get(0);
        assertTrue("View does not contain tokens", view.contains(Uri.TOKEN));
        List<Annotation> annotations = view.getAnnotations();
        if (annotations.size() != 2) {
            fail(String.format("Expected 2 tokens. Found %d", annotations.size()));
        }
        Annotation tok1 = annotations.get(0);
        assertEquals("Token 1: wrong label", Uri.TOKEN, tok1.getLabel());
        assertEquals("Token 1: wrong start", 3L, tok1.getStart().longValue());
        assertEquals("Token 1: wrong word", "abc", tok1.getFeature(Features.Token.WORD));

        Annotation tok2 = annotations.get(1);
        assertEquals("Token 2: wrong end", 10L, tok2.getEnd().longValue());
        assertEquals("Token 2: wrong word", "def", tok2.getFeature(Features.Token.WORD));
    }
```

The full example code can be found at `src/test/java/org/lappsgrid/example/TestWhitespaceTokenizer.java`

## Testing the service as a web application

We will use [jetty Maven plugin](http://mvnrepository.com/artifact/org.eclipse.jetty) for web-app testing. 
As we stated, this tutorial is not focused on details about Maven. 
For more details about the plugin, please refer to [the documentation from Eclipse](http://www.eclipse.org/jetty/documentation/current/jetty-maven-plugin.html#running-assembled-webapp-as-war)

First, we need to set up the plugin at `pom.xml` so that Maven automatically run jetty server for testing.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion/>
    <groupId/>
    <artifactId/>
    <version/>
    <packaging/>
    <name/>
    <description/>
    <parent> ... </parent>
    <dependencies> ... </dependencies>
    <repositories> ... </repositories>
    
    <!-- add jetty plugin -->
    <build>:
        <finalName> YOUR_ARTIFACT_NAME </finalName>
        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

Then run Maven to compile the project then start jetty server for rapid testing.

```bash
maven jetty:run
```

**TODO** now, how to test the service with SOAP-UI

# Up Next

In Step four of the tutorial, we pack up our service into a WAR package using Maven, make it ready to be deployed.

To advance to step two run the command:

```bash
> git checkout Step5-Packaging
```