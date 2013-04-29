package org.anc.grid.data.masc.client.serial;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.anc.lapps.data.api.Data;
import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.wsdl.fromJava.Types;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

class DataSerializer implements org.apache.axis.encoding.Serializer
{

   @Override
   public String getMechanismType()
   {
      return Constants.AXIS_SAX;
   }

   @Override
   public void serialize(QName qname, Attributes atts, Object object,
         SerializationContext context) throws IOException
   {
      if (!(object instanceof Data))
      {
         throw new IOException("Invalid type for serializer.");
      }
      Data data = (Data) object;
      context.startElement(qname, atts);
      AttributesImpl payloadAtts = new AttributesImpl();
      payloadAtts.addAttribute("", "type", "type", "String", Long.toString(data.getDiscriminator()));
      context.serialize(new QName("", "payload"), payloadAtts, data.getPayload());
   }

   @Override
   public Element writeSchema(Class arg0, Types arg1) throws Exception
   {
      return null;
   }
   
}
