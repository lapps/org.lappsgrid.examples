package org.lappsgrid.discriminator.test;

import org.junit.*;
import org.lappsgrid.discriminator.Types;

import static org.junit.Assert.*;

public class TypesTest
{
   @Test
   public void testTypes()
   {
      assertTrue(Types.ERROR == 0);
      assertTrue(Types.DOCUMENT > 0);
      assertTrue(Types.GATE_DOCUMENT > 0);
      assertTrue(Types.GET > 0);
      assertTrue(Types.GRAF_DOCUMENT_HEADER > 0);
      assertTrue(Types.GRAF_RESOURCE_HEADER > 0);
      assertTrue(Types.GRAF_STANDOFF_XML > 0);
      assertTrue(Types.INDEX > 0);
      assertTrue(Types.LIST > 0);
      assertTrue(Types.QUERY > 0);
      assertTrue(Types.TEXT > 0);
      assertTrue(Types.XML > 0);
   }
}
