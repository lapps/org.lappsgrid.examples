package org.anc.grid.data.masc.client.serial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.Constants;
import org.apache.axis.encoding.DeserializerFactory;

class DataDeserializerFactory implements DeserializerFactory
{
   List<String> types = new ArrayList<String>();
   
   public DataDeserializerFactory()
   {
      types.add(Constants.AXIS_SAX);
   }
   

   @Override
   public javax.xml.rpc.encoding.Deserializer getDeserializerAs(String arg0)
   {
      return new DataDeserializer();
   }

   @Override
   public Iterator getSupportedMechanismTypes()
   {
      return types.iterator();
   }
   
}

