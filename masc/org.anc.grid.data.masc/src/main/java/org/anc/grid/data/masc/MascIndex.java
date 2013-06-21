package org.anc.grid.data.masc;

import java.io.*;
import java.util.*;

/**
 * Maps MASC document ID values to the physical location
 * of the file on disk.
 * 
 * @author Keith Suderman
 *
 */
public class MascIndex
{
   private Map<String,File> index = new HashMap<String,File>();
   
   public MascIndex()
   {
   }

   public int size()
   {
      return index.size();
   }
   
   public File get(String key)
   {
      return index.get(key);
   }
   
   public List<String> keys()
   {
      return new ArrayList<String>(index.keySet());
   }

   public boolean load()
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      if (loader == null)
      {
         loader = this.getClass().getClassLoader();
      }
      
      InputStream stream = loader.getResourceAsStream("/masc3.index");
      if (stream == null)
      {
         return false;
      }
      
      boolean result = true;
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      try 
      {
         String line = reader.readLine();
         while (line != null)
         {
            String[] parts = line.split(" ");
            index.put(parts[0], new File(parts[1]));
            line = reader.readLine();
         }
      }
      catch (IOException e)
      {
         result = false;
      }
      
      try 
      {  
         reader.close(); 
      } 
      catch (Exception e) 
      { 
         // Ignore.
      }
      return result;
   }
}
