package org.anc.grid.data.masc.client;

import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

public final class Types
{
   public static long ERROR = DiscriminatorRegistry.get("error");
   public static long INDEX = DiscriminatorRegistry.get("index");
   
   private Types()
   {
   }
}
