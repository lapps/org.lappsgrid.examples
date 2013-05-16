package org.lapps.datasource;

/**
 * Streams over a list of {@link Document}.
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
  public Document next();
}
