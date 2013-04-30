package org.anc.grid.data.masc;

import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

/**
 * A helper class that caches Discriminator values in
 * constant (final) fields for easy access.
 * 
 * @author Keith Suderman
 *
 */
public class Types
{
   public static final long ERROR = get("error");
   public static final long INDEX = get("index");
   public static final long HEADER = get("graf-hdr");
   public static final long TEXT = get("text");
   public static final long XML = get("xml");
   public static final long GRAF_XML = get("graf-xml");
   public static final long META_XML = get("meta-xml");
         
   private Types()
   {
   }

   static public long decode(String key)
   {
      if (key.endsWith("-hdr"))
      {
         return META_XML;
      }
      if (key.endsWith("-txt"))
      {
         return TEXT;
      }
      return GRAF_XML;
   }
   
   static public long get(String name) 
   {
      return DiscriminatorRegistry.get(name);
   }
}
