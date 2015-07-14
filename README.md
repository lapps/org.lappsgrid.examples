Wrapping Web Services : Step 3
=====================

### Contents

1. Maven project setup
1. Implementing Lappsgrid service
1. [Service metadata](#getmetadata-method)
    - [What's in Metadata](#whats-in-the-Metadata)
    - [Generating with `metadata` module](#generating-metadata-with-metadata-module)
    - [Generating with `annotations` module](#generating-metadata-with-annotations-module)
    - [Returning metadata](#returning-metadata)
1. Testing a service
1. Packaging a service
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
1. to have have completed Step two
1. about 15 minutes

It is assumed that you know how to create a Maven project either using your IDE or via the command line.  Maven usage is beyond the scope of this tutorial.


# `getMetadata()` method

## What's in the Metadata

The metadata of a LAPPS grid service should be able to provide information about the service itself
as well as the service's I/O specification.

### Metadata for a service

Default JSON schema for a service consists of these fields:

* **$schema** (String)
    * The JSON schema that describes the JSON format
    * Defaults to "http://vocab.lappsgrid.org/schema/service-schema-1.0.0.json".
* **name** (String)
    * A human readable name for the service. 
* **vendor** (String)
    * Name or URI of the organization providing the service. 
* **version** (String)
    * The service version number in [major].[minor].[revision] format with an optional trailing qualifier. 
    * E.G. 1.1.0-SNAPSHOT 
* **description** (String)
    * A plain text description of the service or the URL to an online description. 
* **allow** (URI)
    * Permitted uses of this service. URI describes the allowable usages, e.g. commerial, research, etc. 
    * By default, it gets "allowed for any purpose".
* **license** (URI)
    * The license for this service.
* **url** (URI)
    * The full URL used to invoke the service.
* **parameters** (List)
    * Descriptions of the parameters required by the service.
* **requires** (JSON)
    * Input requirements of the service.  
* **produces** (JSON)
    * Output format specification.

### Metadata specifying input/output data

I/O specifications are nested JSON string which consist of:

* **encoding** (String)
    * The character encoding of input or output.
    * Defaults to UTF-8.
* **language** (List)
    * A list of [ISO language codes](http://www.loc.gov/standards/iso639-2/php/code_list.php) of acceptable/produced languages as input or output.
    * E.G. A Chinese-to-English translator service might require "zh" and produce "en"
* **format** (List)
    * A list of URI from the Lapps vocabulary specifying the format of I/O.
* **annotations** (List)
    * A list of URI from the Lapps vocabulary specifying the annotation types of I/O.

### Metadata JSON example

For example, a tokenizer service might __require__ a plain text with no annotation then __produce__ a sequence of tokens in LIF format.
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
  "vendor" : "http://www.lappsgrid.org",
  "allow" : "http://vocab.lappsgrid.org/ns/allow#any",
  "license" : "http://vocab.lappsgrid.org/ns/license#apache-2.0",
  "requires" : { ... },
  "produces" : { ... },
}
```

Service vendors can generate metadata JSON at compile or in runtime. 
We will go through two ways to generate metadata in this tutorial. However developers are always allowed to generate this string in their own ways.

## Generating metadata with `Metadata` module

One way to generate a JSON object is to use [`metadata`](http://lapps.github.io/org.lappsgrid.metadata/) module. To use it, first add it as a dependency in `pom.xml`

```xml
<dependency>
    <groupId>org.lappsgrid</groupId>
    <artifactId>metadata</artifactId>
    <version>1.0.4</version>
</dependency>
```

`metadata` module has these classes for generating JSON string:

* [`ServiceMetadata`](http://lapps.github.io/org.lappsgrid.metadata/index.html?org/lappsgrid/metadata/ServiceMetadata.html): provides JSON schema for a `ProcessingService` as well as getters and setters.
* [`DataSourceMetadata`](http://lapps.github.io/org.lappsgrid.metadata/index.html?org/lappsgrid/metadata/DataSourceMetadata.html): provides JSON schema for a `DataSource` serive as well as getters and setters.
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
        metadata.setVendor("http://www.lappsgrid.org");
        metadata.setLicense(Uri.APACHE2);

        // JSON for input information
        IOSpecification requires = new IOSpecification();
        requires.addFormat(Uri.TEXT);           // Plain text (form)
        requires.addLanguage("en");             // Source language

        // JSON for output information
        IOSpecification produces = new IOSpecification();
        produces.addFormat(Uri.LAPPS);          // LIF (form)
        produces.addAnnotation(Uri.TOKEN);      // Tokens (contents)
        requires.addLanguage("en");             // Target language
        
        // Embed I/O metadata JSON objects
        metadata.setRequires(requires);
        metadata.setProduces(produces);
        
    }
```

## Generating metadata with `annotations` module

If we can generate this metadata before the runtime and then simply cache a mere string ready to return, the service will run even faster.
That's why lappsgrid team provides java annotation for metadata.
By using java annotation, the compiler will generate JSON string into a file while compiling, which can be read in at runtime efficiently.

First thing to do is to add `annotation` module as a dependency for the project in `pom.xml`

```xml
<project ...>
...
    <dependencies>
        <dependency>
            <groupId>org.lappsgrid.experimental</groupId>
            <artifactId>annotations</artifactId>
            <version>${lapps.annotation.version}</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
...
</project>
```


And then import `MetadataProcess` as a compiler plugin for maven.

```xml
<project ...>
...
    <build>
        <finalName> YOUR_ARTIFACT_NAME </finalName>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessors>
                        <annotationProcessor>org.lappsgrid.annotation.processing.MetadataProcessor</annotationProcessor>
                    </annotationProcessors>
                </configuration>
            </plugin>
        </plugins>
    </build>
...
</project>
```

Finally, give an annotation to your service class using these keywords:

* `@ServiceMetadata`: provides annotation schema for a `ProcessingService`
* `@DataSourceMetadata`: provides annotation schema for a `DataSource`

Here is the list of all annotation keys supported by `annotation` module
    
* Service information
    * **schema**
    * **name**
    * **vendor**
    * **version**
    * **description**
    * **allow**
    * **license**
* I/O information
    * **requires_encoding**: input encoding
    * **produces_encoding**: output encoding
    * **encoding**: use if input and output uses the same encoding
    * **requires_language** (list): source language
    * **produces_language** (list): target language
    * **language**: use if input and output are in the same language
    * **requires_format** (list): acceptable formats
    * **produces_format** (list): produced foramts
    * **format**: use if input and output share the same format
    * **requires** (list): input annotations
    * **produces** (list): produced annotations
    
Note that `name` and `version` are optional. If `name` is not specified, the compiler gets its value automatically from the class name. When `version` is omitted, compiler first try to find a file named `VERSION` with version information, if the file isn't there, then, it gets the version from `pom.xml` Be careful when the service is not a maven project; `version` should be specified in java annotation or in `VERSION` file.
For keys that require discriminator(s), you can simply give their aliases from [Lappsgrid URI Inventory](http://vocab.lappsgrid.org/discriminators.html).
    
Here is an example:

```java
    import org.lappsgrid.annotations.ServiceMetadata;
    
    @ServiceMetadata(
        vendor = "http://www.lappsgrid.org",
        version = "1.0.0-SNAPSHOT",
        description = "Whitespace tokenizer",
        allow = "any",
        license = "apache2",
        language = { "en" },
        requires_format" = { "text" },
        produces_format" = { "lapps" },
        produces = { "token" }
    )
    public class WhitespaceTokenizer implements ProcessingService {
    
        public WhitespaceTokenizer() { ... }
    
        @Override
        public String getMetadata() { ... }
        
        @Override
        public String execute(String input) { ... }
    }
```
 
If you have multiple services inherit an abstract class or interface, you can give `@CommonMetadata` at the super class/interface for common factors.

Now, in `WhitespaceTokenizer` class, we need a method to read the JSON files generated by `MetadataProcess` plugin.
The files are stored as `/resources/metadata/CLASS_NAME.json`.

Here's an example:

```java
    import java.util.*
    
    private void loadMetadata(Class<?> serviceClass) throws IOException {
        // java will care the path to resources
        String resourceName = "metadata/" + serviceClass.getName() + ".json";
        InputStream inputStream = this.getClass().getResourceAsStream(resourceName);

        if (inputStream == null) {
            // throw exception
        }
        try {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            this.metadata = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            this.metadata = "error";
            // throw exception
        }
    }
```

## Returning metadata

Remember that all Strings passed to and from Lappsgrid services are `org.lappsgrid.serialization.Data` objects. 
So we need to wrap metadata with `Data` class before return it. For metadata, we use "http://vocab.lappsgrid.org/ns/meta"(org.lappsgrid.discriminator.Discriminators.Uri.META) as its discriminator.

```java
    public String getMetadata() {
        // Create Data instance and populate it
        Data<ServiceMetadata> data = new Data<>(Uri.META, this.metadata);
        return data.asJson();
    }
```

This is only an example. Typically services will generate this metadata at compile time or when the service is first launched. 
The metadata can then be efficiently cached for `getMetadata()` method. See `WhitespaceTokenizer.java` source file for the caching example. The example code generates metadata while the class is initialized.

# Up Next

In Step four of the tutorial, we will write some test methods to test out services

To advance to step four run the command:

```bash
> git checkout step4
```
