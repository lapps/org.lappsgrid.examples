Wrapping Web Services : Step 2
=====================

# Sections To Be Written

 - [ ] Lappsgrid interfaces
 - [ ] LEDS
   - [ ] Creating Data
   - [ ] Returning errors
   - [ ] Creating LIF objects
 - [ ] Simple execute method.
 - [ ] getMetadata() returns an error object
   

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


## Lappsgrid Interchange Format

All of the Strings passed to and from Lappsgrid services are JSON strings containing
[org.lappsgrid.serialization.Data](http://lapps.github.io/org.lappsgrid.serialization/index.html?org/lappsgrid/serialization/Data.html)
objects.  Each `Data` object consists of a `discriminator`, which is a URI from the
[Lappsgrid URI Inventory](http://vocab.lappsgrid.org/discriminators.html) and a payload.
The `discriminator` is used to determine the contents of the `payload`.  The
[org.lappsgrid.discriminator.Discriminators.Uri](http://lapps.github.io/org.lappsgrid.discriminator/index.html?org/lappsgrid/discriminator/Discriminators.html) 
class contains constant definitions of the URI in the Lappsgrid inventory so users don't 
have to remember the URIs:

```java
import static org.lappsgrid.discriminator.Discriminators.Uri;
...
System.out.println(Uri.TOKEN);
System.out.println(Uri.ERROR);
```

Typically Lappsgrid service will exchange JSON objects that conform to the Lappsgrid Interchange
Format (LIF). JSON-Schema for LIF are available [here](http://vocab.lappsgrid.org/schema/lif-schema.json).
Additionally, the [Lappsgrid Exchange Datastructures](http://github.com/lapps/org.lappsgrid.serialization)
can be used to reliably generate conformant JSON.

```java
import static org.lappsgrid.discriminator.Discriminators.Uri;
...
Data<String> data = new Data<>(Uri.TEXT, "Hello world");
System.out.println(data.asPrettyJson());
```

The [org.lappsgrid.core.DataFactory](http://lapps.github.io/org.lappsgrid.core/index.html?org/lappsgrid/core/DataFactory.html)
class can be used to generate the JSON string for the most commonly used Data object types:

```java
data = DataFactory.error("Something bad happened");
data = DataFactory.text("Hello world");
```

Creating `Data<Container>` objects is such a frequent task for Lappsgrid services
that the [DataContainer](http://lapps.github.io/org.lappsgrid.serialization/index.html?org/lappsgrid/serialization/DataContainer.html)
 class has been defined for just that purpose:

```
	Data data = new DataContainer(container);
	System.out.println(data.asPrettyJson());
```

## Still To Do

1. Create Container 
1. Add a view
1. Add annotations

# Up Next

In Step three we will generate the JSON metadata returned by the `getMetadata()` method.

To advance to step three run the command:

```bash
> git checkout Step3-Metadata
```