package org.lappsgrid.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.DataSource;
import org.lappsgrid.api.DataSourceException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

public class ListDataSource implements DataSource, Iterable<Data>
{
   /** The list of document ID values this DataSource will iterate over. */
   protected List<String> docIdList;
   
   /** The DataSource to retrieve Data from. */
   protected DataSource dataSource;
   
   public ListDataSource()
   {
      
   }
   
   public ListDataSource(DataSource dataSource) throws DataSourceException
   {
      this.dataSource = dataSource;
      this.docIdList = new ArrayList<String>();
      Data result = dataSource.query(DataFactory.list());
      if (result.getDiscriminator() == Types.ERROR)
      {
         String message = result.getPayload();
         if (message == null || message.trim().equals(""))
         {
            message = "Unable to get key list from data source.";
         }
         throw new DataSourceException(message);
      }
      String payload = result.getPayload();
      if (payload == null || payload.trim().length() == 0)
      {
         throw new DataSourceException("Data source returned empty key list.");
      }
      String[] keys = payload.split("\\s+");
      for (String key: keys)
      {
         docIdList.add(key);
      }
   }
   
   public ListDataSource(DataSource dataSource, List<String> docIdList) {
      this.dataSource = dataSource;
      
      // Make a defensive copy of any collections passed to us.      
      this.docIdList = new ArrayList<String>(docIdList);
   }
   
   public ListDataSource(DataSource dataSource, String[] docIdArray)
   {
      this.dataSource = dataSource;
      this.docIdList = new ArrayList<String>(Arrays.asList(docIdArray));      
   }
   
   public Data query(Data query)
   {
      return dataSource.query(query);
   }
   
   public Data get(int index) {
      if (index >= docIdList.size()) {
         return DataFactory.error("Index out of bounds");
      }
      
      String key = docIdList.get(index);
      return dataSource.query(DataFactory.get(key));
   }
   
   public Iterator<Data> iterator() {
      return new ListDataSourceIterator(dataSource, docIdList);
   }
   
   protected void setDocIdList(List<String> list)
   {
      this.docIdList = new ArrayList<String>(list);
   }
   
   protected void setDataSource(DataSource dataSource)
   {
      this.dataSource = dataSource;
   }
}
