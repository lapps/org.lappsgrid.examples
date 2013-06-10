package org.lappsgrid.api;


/**
 * Exception for Data Source in general.
 * 
 * @author Di Wang
 */
public class DataSourceException extends LappsException {
   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = 196698557475849982L;
   
   public DataSourceException()
   {
      super();
   }

   public DataSourceException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public DataSourceException(String message)
   {
      super(message);
   }

   public DataSourceException(Throwable cause)
   {
      super(cause);
   }

}
