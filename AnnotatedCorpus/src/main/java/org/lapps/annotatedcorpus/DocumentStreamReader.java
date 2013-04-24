package org.lapps.annotatedcorpus;

/**
 * Streams over a list of {@link AnnotatedDocument}.
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
