package org.lapps.annotatedcorpus;

/**
 * The Interface DocumentStreamReader streams over a list of documents.
 *
 * @author Di Wang
 */
public interface DocumentStreamReader {
  
  /**
   * Checks for next.
   *
   * @return true, if successful
   */
  public boolean hasNext();
  
  /**
   * Next document.
   *
   * @return the annotated document
   */
  public AnnotatedDocument next();
}
