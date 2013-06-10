package org.lappsgrid.discriminator.test;

//import org.anc.lapps.data.discriminator.DiscriminatorRegistry;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lappsgrid.discriminator.DiscriminatorRegistry;

public class RegistryTest
{

   @Test
   public void testError()
   {
      long id = DiscriminatorRegistry.get("error");
      assertTrue( id >= 0 );
   }
   
   @Test
   public void testInvalidDiscriminatorName()
   {
      assertTrue(DiscriminatorRegistry.get("FooBar") < 0);
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
   
   protected void isa(String child, String parent)
   {
      assertTrue(child + " is not a " + parent, DiscriminatorRegistry.isa(child, parent));
   }
   
//   protected long get(String id)
//   {
//      return DiscriminatorRegistry.get(id);
//   }
//   
//   protected String get(long id)
//   {
//      return DiscriminatorRegistry.get(id);
//   }
   
   public RegistryTest()
   {
   }

   
}
