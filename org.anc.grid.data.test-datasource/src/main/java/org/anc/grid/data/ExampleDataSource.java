package org.anc.grid.data;

import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.api.RawDataSource;
import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

public class ExampleDataSource implements RawDataSource
{
   public static final long ERROR = DiscriminatorRegistry.get("error");
   
   public ExampleDataSource()
   {
   }

   @Override
   public Data query(String query)
   {
      return new Data(ERROR, "Unsupported operation.");
   }

}
