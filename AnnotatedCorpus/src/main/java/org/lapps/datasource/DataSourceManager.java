package org.lapps.datasource;

/**
 * Basic facility to manage {@link DataSource}. 
 *
 * @author Di Wang
 */
public interface DataSourceManager {

  /**
   * Connect to a remote corpus
   *
   * @param connOptions the options that specify how to establish a connection to remote corpus. 
   * @return the annotated corpus
   * @throws NoSuchDataSourceException the no such corpus exception
   */
  public DataSource connect(ConnectionOptions connOptions) throws NoSuchDataSourceException;

}
