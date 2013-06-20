package org.anc.grid.data.masc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.anc.io.UTF8Reader;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.DataSource;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;
//import org.lappsgrid.discriminator.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A DataSource that returns documents from the MASC.
 * <p>
 * Each MASC document is identified by the @docId attribute in the
 * document header file.
 * @author Keith Suderman
 *
 */
public class MascDataSource implements DataSource
{
   private List<String> keys;
   private static MascIndex index;

   private static Logger logger = LoggerFactory.getLogger(MascDataSource.class);
   
   static {
      index = new MascIndex();
      index.load();
   }
   
   public MascDataSource()
   {
      logger.info("Creating a MASC data source.");
      keys = index.keys();
   }

   public MascDataSource(Set<String> keys)
   {
      logger.info("Creating a filtered MASC data source.");
      this.keys = new ArrayList<String>(keys);
   }
   
   //@Override
   protected Data list()
   {        
      logger.info("Listing.");
      return new Data(Types.INDEX, collect(keys));
   }

//   @Override
   protected Data get(String key)
   {
      
      logger.info("Getting document for {}", key);
      File file = index.get(key);
      if (file == null)
      {
         logger.error("No such file.");
         return DataFactory.error("No such file.");
      }
      if (!file.exists())
      {
         logger.error("File not found.");
         return DataFactory.error("File not found.");
      }
      
      UTF8Reader reader = null;
      String payload = null;
      long type = decode(key);
      try
      {
         logger.debug("Loading {}", file.getPath());
         reader = new UTF8Reader(file);
         payload = reader.readString();
         reader.close();
      }
      catch (IOException e)
      {
         logger.error("Unable to load file.", e);
         type = Types.ERROR;
         payload = e.getMessage();
      }      
      logger.debug("Returning the Data object.");
      return new Data(type, payload);
   }

   @Override
   public Data query(Data query)
   {
      Data result;
      long type = query.getDiscriminator();
      if (type == Types.QUERY) 
      {
         result = doQuery(query.getPayload());
      }
      else if (type == Types.LIST)
      {
         result = list();
      }
      else if (type == Types.GET)
      {
         result = get(query.getPayload());
      }
      else
      {
         result = DataFactory.error("Unknown query type");
      }
      return result;
      
   }

   protected Data doQuery(String queryString)
   {
      List<String> list = new ArrayList<String>();
      for (String key : keys)
      {
         if (key.contains(queryString))
         {
            list.add(key);
         }
      }
      return DataFactory.index(collect(list));
   }

   protected long decode(String key)
   {
      if (key.endsWith("txt"))
      {
         return Types.TEXT;
      }
      else if (key.endsWith("hdr"))
      {
         return Types.XML;
      }
      return Types.GRAF;
   }
   
   /**
    * Takes a list of String objects and concatenates them into
    * a single String. Items in the list are separated by a single
    * space character.
    * 
    */
   private String collect(Collection<String> list)
   {
      StringBuilder buffer = new StringBuilder();
      Iterator<String> it = list.iterator();
      if (it.hasNext())
      {
         buffer.append(it.next());
      }
      while (it.hasNext())
      {
         buffer.append(' ');
         buffer.append(it.next());
      }
      return buffer.toString();
   }

}
