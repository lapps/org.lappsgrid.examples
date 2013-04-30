package org.anc.grid.data.masc.client.serial;

import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Target;
import org.apache.axis.message.SOAPHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

class DataDeserializer extends SOAPHandler implements Deserializer
{

   @Override
   public String getMechanismType()
   {
      return Constants.AXIS_SAX;
   }

   @Override
   public void setValue(Object arg0, Object arg1) throws SAXException
   {
      System.out.println("Setting " + arg0.toString() + " to " + arg1.toString());
   }

   @Override
   public boolean componentsReady()
   {
      System.out.println("DataDeserializer.componentsReady()");
      return true;
   }

   @Override
   public void endElement(String arg0, String arg1, DeserializationContext context)
         throws SAXException
   {
      System.out.println("end Element " + arg0 + " " + arg1);
   }

   @Override
   public QName getDefaultType()
   {
      System.out.println("DataDeserializer.getDefaultType()");
      return null;
   }

   @Override
   public Object getValue()
   {
      System.out.println("DataDeserializer.getValue()");
      return null;
   }

   @Override
   public Object getValue(Object arg0)
   {
      System.out.println("DataDeserializer.getValue() " + arg0.toString());
      return null;
   }

   @Override
   public Vector getValueTargets()
   {
      System.out.println("DataDeserializer.getValueTargets()");
      return null;
   }

   @Override
   public void moveValueTargets(Deserializer arg0)
   {
      System.out.println("DataDeserializer.moveValueTargets()");
   }

   @Override
   public void onEndElement(String arg0, String arg1,
         DeserializationContext arg2) throws SAXException
   {
      System.out.println("DataDeserializer.onEndElement() " + arg0 + " " + arg1);
   }

   @Override
   public SOAPHandler onStartChild(String namespace, String localName, String prefix,
         Attributes atts, DeserializationContext context) throws SAXException
   {
      System.out.println("DataDeserializer.onStartChild()");
      return null;
   }

   @Override
   public void onStartElement(String namespace, String localName, String prefix,
         Attributes atts, DeserializationContext context) throws SAXException
   {
      System.out.println("DataDeserializer.onStartElement()");
      System.out.println("  ns=" + namespace + " name=" + localName + " prefix" + prefix);
   }

   @Override
   public void registerValueTarget(Target target)
   {
      System.out.println("DataDeserializer.registerValueTarget() " + target.toString());
   }

   @Override
   public void removeValueTargets()
   {
      System.out.println("DataDeserializer.removeValueTargets()");
   }

   @Override
   public void setChildValue(Object arg0, Object arg1) throws SAXException
   {
      System.out.println("DataDeserializer.setChileValue() " + arg0.toString() + " " + arg1.toString());
   }

   @Override
   public void setDefaultType(QName arg0)
   {
      System.out.println("DataDeserializer.setDefaultType()" + arg0.toString());
   }

   @Override
   public void setValue(Object arg0)
   {
      System.out.println("DataDeserializer.setValue() " + arg0.toString());
   }

   @Override
   public void startElement(String namespace, String local, String prefix,
         Attributes atts, DeserializationContext context) throws SAXException
   {
      System.out.println("DataDeserializer.startElement()");
      System.out.println(String.format("   %s %s %s", namespace, local, prefix));
   }

   @Override
   public void valueComplete() throws SAXException
   {
      System.out.println("DataDeserializer.valueComplete()");
   }
  
}

