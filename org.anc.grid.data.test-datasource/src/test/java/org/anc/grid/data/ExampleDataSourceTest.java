package org.anc.grid.data;

import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.api.RawDataSource;
import org.junit.*;
import static org.junit.Assert.*;

public class ExampleDataSourceTest
{
   protected RawDataSource source;
   
   public ExampleDataSourceTest()
   {
   }


   @Before
   public void setup()
   {
      source = new ExampleDataSource();
   }
   
   @After
   public void tearDown()
   {
      source = null;
   }
   
   @Test
   public void testQuery()
   {
      Data result = source.query("list");
      assertTrue("Result is null.", result != null);
      long actual = result.getDiscriminator();
      long expected = ExampleDataSource.ERROR;
      String message = "Expected: " + expected + " Actual: "+ actual;
      assertTrue(message, actual == expected);
      assertTrue("Wrong error message.", "Unsupported operation.".equals(result.getPayload()));
   }
}
