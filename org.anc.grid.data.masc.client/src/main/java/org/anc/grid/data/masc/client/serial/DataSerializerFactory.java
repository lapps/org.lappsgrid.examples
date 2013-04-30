package org.anc.grid.data.masc.client.serial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.Constants;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.SerializerFactory;

class DataSerializerFactory implements SerializerFactory
{

   List<String> types = new ArrayList<String>();
   
   public DataSerializerFactory()
   {
      types.add(Constants.AXIS_SAX);
   }
   
   @Override
   public Serializer getSerializerAs(String ignored)
   {
      return new DataSerializer();
   }

   @Override
   public Iterator getSupportedMechanismTypes()
   {
      return types.iterator();
   }
   
}


