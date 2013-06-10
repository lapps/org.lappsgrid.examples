package org.lappsgrid.discriminator.core;

import java.util.*;

public class DiscriminatorImpl implements Discriminator
{
   private static final long serialVersionUID = 1L;
   
   private long id;
   private String name;
   private int hashCode;
   private Set<Discriminator> parents;
   
   protected DiscriminatorImpl(String name, long id)
   {
      this.id = id;
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
   }
   
   public DiscriminatorImpl(String name, Discriminator parent, long id)   
   {
      this.id = id;
      this.name = name;
      this.hashCode = name.hashCode(); 
      this.parents = new HashSet<Discriminator>();
      if (parent != null)
      {
         this.parents.add(parent);
      }
   }
   
   public DiscriminatorImpl(String name, List<Discriminator> parents, long id)
   {
      this.id = id;
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
      for (Discriminator d : parents)
      {
         this.parents.add(d);
      }
   }
   
   public DiscriminatorImpl(String name, Discriminator[] parents, long id)
   {
      this.id = id;
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
      for (Discriminator d : parents)
      {
         this.parents.add(d);
      }
   }
   public long getId() { return id; }
   
   public boolean isa(Discriminator d)
   {
      if (id  == d.getId())
      {
         return true;
      }      
      if (parents.size() == 0)
      {
         return false;
      }
      for (Discriminator parent : parents)
      {
         if (parent.isa(d))
         {
            return true;
         }
      }
      return false;
   }

   
   @Override
   public boolean equals(Object d)
   {
      if (!(d instanceof Discriminator))
      {
         return false;
      }
      return id == ((Discriminator)d).getId();
   }

   @Override
   public int hashCode()
   {
      return hashCode;
   }
}
