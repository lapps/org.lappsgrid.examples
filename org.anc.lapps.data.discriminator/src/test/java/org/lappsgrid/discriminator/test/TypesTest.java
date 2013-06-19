package org.lappsgrid.discriminator.test;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Ignore;
import org.junit.Test;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;

public class TypesTest
{
    @Test
    public void testRegex()
    {
        long code = DiscriminatorRegistry.get("regex");
        assertTrue(code > 0);
    }

   @Test
   public void testTypes() throws IllegalArgumentException, IllegalAccessException
   {
      assertTrue(Types.ERROR == 0);
      Field[] fields = Types.class.getDeclaredFields();
      for (Field field : fields)
      {
         long value = field.getLong(null);
         assertTrue("Invalid value for " + field.getName(), value >= 0);
      }
   }
   
}
