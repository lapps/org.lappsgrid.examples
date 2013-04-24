package org.lapps.annotatedcorpus;

/**
 * Queries over {@link AnnotatedCorpus} to select matching documents.
 * Possible queries include:
 * 1) IR query for lucene/indri e.g. "(president AND Obama)";
 * 2) regular expression matching query e.g. "(president .* Obama)" over text field and/or annotation field
 * 3) composite query built from several queries e.g. "(query1 OR !query2)"
 *
 * @author Di Wang
 */
public abstract class CorpusQuery {
  
}
