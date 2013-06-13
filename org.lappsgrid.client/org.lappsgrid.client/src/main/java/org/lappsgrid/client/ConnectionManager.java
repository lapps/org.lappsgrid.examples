package org.lappsgrid.client;

import org.lappsgrid.api.DataSource;
import org.lappsgrid.api.NoSuchDataSourceException;
import org.lappsgrid.core.ConnectionOptions;

/**
 * Basic facility to manage {@link DataSource}. 
 *
 * @author Di Wang
 */
public interface ConnectionManager {

  /**
   * Connect to a remote corpus
   *
   * @param connOptions the options that specify how to establish a connection to remote corpus. 
   * @return the annotated corpus
   * @throws NoSuchDataSourceException the no such corpus exception
   */
  public DataSource connect(ConnectionOptions connOptions) throws NoSuchDataSourceException;

}
