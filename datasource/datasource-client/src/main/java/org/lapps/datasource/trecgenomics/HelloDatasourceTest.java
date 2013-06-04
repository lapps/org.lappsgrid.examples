package org.lapps.datasource.trecgenomics;

import org.lapps.datasource.Data;
import org.lapps.datasource.DataQuery;
import org.lapps.datasource.DataSource;
import org.lapps.datasource.DataStreamReader;

public class HelloDatasourceTest {
  public static void main(String[] args) {
    TrecDataSourceClient trecClient = new TrecDataSourceClient();
   
    DataQuery subsetQuery = new DataQuery("SELECT 1 3 5");
    DataSource trecSubset = trecClient.subCorpus(subsetQuery);
    
    DataStreamReader reader = trecSubset.getDataStreamReader(null);
    while(reader.hasNext()){
      Data response = reader.next();
      System.out.println("Client Side: " + response.getPayload());
    }
  }
}
