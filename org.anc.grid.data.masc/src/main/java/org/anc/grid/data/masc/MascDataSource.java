package org.anc.grid.data.masc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.anc.io.UTF8Reader;
import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.api.DataSource;
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
   private Set<String> keys;
   private static MascIndex index;

   private static Logger logger = LoggerFactory.getLogger(MascDataSource.class);
   
   static {
      index = new MascIndex();
      index.load();
   }
   
   public MascDataSource()
   {
      logger.info("Creating a MASC data source.");
      keys = new HashSet<String>();
      for (String key : index.keys())
      {
         keys.add(key);
      }
   }

   public MascDataSource(Set<String> keys)
   {
      logger.info("Creating a filtered MASC data source.");
      this.keys = keys;
   }
   
   @Override
   public Data list()
   {        
      logger.info("Listing.");
      return new Data(Types.INDEX, collect(keys));
   }

   @Override
   public Data get(String key)
   {
      logger.info("Getting document for {}", key);
      File file = index.get(key);
      if (file == null)
      {
         logger.error("No such file.");
         return new Data(Types.ERROR, "No such file.");
      }
      if (!file.exists())
      {
         logger.error("File not found.");
         return new Data(Types.ERROR, "File not found.");
      }
      
      UTF8Reader reader = null;
      String payload = null;
      long type = Types.decode(key);
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
   public Data query(String query)
   {
      List<String> list = new ArrayList<String>();
      for (String key : keys)
      {
         if (key.endsWith(query))
         {
            list.add(key);
         }
      }
      
      return new Data(Types.INDEX, collect(list));
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
