package org.lapps.datasource.trecgenomics;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.DataSource;
import org.lappsgrid.api.DataSourceException;
import org.lappsgrid.api.DataSourceIterator;
import org.lappsgrid.client.DataSourceClient;
import org.lappsgrid.client.ListDataSourceClient;
import org.lappsgrid.discriminator.Types;

public class HelloDatasourceTest {
  public static void main(String[] args) throws DataSourceException {
	  
    TrecPrimitiveDataSourceService trecService = new TrecPrimitiveDataSourceService();
    DataSource trecDataSource = trecService.getTrecPrimitiveDataSourcePort();
    
    ListDataSourceClient trecClient = new ListDataSourceClient(trecDataSource);
    
    Data subsetQuery = new Data(Types.QUERY, "SELECT 1 3 5");
    DataSourceClient trecSubset = trecClient.subDataSource(subsetQuery);
    
    DataSourceIterator iter = trecSubset.iterator(null);
    while(iter.hasNext()){
      Data response = iter.next();
      System.out.println("Client Side: " + response.getPayload());
    }
  }
}
