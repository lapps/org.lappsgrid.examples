package org.lappsgrid.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Data objects passed between LAPPS web services.
 * <p>
 * Data objects consist of a <code>discrimintator</code> and a 
 * <code>payload</code>. The payload field carries the data and the discriminator is used to 
 * hold any type information for the data.
 * <p>
 * Discriminator values are represented as <code>long</code> values in
 * this implementation.  A central "Discriminator Registry" should be
 * consulted to map discriminator names to values and vice-versa.
 * 
 * @author Keith Suderman
 *
 */
public class Data implements LappsSerializable
{
   private static final long serialVersionUID = 1L;
   
   private long discriminator;
   private String payload;
   
   public Data(long type, String payload)
   {
      this.discriminator = type;
      this.payload = payload;
   }
   
   public long getDiscriminator()
   {
      return discriminator;
   }
   
   public String getPayload()
   {     
      return payload;
   }
   
   public void setDiscriminator(long discriminator) 
   {
      this.discriminator = discriminator;
   }
   
   public void setPayload(String payload)
   {
      this.payload = payload;
   }

   @Override
   public void readObject(ObjectInputStream stream) throws IOException,
         ClassNotFoundException
   {
      discriminator = stream.readLong();
      payload = stream.readUTF();
   }

   @Override
   public void writeObject(ObjectOutputStream stream) throws IOException,
         ClassNotFoundException
   {
      stream.writeLong(discriminator);
      stream.writeUTF(payload);
   }
   
   
}
