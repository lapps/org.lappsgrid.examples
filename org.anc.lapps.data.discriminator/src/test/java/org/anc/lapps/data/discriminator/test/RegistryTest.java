package org.anc.lapps.data.discriminator.test;

//import org.anc.lapps.data.discriminator.DiscriminatorRegistry;
import org.anc.lapps.data.discriminator.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RegistryTest
{

   @Test
   public void testError()
   {
      long id = get("error");
      assertTrue( id >= 0);
   }
   
   @Test
   public void testIsa()
   {      
      isa("xml", "encoded");
   }
   
   @Test
   public void testGraf()
   {
      isa("graf-xml", "graf");
      isa("graf-xml", "xml");
      isa("graf-res-hdr", "graf");
      isa("graf-res-hdr", "meta-xml");
      isa("graf-doc-hdr", "graf");
      isa("graf-doc-hdr", "meta-xml");
      isa("graf-doc-hdr", "meta");
      isa("graf-doc-hdr", "xml");
      isa("graf-txt", "graf");
      isa("graf-txt", "text");
      isa("graf-xml", "graf");
      isa("graf-xml", "xml");

   }
   
   @Test
   public void testGet()
   {
      long longValue = get("error");
      String stringValue = get(longValue);
      assertTrue(stringValue.equals("error"));
   }
   
   @Test
   public void testAnc2Go()
   {
      long value = get("error");
      long a2g = get("anc2go");
      assertTrue(value != a2g);
   }
   
   protected void isa(String child, String parent)
   {
      assertTrue(child + " is not a " + parent, DiscriminatorRegistry.isa(child, parent));
   }
   
   protected long get(String id)
   {
      return DiscriminatorRegistry.get(id);
   }
   
   protected String get(long id)
   {
      return DiscriminatorRegistry.get(id);
   }
   
   public RegistryTest()
   {
   }

   
}
