Wrapping Web Services : Step 2
=====================

### Contents

1. Maven project setup
1. [Implementing Lappsgrid service](#implementing-lappsgrid-service)
    - [Classes and Methods](#lappsgrid-interfaces-and-classes)
    - [Lappsgrid Exchange Datastructures](#lappsgrid-exchange-datastructures)
    - [The `execute()` Method](#putting-it-all-together-the-execute-method)
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

1. Java 1.7 (or later)
1. Maven 3.0.x
1. an IDE such as IntelliJ or Eclipse
1. to have have completed Step one
1. about 15 minutes

# Implementing Lappsgrid service

All services deployed to the Lappsgrid should extend one of the interfaces in the
[org.lappsgrid.api](http://lapps.github.io/org.lappsgrid.api/apidocs/) module.
Services that provide data to other services (aka data sources) 
should implement the [DataSource](http://lapps.github.io/org.lappsgrid.api/apidocs/org/lappsgrid/api/DataSource.html) interface 
and services that process data (aka ProcessingServices) 
should implement the [ProcessingService](http://lapps.github.io/org.lappsgrid.api/apidocs/org/lappsgrid/api/ProcessingService.html) interface.
Both interfaces are only *naming interfaces*, that is, they extend [WebService](http://lapps.github.io/org.lappsgrid.api/apidocs/org/lappsgrid/api/WebService.html) but do not add any new methods.
In this step, we start to write a simple Java class `WhitespaceTokenizer`, a tokenizer based on white spaces, that will be a Lappsgrid web service through steps of this tutorial.

## Lappsgrid Interfaces and Classes

Place a new empty class in `src/main/java/org/lappsgrid/example/WhitespaceTokenizer.java` implementing `ProcessingService`, because it will process input text, turn it into tokens.

The `WebService` (superclass of `ProcessingService`) interface contains two methods:

1. **`String getMetadata()`**<br/>
Returns metadata about the service
1. **`String execute(String)`**<br/>
Executes the service.

So the skeleton for a Lappsgrid web service looks like:

```java
package org.lappsgrid.example;

import org.lappsgrid.api.ProcessingService;

public class WhitespaceTokenizer implements ProcessingService
{
    public WhitespaceTokenizer() { }
    @Override
    public String getMetadata() { return null; }
    @Override
    public String execute(String input) { return null; }
}
```

With a tiny bit of XML boilerplate we could deploy the above as a perfectly functioning
service to the Lappsgrid; albeit not a particularly interesting service.

## Lappsgrid Exchange Datastructures (LEDS)

All of the Strings passed to and from Lappsgrid services are JSON strings containing
LEDS. See [org.lappsgrid.serialization.Data](http://lapps.github.io/org.lappsgrid.serialization/groovydoc/org/lappsgrid/serialization/Data.html), 
this `Data` class is a wrapper for LEDS compatible JSON format. 
Each `Data` object, that is LEDS, consists of a `discriminator`, which is a URI from the
[Lappsgrid URI Inventory](http://vocab.lappsgrid.org/discriminators.html), and a payload.
The `discriminator` is used to determine how the contents of the `payload` should be
interpreted.  
For example, the following `data` contains plain text.

```java
Data data = new Data<>();
data.setDiscriminator("http://vocab.lappsgrid.org/ns/media/text");
data.setPayload("Goodbye cruel world, I am leaving you today.");
```

To convert `data` into json string, one can use one of these methods.

```java
String json = data.asJson()
String prettyJson = data.asPrettyJson()
```

The [org.lappsgrid.discriminator.Discriminators.Uri](http://lapps.github.io/org.lappsgrid.discriminator/apidocs/org/lappsgrid/discriminator/Discriminators.Uri.html) 
class contains static definitions of the URI in the Lappsgrid inventory so users don't 
have to remember them all:

```java
data.setDiscriminator(Uri.TEXT);
```

Use of the `Discriminators.Uri` class also allows IDEs to provide command completion and
tool-top help.

### Lapps Interchange Format (LIF)

In previous section, we saw a LEDS that contains plain text as its payload. However, typically Lappsgrid services will exchange JSON as payload of their LEDS that conform to the [Lapps Interchange
Format (LIF)](http://vocab.lappsgrid.org/schema/lif-schema.json). The same [wrapper module for LEDS](http://github.com/lapps/org.lappsgrid.serialization)
also can be used to reliably generate conformant LIF JSON. These three `LEDS` classes are provided for LIF generation:

1. `Container`<br/>
The `Container` is the main wrapper for LIF objects.  A `Container` consists of some metadata and a list of Views.
1. `View`<br/>
Each service should contribute a **view**. A `View` consists of some metadata and a list of Annotations.
1. `Annotation`<br/>
A single annotation from the service.

```java
Container container = new Container()       // creates a new LIF
container.setText("Goodbye cruel world, I am leaving you today.");      // original input text
container.setLanguage("en");            // original input langage
View view = container.newView();        // new view that this service will contribute
Annotation a = view.newAnnotation("tok1", Uri.TOKEN, 0, 7);     // add annotations
Annotation a = view.newAnnotation("tok2", Uri.TOKEN, 8, 13); 
...
Data<Container> data = new Data<Container>(Uri.LAPPS, container);       // wrap LIF inside LEDS
System.out.println(data.asPrettyJson());
```

Creating `Data<Container>` objects is such a frequent task for Lappsgrid services
that the [DataContainer](http://lapps.github.io/org.lappsgrid.serialization/groovydoc/org/lappsgrid/serialization/DataContainer.html)
 class has been defined for just that purpose:

```
    Data data = new DataContainer(container);
    System.out.println(data.asPrettyJson());
```

### Serialization and de-serialization

The [org.lappsgrid.serialization.Serializer](http://lapps.github.io/org.lappsgrid.serialization/groovydoc/org/lappsgrid/serialization/Serializer.html)
class is a light-weight wrapper around the [Jackson](https://github.org/FasterXML/Jackson) 
library that can be used to serialize LIF objects to/from JSON.

```java
String json = Serializer.toPrettyJson(dataObject);
DataContainer data = Serializer.parse(json, DataContainer.class);
```

## Putting it all together - The `execute()` Method

Recall that all Strings passed to/from Lappgrid interface methods contain the JSON
representation of a `Data` object.  So the `execute()` method will need to perform the 
following steps:

1. Parse the incoming String into a `Data` object.
1. Check the `discriminator` value to determine the `payload` type.  Our WhitespaceTokenzier
will accept text and LIF containers as input.  The tokenizer should also check that
the `discriminator` is not `http://vocab.lappsgrid.org/ns/error`.
1. Extract the text from the `payload`.
    1. Create a new `Container` if the input was text
    1. Otherwise reuse the existing container.
1. Create a new `View`.
1. Tokenize the text and add annotations to the view.
1. Add information about the tokens to the view's metadata
1. Create a new `Data` object to wrap the container.
1. Serialize the `Data` and return the JSON string.

```java

import static org.lappsgrid.discriminator.Discriminators.Uri;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.DataContainer;
import org.lappsgrid.serialization.Serializer;
import org.lappsgrid.serialization.lif.Annotation;
import org.lappsgrid.serialization.lif.Container;
import org.lappsgrid.serialization.lif.View;
import org.lappsgrid.vocabulary.Features;

public class WhitespaceTokenizer implements ProcessingService {
    ...
    public String execute(String input) {
        // Step #1: Parse the input.
        Data data = Serializer.parse(input, Data.class);

        // Step #2: Check the discriminator
        final String discriminator = data.getDiscriminator();
        if (discriminator.equals(Uri.ERROR)) {
            // Return the input unchanged.
            return input;
        }

        // Step #3: Extract the text.
        Container container = null;
        if (discriminator.equals(Uri.TEXT)) {
            container = new Container();
            container.setText(data.getPayload().toString());
        }
        else if (discriminator.equals(Uri.LAPPS)) {
            container = new Container((Map) data.getPayload());
        }
        else {
            // This is a format we don't accept.
            String message = String.format("Unsupported discriminator type: %s", discriminator);
            return new Data<String>(Uri.ERROR, message).asJson();
        }

        // Step #4: Create a new View
        View view = container.newView();

        // Step #5: Tokenize the text and add annotations.
        String text = container.getText();
        String[] words = text.split("\\s+");
        int id = -1;
        int start = 0;
        for (String word : words) {
            start = text.indexOf(word, start);
            if (start < 0) {
                return new Data<String>(Uri.ERROR, "Unable to match word: " + word).asJson();
            }
            int end = start + word.length();
            Annotation a = view.newAnnotation("tok" + (++id), Uri.TOKEN, start, end);
            a.addFeature(Features.Token.WORD, word);
        }

        // Step #6: Update the view's metadata. Each view contains metadata about the
        // annotations it contains, in particular the name of the tool that produced the
        // annotations.
        view.addContains(Uri.TOKEN, this.getClass().getName(), "whitespace");
        
        // Step #7: Create a DataContainer with the result.
        data = new DataContainer(container);

        // Step #8: Serialize the data object and return the JSON.
        return data.asJson();
    }
    ...
}
```
This method is heavily relying on Lappsgrid APIs mentioned above. 
Complete example code can be found at [here](https://github.com/lapps/org.lappsgrid.examples/blob/step2/src/main/java/org/lappsgrid/example/WhitespaceTokenizer.java).

# Up Next

In Step three we will generate the JSON metadata returned by the `getMetadata()` method.

To advance to step three run the command:

```bash
> git checkout step3
```

Or follow the link: [step3](https://github.com/lapps/org.lappsgrid.examples/tree/step3)
