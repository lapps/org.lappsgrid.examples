Wrapping Web Services : Step 5
=====================

### Contents

1. Maven project setup
1. Implementing LAPPSgrid service
1. Service metadata
1. Packaging a service
1. [Testing a service](#optional-testing)
    - [Unit Testing](#unit-testing)
    - [Service Integration Testing](#integration-testing)
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
1. to have have completed Step four
1. about 15 minutes

It is assumed that you know how to create a Maven project either using your IDE or via the command line. Maven usage is beyond the scope of this tutorial.


# (Optional) Testing

**Note that** this is an optional step; if a developer has his or her own test suite, then parts of this tutorial can be skipped.

Here, we will test out `WhitespaceTokenizer` service, first as a Java program, then as a web-service.
We will use the `JUnit` library, which is already included by the LAPPS Grid parent pom, for Java unit testing and Jetty/SoapUI for (simple) integration testing. 
The unit tests will verify that the `WhitespaceTokenizer` class does what it claims it does, while the integration tests ensure the program runs as a web service and doesn't return error messages.

## Unit Testing

Before start tutorial on unit testing, note that unit tests are specific to the service for which they're designed

Create a new test class  

```
src/test/Java/org/lappsgrid.example/TestWhitespaceTokenizer.Java
```

We start with the following skeleton.

```Java
    package org.lappsgrid.example;

    // JUnit modules for unit tests
    import org.junit.After;
    import org.junit.Before;
    import org.junit.Test;
    import static org.junit.Assert.*;

    // more APIs for testing code 
    import org.lappsgrid.api.WebService;
    import static org.lappsgrid.discriminator.Discriminators.Uri;
    import org.lappsgrid.metadata.IOSpecification;
    import org.lappsgrid.metadata.ServiceMetadata;
    import org.lappsgrid.serialization.Data;
    import org.lappsgrid.serialization.DataContainer;
    import org.lappsgrid.serialization.Serializer;
    import org.lappsgrid.serialization.lif.Annotation;
    import org.lappsgrid.serialization.lif.Container;
    import org.lappsgrid.serialization.lif.View;
    import org.lappsgrid.vocabulary.Features;
    import Java.io.IOException;
    import Java.util.List;
    import Java.util.Map;

    public class TestWhitespaceTokenizer {

        // this will be the sandbag
        protected WebService service;

        // initiate the service before each test
        @Before
        public void setUp() throws IOException {
            service = new WhitespaceTokenizer();
        }

        // then destroy it after the test
        @After
        public void tearDown() {
            service = null;
        }

        @Test
        public void testMetadata() { ... }
        
        @Test
        public void testExecute() { ... }
    }
```

Now, we're going to write two example test methods for `getMetadata()` and `execute()` method we wrote in step 2 and 3

### **`testMetadata()`**: Testing `getMetadata()` Method

In step 3, we gave the following metadata to our `WhitespaceTokenizer` service.

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

Now, we will see whether the program generates correct metadata by comparing strings from each field.

```Java

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
        assertTrue("LIF format not accepted.", list.contains(Uri.LAPPS));
        assertTrue("Text not accepted", list.contains(Uri.TEXT));
        
        assertEquals("Too many annotation types produced", 1, produces.getAnnotations().size());
        assertEquals("Tokens not produced", Uri.TOKEN, produces.getAnnotations().get(0));
    }
```

### **`testExecute()`**: Testing `execute()` Method

We will test `execute()` as well. Since our `WhitespaceTokenizer` produces *LIF* format, `execute()` will return a JSON serialization. We need to handle this string, just like we did in `testMetadata()`. See the following snippet.

```Java

    @Test
    public void testExecute() {

        final String text = "   abc def";

        // wrap plain text into `Data`
        Data input = new Data<>(Uri.TEXT, text);
        
        // call `execute()` with jsonized input,
        String tokenized = this.service.execute(input.asJson());

        // store the payload from what is returned into a `Container`, the main wrapper for LIF
        Container container = Serializer.parse(tokenized, DataContainer.class).getPayload();
        assertEquals("Text not set correctly", text, container.getText());

        // Now, see all annotations in current view is correct
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
        assertEquals("Token 1: wrong @type", Uri.TOKEN, tok1.getAtType());
        assertEquals("Token 1: wrong start", 3L, tok1.getStart().longValue());
        assertEquals("Token 1: wrong word", "abc", tok1.getFeature(Features.Token.WORD));

        Annotation tok2 = annotations.get(1);
        assertEquals("Token 2: wrong end", 10L, tok2.getEnd().longValue());
        assertEquals("Token 2: wrong word", "def", tok2.getFeature(Features.Token.WORD));
    }
```

The full example code can be found at [here](https://github.com/lapps/org.lappsgrid.examples/blob/step5/src/test/java/org.lappsgrid.example/TestWhitespaceTokenizer.java).

# Integration Testing

Here we will test `WhitespaceTokenizer` as a web-service, then its compatibility with SOAP protocol Lappsgrid adopted. First part is universally applicable to any Maven WAR web-service, while second part contains a test designed specifically for `WhitespaceTokenizer`.

The LAPPS Grid war-parent-pom includes the [Jetty Maven plugin](http://mvnrepository.com/artifact/org.eclipse.jetty)
plugin so services can be launched from the command line. To launch the service invoke
the `jetty:run` goal.

```bash
> mvn jetty:run
```

After a short time you should see the message
```bash
    [INFO] Started Jetty Server
```
To verify the server is running visit 
<a target="_blank" href="http://localhost:8080/lappsgrid_example/jsServices">http://localhost:8080/lappsgrid_example/jsServices</a>. Note that `lappsgrid_example` is the value of the `artifactID` property in your pom file. You should see the following in your browser: 

![JSON RPC](images/jsServices.png)

This simple user interface is provided automatically by the Apache Axis framework and 
we can use it to invoke methods on the service.  We will use it to invoke the `getMetatdata`
method since it does not require any input.

Click on the + sign beside the `getMetadata` line and then click on the [invoke] link to 
invoke the `getMetadata` method.  If you see the following page then the service is working 
correctly.  If not check your terminal window for a stack trace

![JSON RPC](images/jsServices2.png)

## SoapUI (Optional)

For the next test we will use SoapUI. SoapUI, as the name suggests, provides a user 
interface for interacting with SOAP web services. SoapUI is available for Windows, Mac, and Linux and 
can be downloaded from [Sourceforge](http://sourceforge.net/projects/soapui/files/). See the
*Getting Started* section at [www.soapui.org](http://www.soapui.org) if you need help using
SoapUI.

Start SoapUI and create a new SOAP project. Enter the following in the project setup dialog:

* **Project Name:** Whitespace Tokenizer
* **Initial WSDL:** http://localhost:8080/lappsgrid_example/services/WhitespaceTokenizer?wsdl

Click the `OK` button.  SoapUI will load the WSDL file from the service and after a
moment you should see the Whitespace Tokenizer project in the left hand pane.  Expand the
`execute` method and double click on `Request 1`. This will open the request editor in the right
hand pane.

Find the line
```xml
         <input xsi:type="xsd:string">?</input>
```

and replace the question mark with the following JSON:
```json
{
    "discriminator":"http://vocab.lappsgrid.org/ns/media/text",
    "payload": "Goodbye cruel world I am leaving you today."
}
```

The XML in the request editor should now look like:

![soap-ui](images/soapui.png)

To invoke the `execute` method you can:
* Click the green arrow in the top left of the request editor, or
* Press Alt-Enter (Windows) or Cmd-Enter (Mac).

You should see the response from the service appear in the right hand pane of the request
editor.

## Troubleshooting

**Q: My browser says the server could not be found**

**A:** This means the Jetty server did not start.  Check the terminal window for error messages.

**Q: Jetty can't start because the port is in use**

**A:** Another application is using port 8080 on your computer.  You can either quit the other 
application and try starting Jetty again, or you can tell Jetty to use a different port
number.
 
```bash
> mvn jetty:run -Djetty.port=9999
```

If you do change the port that Jetty uses remember to adjust the URLs used in the rest
of the tutorial.

**Q: I get a HTTP 404 Not Found error**

**A:** Double check the URL you are using. Did you change the port number? The URL will 
take the form
```
    # For the WSDL file
    http://localhost:<port>/<artifactId>/services/<className>?wsdl
    # For the JSON-RPC services
    http://localhost:<port>/<artifactId>/jsServices

```
Where

* **port** Is 8080 by default unless explicitly changed when starting Jetty.
* **artifactId** The <artifactId> value specified in the pom.xml file.
* **className** The name of the Java class (without the package name) providing the service.

# Up Next

We're adding in more tutorials. Stay tuned for coming tutorials on wrapping existing Java program, and wrapping non-Java code, such as Python.
