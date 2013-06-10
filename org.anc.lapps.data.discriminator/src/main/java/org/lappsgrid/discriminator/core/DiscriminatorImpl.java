package org.lappsgrid.discriminator.core;

import java.util.*;

public class DiscriminatorImpl implements Discriminator
{
   private static final long serialVersionUID = 1L;
   
   private String name;
   private int hashCode;
   private Set<Discriminator> parents;
   
   protected DiscriminatorImpl(String name)
   {
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
   }
   
   public DiscriminatorImpl(String name, Discriminator parent)   
   {
      this.name = name;
      this.hashCode = name.hashCode(); 
      this.parents = new HashSet<Discriminator>();
      if (parent != null)
      {
         this.parents.add(parent);
      }
   }
   
   public DiscriminatorImpl(String name, List<Discriminator> parents)
   {
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
      for (Discriminator d : parents)
      {
         this.parents.add(d);
      }
   }
   
   public DiscriminatorImpl(String name, Discriminator[] parents)
   {
      this.name = name;
      this.hashCode = name.hashCode();
      this.parents = new HashSet<Discriminator>();
      for (Discriminator d : parents)
      {
         this.parents.add(d);
      }
   }
   public String getId() { return name; }
   
   public boolean isa(Discriminator d)
   {
      if (name.equals(d.getId()))
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
      return name.equals(((Discriminator) d).getId());
   }

   @Override
   public int hashCode()
   {
      return hashCode;
   }
}
