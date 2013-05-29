package org.lapps.datasource;

/**
 * Streams over a list of {@link Document}.
 *
 * @author Di Wang
 */
public interface DataStreamReader {
  
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
  public Data next();
}
