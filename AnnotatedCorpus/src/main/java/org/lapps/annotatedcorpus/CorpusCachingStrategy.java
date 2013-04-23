package org.lapps.annotatedcorpus;

/**
 * The Interface CorpusCachingStrategy defines how to cache corpus based on {@link CorpusQuery} and {@link ViewOptions}.
 *  
 * @author Di Wang
 */
public interface CorpusCachingStrategy {

  /**
   * Adds a corpus to the cache.
   *
   * @param query the query
   * @param option the option
   * @param corpus the corpus
   */
  public void addToCache(CorpusQuery query, ViewOptions option, AnnotatedCorpus corpus);

  /**
   * Gets a corpus the from cache.
   *
   * @param query the query
   * @param option the option
   * @return the from cache
   */
  public AnnotatedCorpus getFromCache(CorpusQuery query, ViewOptions option);

}
