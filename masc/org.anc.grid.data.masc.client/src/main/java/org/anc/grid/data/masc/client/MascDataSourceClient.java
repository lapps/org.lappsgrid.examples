package org.anc.grid.data.masc.client;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.api.DataSource;
import org.anc.soap.client.AbstractSoapClient;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

public class MascDataSourceClient extends AbstractSoapClient implements DataSource
{
   private static class Service {
	      public static final String NAMESPACE = "http://localhost:8080/anc2go/services/MascXmlProcessorService";
	      public static final String ENDPOINT = "http://localhost:8080/anc2go/services/MascXmlProcessorService";
   };

	// The service on grid.anc.org
//   private static class Service {
//      public static final String NAMESPACE = "http://grid.anc.org:8080/MascDataSource/services/MascDataSource";
//      public static final String ENDPOINT = "http://grid.anc.org:8080/MascDataSource/services/MascDataSource"; 
//   };
   
   // The service on the service grid.
    
   public MascDataSourceClient(String user, String password) throws ServiceException
   {
      super(Service.NAMESPACE);
      super.setEndpoint(Service.ENDPOINT);
      super.setCredentials(user, password);            
      QName q = new QName ("uri:org.anc.lapps.data.api/", "Data"); 
      BeanSerializerFactory serializer =   new BeanSerializerFactory(Data.class,q);   // step 2
      BeanDeserializerFactory deserializer = new BeanDeserializerFactory(Data.class,q);  // step 3
      call.registerTypeMapping(Data.class, q, serializer, deserializer); //step 4
   }

   @Override
   public Data list()
   {
      Data data = null;
      try
      {
         data = (Data) super.invoke("list");
      }
      catch (RemoteException e)
      {
         
      } 
      return data; 
   }

   @Override
   public Data get(String id)
   {
      String[] args = { id };
      Data data = null;
      try
      {
         data = (Data) super.invoke("get", args);
      }
      catch (RemoteException e)
      {
         e.printStackTrace();
      } 
      return data; 
   }

   @Override
   public Data query(String query)
   {
      String[] args = { query };
      Data data = null;
      try
      {
         data = (Data) super.invoke("query", args);
      }
      catch (RemoteException e)
      {
         e.printStackTrace();
      } 
      return data; 
   }
   
   

}

