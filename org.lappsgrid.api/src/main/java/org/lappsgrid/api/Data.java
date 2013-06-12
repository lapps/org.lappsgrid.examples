package org.lappsgrid.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;


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
   private static final long serialVersionUID = 2L;
   private static final String UTF8 = "UTF-8";
   
   private long discriminator;
   private byte[] payload;
   
   public Data(long type)
   {
      this.discriminator = type;
   }
   
   public Data(long type, String payload)
   {
      this.discriminator = type;           
      this.payload = payload.getBytes();
   }
   
   public Data(long type, byte[] payload)
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
      if (payload == null)
      {
         return null;
      }
      return new String(payload);
   }
   
   public byte[] getBytes()
   {
      return payload;
   }
   
   public void setDiscriminator(long discriminator) 
   {
      this.discriminator = discriminator;
   }
   
   public void setPayload(String payload)
   {
      this.payload = payload.getBytes();
   }

   public void setPayload(byte[] bytes)
   {
      this.payload = bytes;
   }
   
   @Override
   public void readObject(ObjectInputStream stream) throws IOException,
         ClassNotFoundException
   {
      discriminator = stream.readLong();
      int size = stream.readInt();
      payload = new byte[size];
      stream.read(payload);
   }

   @Override
   public void writeObject(ObjectOutputStream stream) throws IOException,
         ClassNotFoundException
   {
      stream.writeLong(discriminator);
      stream.writeInt(payload.length);
      stream.write(payload);
   }
   
   
}
