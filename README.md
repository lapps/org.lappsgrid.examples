Wrapping Web Services : Step 3
=====================

# Sections To Be Written

 - [ ] Implementing required methods
     - [x] `execute` method (in prev section)
     - [ ] `getMetadata`
         - [x] What's in metadata?
         - [x] Generating metadata with ServiceMetadata class
         - [ ] Generating metadata with annotations
         - [ ] Metadata string as external resource
         - [x] Returning metadata
  

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


# `getMetadata` method

## What's in Metadata

The metadata of a LAPPS grid service should be able to provide information about the service itself
as well as the service's I/O specification.

### Metadata for service

Default JSON schema for a service consists of these fields:

* $schema(String)
    * The JSON schema that describes the JSON format
    * Defaults to "http://vocab.lappsgrid.org/schema/service-schema-1.0.0.json".
* name(String)
    * A human readable name for the service. 
* vendor(String)
    * Name or URI of the organization providing the service. 
* version(String)
    * The service version number in [major].[minor].[revision] format with an optional trailing qualifier. 
    * E.G. 1.1.0-SNAPSHOT 
* description(String)
    * A plain text description of the service or the URL to an online description. 
* allow
    * Permitted uses of this service. URI describes the allowable usages, e.g. commerial, research, etc. 
    * By default, it gets "allowed for any purpose".
* license(String)
    * The license for this service.
* url(String)
    * The full URL used to invoke the service.
* parameters(List)
    * Descriptions of the parameters required by the service.
* requires(JSON)
    * Input requirements of the service.  
* produces(JSON)
    * Output format specification.

### Metadata specifying input/output data

I/O specifications are nested JSON string which consist of:

* encoding(String)
    * The character encoding of input or output.
    * Defaults to UTF-8.
* language(List)
    * A list of ISO language codes of acceptable/produced languages of I/O.
    * E.G. A Chinese-to-English translator service might requires "zh" and produces "en"
* format(List)
    * A list of URI from the Lapps vocabulary specifying the format of I/O.
* annotations(List)
    * A list of URI from the Lapps vocabulary specifying the annotation types of I/O.

### Metadata JSON exmaple

For example, a tokenizer service might _require_ a plain text with no annotation then _produce_ a sequence of tokens in LIF format.
Thus, 

```json
  "requires" : {
    "language" : [ "en" ],
    "format" : [ "http://vocab.lappsgrid.org/ns/media/text" ]
  },
  "produces" : {
    "language" : [ "en" ],
    "format" : [ "http://vocab.lappsgrid.org/ns/media/jsonld#lif" ],
    "annotations" : [ "http://vocab.lappsgrid.org/Token" ]
  }
```

Then, the rest of metadata for this whitespace tokenizer would look like:

```json
{
  "$schema" : "http://vocab.lappsgrid.org/schema/service-schema-1.0.0.json",
  "name" : "org.lappsgrid.example.WhitespaceTokenizer",
  "version" : "1.0.0-SNAPSHOT",
  "description" : "Whitespace Tokenizer",
  "vendor" : "http://www.anc.org",
  "allow" : "http://vocab.lappsgrid.org/ns/allow#any",
  "license" : "http://vocab.lappsgrid.org/ns/license#apache-2.0",
  "requires" : { ... },
  "produces" : { ... },
}
```

Service vendors can generate metadata JSON at compile or in runtime. 
We will go through two ways to generate metadata in this tutorial. However developers are always allowed to generate this string in their own ways.

## Generating metadata with ServiceMetadata class

One way to generate a JSON object is to use [`metadata`](http://lapps.github.io/org.lappsgrid.metadata/) module in lappsgrid.
`metadata` module has these classes for generating JSON string:

* [`DataSourceMetadata`](http://lapps.github.io/org.lappsgrid.metadata/index.html?org/lappsgrid/metadata/DataSourceMetadata.html): provides JSON schema for a `DataSource` serive as well as getters and setters.
* [`ServiceMetadata`](http://lapps.github.io/org.lappsgrid.metadata/index.html?org/lappsgrid/metadata/ServiceMetadata.html): provides JSON schema for a `ProcessingService` as well as getters and setters.
* [`IOSpecification`](http://lapps.github.io/org.lappsgrid.metadata/index.html?org/lappsgrid/metadata/IOSpecificatoin.html): provides JSON schema for a specification for input and output.

For WhitespaceTokenizer, we will use ServiceMetadata and IOSpecification classes. Using these classes should be very straightforward. See the below snippet. 

```java
    import static org.lappsgrid.discriminator.Discriminators.Uri;
    import org.lappsgrid.metadata.ServiceMetadata;
    import org.lappsgrid.metadata.IOSpecification;
    import org.lappsgrid.serialization.Data;
    
    public class WhitespaceTokenizer implements ProcessingService {

    
        // Create a metadata object
        ServiceMetadata this.metadata = new ServiceMetadata();
        
        // Populate metadata using setX() methods
        metadata.setName(this.getClass().getName());
        metadata.setDescription("Whitespace tokenizer");
        metadata.setVersion("1.0.0-SNAPSHOT");
        metadata.setVendor("http://www.anc.org");
        metadata.setLicense(Uri.APACHE2);

        // JSON for input information
        IOSpecification requires = new IOSpecification();
        requires.addFormat(Uri.TEXT);
        requires.addFormat(Uri.LAPPS);
        requires.addFormat(Uri.LIF);
        requires.setEncoding("UTF-8");

        // JSON for output information
        IOSpecification produces = new IOSpecification();
        produces.addFormat(Uri.LAPPS);
        produces.setEncoding("UTF-8");
        produces.addAnnotation(Uri.TOKEN);
        
        // Embed I/O metadata JSON objects
        metadata.setRequires(requires);
        metadata.setProduces(produces);
        
    }
```
## Generating metadata with annotations

TODO


## Metadata string as external resource

TODO


## Returning metadata

Remember that all Strings passed to and from Lappsgrid services are `org.lappsgrid.serialization.Data` objects. 
So we need to wrap metadata with `Data` class before return it. For metadata, we use "http://vocab.lappsgrid.org/ns/meta"(org.lappsgrid.discriminator.Discriminators.Uri.META) as its discriminator.

```java
    public String getMetada() {
        // Create Data instance and populate it
        Data<ServiceMetadata> data = new Data<>(Uri.META, this.metadata);
        return data.asJson();
    }
```

This is only an example. Typically services will generate this metadata at compile time or when the service is first launched. The metadata can then be efficiently cached for getMetadata() method.

# Up Next

In Step four of the tutorial, we will write some test methods to test out services

To advance to step two run the command:

```bash
> git checkout Step4-Testing
```
