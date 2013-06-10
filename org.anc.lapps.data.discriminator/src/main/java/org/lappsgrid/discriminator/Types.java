package org.lappsgrid.discriminator;

/**
 * Static final definitions of the most commonly used discriminator types.
 * 
 * @author Keith Suderman
 *
 */
public final class Types
{
   public static final long ERROR = get("error");
   public static final long TEXT = get("text");
   public static final long DOCUMENT = get("document");
   public static final long XML = get("xml");
   public static final long INDEX = get("index");
   public static final long QUERY = get("input-param-query");
   public static final long LIST = get("input-param-list");
   public static final long GET = get("input-param-get");
   public static final long GATE_DOCUMENT = get("gate");
   public static final long GRAF_RESOURCE_HEADER = get("graf-res-hdr");
   public static final long GRAF_DOCUMENT_HEADER = get("graf-doc-hdr");
   public static final long GRAF_STANDOFF_XML = get("graf-xml");
   
   // Don't allow instances of this class to be created.
   private Types()
   {
   }

   private static long get(String name) {
      return DiscriminatorRegistry.get(name);
   }
}
