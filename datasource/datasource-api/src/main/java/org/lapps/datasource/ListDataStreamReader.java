package org.lapps.datasource;

import java.util.Iterator;


public class ListDataStreamReader implements DataStreamReader {
  
  ListDataSource dataSource;
  ViewOptions options;
  private Iterator<String> iterator;
  
  public ListDataStreamReader(ListDataSource dataSource, ViewOptions options) {
    super();
    this.dataSource = dataSource;
    iterator = dataSource.getDocIdList().iterator();
    
    this.options = options;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public Data next() {
    return dataSource.getData(options, iterator.next());
  }

}
