Wrapping Web Services : Step 2
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
1. to have have completed Step one
1. about 15 minutes

It is assumed that you know how to create a Maven project either using your IDE or via
the command line.  Maven usage is beyond the scope of this tutorial.

# WhitespaceTokenizer Class

All services deployed to the Lappsgrid should extend one of the interfaces in the
[org.lappsgrid.api](https://lapps.github.io/org.lappsgrid.api) module.  Services that provide
data to other services (aka DataSources) should implement the [DataSource](http://lapps.github.io/org.lappsgrid.api/index.html?org/lappsgrid/api/DataSource.html) 
interface and services that process data (aka ProcessingServices) should implement the
[ProcessingService](http://lapps.github.io/org.lappsgrid.api/index.html?org/lappsgrid/api/ProcessingService.html)
interface.  Both interfaces are *naming interfaces*, that is, they extend [WebService](http://lapps.github.io/org.lappsgrid.api/index.html?org/lappsgrid/api/WebService.html)
but do not add any new methods.

## Lappsgrid Interfaces and Classes

The `WebService` interface contains two methods:

1. **String getMetadata()**<br/>
Returns metadata about the service
1. **String execute(String)**<br/>
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
service to the Lappsgrid; albeit not a particularly interesting service...


## Lappsgrid Exchange Datastructures

All of the Strings passed to and from Lappsgrid services are JSON strings containing
[org.lappsgrid.serialization.Data](http://lapps.github.io/org.lappsgrid.serialization/index.html?org/lappsgrid/serialization/Data.html)
objects.  Each `Data` object consists of a `discriminator`, which is a URI from the
[Lappsgrid URI Inventory](http://vocab.lappsgrid.org/discriminators.html), and a payload.
The `discriminator` is used to determine how the contents of the `payload` should be
interpreted.  

```java
Data<String> data = new Data<String>();
data.setDiscriminator("http://vocab.lappsgrid.org/ns/media/text");
data.setPayload("Goodbye cruel world, I am leaving you today.");
```

The
[org.lappsgrid.discriminator.Discriminators.Uri](http://lapps.github.io/org.lappsgrid.discriminator/index.html?org/lappsgrid/discriminator/Discriminators.html) 
class contains static definitions of the URI in the Lappsgrid inventory so users don't 
have to remember them all:

```java
data.setDiscriminator(Uri.TEXT);
```

Use of the `Discriminators.Uri` class also allows IDEs to provide command completion and
tool-top help.

### Lappsgrid Interchange Format

Typically Lappsgrid services will exchange JSON objects that conform to the [Lappsgrid Interchange
Format (LIF)](http://vocab.lappsgrid.org/schema/lif-schema.json). The [Lappsgrid Exchange Datastructures (LEDS)](http://github.com/lapps/org.lappsgrid.serialization)
can be used to reliably generate conformant JSON.  The three main `LEDS` classes are:

1. `Container`<br/>
The `Container` is the main wrapper for LIF objects.  A `Container` consists of some 
metadata and a list of Views.
1. `View`<br/>
A `View` consists of some metadata and a list of Annotations.
1. `Annotation`<br/>
A single annotation.


```java
Container container = new Container()
container.setText("Goodbye cruel world, I am leaving you today.");
container.setLanguage("en");
View view = container.newView();
Annotation a = view.newAnnotation("tok1", Uri.TOKEN, 0, 7);
...
Data<Container> data = new Data<Container>(Uri.LAPPS, container);
System.out.println(data.asPrettyJson());
```

Creating `Data<Container>` objects is such a frequent task for Lappsgrid services
that the [DataContainer](http://lapps.github.io/org.lappsgrid.serialization/index.html?org/lappsgrid/serialization/DataContainer.html)
 class has been defined for just that purpose:

```
	Data data = new DataContainer(container);
	System.out.println(data.asPrettyJson());
```
### Serialization

The [org.lappsgrid.serialization.Serializer](http://lapps.github.io/org.lappsgrid.serialization/index.html?org/lappsgrid/serialization/Serializer.html)
class is a light-weight wrapper around the [Jackson](https://github.org/FasterXML/Jackson) 
library that can be used to serialize LIF objects to/from JSON.

```java
String json = Serializer.toPrettyJson(dataObject);
DataContainer data = Serializer.parse(json, DataContainer.class);
```

# Putting it all together

## The execute() Method

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
1. Create a new `DataContainer` object to wrap the container.
1. Serialize the `DataContainer` and return the JSON string.

```java
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
```

# Up Next

In Step three we will generate the JSON metadata returned by the `getMetadata()` method.

To advance to step three run the command:

```bash
> git checkout Step3-Metadata
```