package org.anc.lapps.data.discriminator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.anc.lapps.data.discriminator.core.Discriminator;
import org.anc.lapps.data.discriminator.core.DiscriminatorImpl;


/**
 * The DiscriminatorRegistry serves as the source of all
 * knowledge (SOAK) for {@link Discriminator} objects. 
 * <p>
 * Discriminator objects are never exposed directly to users.  
 * Instead the <code>DiscriminatorRegistry</code> class is used to
 * map discriminator names to long values, and vice-versa.
 * <p>
 * This class is subject to change as needs evolve.
 * 
 * @author Keith Suderman
 *
 */
public class DiscriminatorRegistry
{
   static private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   static private boolean initialized = false;
   
   static private long nextId = 0;
   static private Map<String,Discriminator> registered = new HashMap<String, Discriminator>();
//   static private Map<Long,String> index = new HashMap<Long,String>();
   
   static {
      initialize();
   }
   
   /**
    * The <code>register</code> methods should only be used during
    * testing and development. Calling this <code>register</code>
    * method is <b>not</b> the way to register new discriminators 
    * with the registry.
    * 
    */
   static public String register(String name)
   {
      return register(name, (Discriminator)null);
   }

   /**
    * The <code>register</code> methods should only be used during
    * testing and development. Calling this <code>register</code>
    * method is <b>not</b> the way to register new discriminators 
    * with the registry.
    * 
    */
   static public String register(String name, Discriminator parent)
   {
      lock.writeLock().lock();
      try
      {
         Discriminator d = registered.get(name);
         if (d == null)
         {
            d = create(name, parent);
            registered.put(name, d);
//            index.put(d.getId(), name);
         }
         return d.getId();
      }
      finally
      {
         lock.writeLock().unlock();
      }
   }

   /**
    * The <code>register</code> methods should only be used during
    * testing and development. Calling this <code>register</code>
    * method is <b>not</b> the way to register new discriminators 
    * with the registry.
    * 
    */
   static public String register(String name, List<Discriminator> parents)
   {
      lock.writeLock().lock();
      try
      {
         Discriminator d = registered.get(name);
         if (d == null)
         {
            d = create(name, parents);
            registered.put(name, d);
//            index.put(d.getId(), name);
         }
         return d.getId();
      }
      finally
      {
         lock.writeLock().unlock();
      }
   }

   /**
    * Returns a list (array) of all discriminator values used
    * by the registry.
    * 
    */
   static public Long[] types()
   {
      lock.readLock().lock();
      try
      {
         Long[] array = new Long[registered.size()];
         registered.values().toArray(array);
         return array;
      }
      finally
      {
         lock.readLock().unlock();
      }
   }

   /**
    * Returns a non-negative number corresponding to the 
    * discriminator value associated with this <code>name</code>.
    * Returns a negative value is there is no such discriminator.
    * 
    * @param name The name of the discriminator
    * @return The id value associated with the discriminator name, or
    * -1 if there is no such discriminator.
    */
   static public String get(String name)
   {
      Discriminator d;
      lock.readLock().lock();
      try
      {
         d = registered.get(name);
      }
      finally
      {
         lock.readLock().unlock();
      }
      if (d == null)
      {
         return "error";
      }
      return d.getId();
   }

   /**
    * Returns true if the discriminator <code>parentName</code>
    * is a super-type of the <code>name</code> discriminator.
    * Returns false otherwise.
    */
   public static boolean isa(String name, String parentName)
   {
      Discriminator d = registered.get(name);
      if (d == null)
      {
         return false;
      }
      Discriminator parent = registered.get(parentName);
      if (parent == null)
      {
         return false;
      }
      
      return d.isa(parent);
   }
   
   /** Initializes the DiscriminatorRegistry.
    * <p>
    * Currently the registry is initialized with values read from a
    * flat text file. In production use the registry should be 
    * initialized from a proper type system of some sort.
    */
   static public void initialize() //throws IOException
   {
      lock.writeLock().lock();
      if (initialized)
      {
         lock.writeLock().unlock();
         return;
      }
      try
      {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         InputStream stream = loader.getResourceAsStream("DataTypes.txt");
         BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
         String line = reader.readLine();
         while (line != null)
         {
            line = trim(line);
            if (line.length() == 0)
            {
               continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length == 1)
            {
               register(parts[0]);
            }
            else if (parts.length > 1)
            {
               List<Discriminator> parents = new ArrayList<Discriminator>();
               for (int i = 1; i < parts.length; ++i)
               {
                  Discriminator parent = registered.get(parts[i]);
                  if (parent == null)
                  {
                     throw new IOException("Invalid parent type " + parts[1]);
                  }
                  parents.add(parent);
               }
               register(parts[0], parents);
            }
            line = reader.readLine();
         }
         
         initialized = true;
      }
      catch (IOException e)
      {
         // TODO In a production environment this error should be
         // logged.
      }
      finally
      {
         lock.writeLock().unlock();
      }
   }
   
   static private String trim(String line)
   {
      int index = line.indexOf('#');
      if (index >= 0)
      {
         return line.substring(0, index).trim();
      }
      index = line.indexOf("//");
      if (index > 0)
      {
         return line.substring(0, index).trim();
      }
      return line.trim();
   }
      
   static private Discriminator create(String name, Discriminator parent)
   {
      return new DiscriminatorImpl(name, parent);
   }

   static private Discriminator create(String name, List<Discriminator> parents)
   {
      return new DiscriminatorImpl(name, parents);
   }
}
