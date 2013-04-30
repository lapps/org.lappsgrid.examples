package org.anc.grid.data.masc.client.test;

import javax.xml.rpc.ServiceException;

import org.anc.grid.data.masc.client.MascDataSourceClient;
import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

public class DataSourceTest
{
   static final long ERROR = DiscriminatorRegistry.get("error");
   public void test() throws ServiceException
   {
      MascDataSourceClient  client = new MascDataSourceClient("Tester", "tester");
      Data listData = client.list();
      long type = listData.getDiscriminator();
      System.out.println("Returned type is: " + DiscriminatorRegistry.get(type));      
      String items = listData.getPayload();
//      String[] parts = items.split(" ");
//      for (String item : parts)
//      {
//         System.out.println(item);
//      }
      
      Data data = client.get("MASC2-0055-txt");
      type = listData.getDiscriminator();
      System.out.println("Returned type is: " + type + " "+ DiscriminatorRegistry.get(type));      
      System.out.println(data.getPayload());

      data = client.get("invalid id");
      type = listData.getDiscriminator();
      System.out.println("Returned type is: " + type + " "+ DiscriminatorRegistry.get(type));      
      System.out.println(data.getPayload());

   }
   
  
   public DataSourceTest()
   {
   }

   public static void main(String[] args)
   {
      try
      {
         new DataSourceTest().test();
      }
      catch (ServiceException e)
      {
         e.printStackTrace();
      }
   }
}
