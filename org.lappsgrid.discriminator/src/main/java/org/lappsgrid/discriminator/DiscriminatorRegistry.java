package org.lappsgrid.discriminator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.lappsgrid.discriminator.core.Discriminator;
import org.lappsgrid.discriminator.core.DiscriminatorImpl;


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
   static private Map<Long,String> index = new HashMap<Long,String>();
   
   static protected final long BANK_SIZE = 1024;
   
   static {
      try
      {
         initialize();
      }
      catch (IOException e)
      {
         // TODO This exception should be logged.
         e.printStackTrace();
      }
   }
   
   /**
    * The <code>register</code> methods should only be used during
    * testing and development. Calling this <code>register</code>
    * method is <b>not</b> the way to register new discriminators 
    * with the registry.
    * 
    */
   static public long register(String name)
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
   static public long register(String name, Discriminator parent)
   {
      name = name.toLowerCase();
      lock.writeLock().lock();
      try
      {
         Discriminator d = registered.get(name);
         if (d == null)
         {
            d = create(name, parent);
            registered.put(name, d);
            index.put(d.getId(), name);
//            System.out.println("Registered " + name + " with id " + d.getId());
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
   static public long register(String name, List<Discriminator> parents)
   {
      name = name.toLowerCase();
      lock.writeLock().lock();
      try
      {
         Discriminator d = registered.get(name);
         if (d == null)
         {
            d = create(name, parents);
            registered.put(name, d);
            index.put(d.getId(), name);
//            System.out.println("Registered " + name + " with id " + d.getId());
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
   static public long[] types()
   {
      lock.readLock().lock();
      try
      {
         int i = 0;
         long[] array = new long[registered.size()];
         for (Discriminator d : registered.values())
         {
            array[i] = d.getId();
            ++i;
         }
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
   static public long get(String name)
   {
      Discriminator d;
      lock.readLock().lock();
      try
      {
         d = registered.get(name.toLowerCase());
      }
      finally
      {
         lock.readLock().unlock();
      }
      if (d == null)
      {
         return -1;
      }
      return d.getId();
   }

   static public String get(long type)
   {
      String name = null;
      lock.readLock().lock();
      try
      {
         name = index.get(type);
      }
      finally
      {
         lock.readLock().unlock();
      }
      return name;
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
   
   /**
    * Returns true if the discriminator <code>parentName</code>
    * is a super-type of the <code>type</code> discriminator.
    * Returns false otherwise.
    */
   public static boolean isa(long type, long parentType)
   {
      String name = get(type);
      String parent = get(parentType);
      return isa(name, parent);
   }
   
   /** Initializes the DiscriminatorRegistry.
    * <p>
    * Currently the registry is initialized with values read from a
    * flat text file. In production use the registry should be 
    * initialized from a proper type system of some sort.
    */
   static public void initialize() throws IOException
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
//            System.out.println(line);
            line = trim(line);
            if (line.length() == 0)
            {
               line = reader.readLine();
               continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length == 1)
            {
               register(parts[0]);
            }
            else if (parts.length > 1)
            {
               int start = 0;
               String first = parts[start];
               if (first.endsWith(":"))
               {
                  // If the first part ends with a colon it is the next
                  // discriminator id value to be used.
                  ++start;
                  int end = first.length() - 1;
                  String token = first.substring(0,end);
                  long value = -1;
                  if (token.toLowerCase().startsWith("bank"))
                  {
                     value = Integer.parseInt(token.substring(4)) * BANK_SIZE;
                  }
                  else
                  {
                     value = Integer.parseInt(token);
                  }
                  if (value < nextId)
                  {
                     throw new IOException("Invalid ID value specified in the DataTypes configuration: " + value);
                  }
                  nextId = value;
               }
               List<Discriminator> parents = new ArrayList<Discriminator>();
               for (int i = start+1; i < parts.length; ++i)
               {
                  Discriminator parent = registered.get(parts[i]);
                  if (parent == null)
                  {
                     throw new IOException("Invalid parent type " + parts[i]);
                  }
                  parents.add(parent);
               }
               register(parts[start], parents);
            }
            line = reader.readLine();
         }
         
         initialized = true;
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
      return new DiscriminatorImpl(name, parent, nextId++);
   }

   static private Discriminator create(String name, List<Discriminator> parents)
   {
      return new DiscriminatorImpl(name, parents, nextId++);
   }
}
