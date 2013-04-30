package org.anc.grid.data.masc.client;

import java.io.IOException;
import java.util.*;

import javax.xml.rpc.ServiceException;

import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.api.DataSource;

/**
 * A client class that wraps a DataSource service and provides
 * an iterator like API to retrieve documents from the service.
 * 
 * @author Keith Suderman
 *
 */
public class DocumentStreamReader
{
   // The DataSource we are providing state for.
   protected DataSource service;
   // The list of id values to be iterated over.
   protected List<String> list;
   // The thing that will do the actual iterating.
   protected Iterator<String> iterator;
   
   public DocumentStreamReader(DataSource service) throws IOException
   {
      this(service, null);
   }
   
   public DocumentStreamReader(DataSource service, String query) throws IOException 
   {
      this.service = service;
      list = new ArrayList<String>();
      
      // Get the list of items from the server. If the "query" is null
      // we will get everything, otherwise we pass the query on to the
      // DataSource.
      Data queryResult = null; 
      if (query == null)
      {
         queryResult = service.list();
      }
      else
      {
         queryResult = service.query(query);
      }
      
      String payload = queryResult.getPayload();
      
      // Check for errors from the server.
      if (queryResult.getDiscriminator() == Types.ERROR)
      {
         throw new IOException(payload);
      }
      
      // Parse the payload and prime the iterator.
      list = new ArrayList<String>();
      for (String s : payload.split(" "))
      {
         list.add(s);
      }
      iterator = list.iterator();
   }

   public boolean hasNext()
   {
      if (iterator == null)
      {
         return false;
      }
      return iterator.hasNext();
   }
   
   public String next() throws IOException
   {
      if (iterator == null)
      {
         return null;
      }
      // Fetch a document from the DataSource service.
      Data data = service.get(iterator.next());
      String payload = data.getPayload();
      if (data.getDiscriminator() == Types.ERROR){
         throw new IOException(payload);
      }
      return payload;
   }
}
