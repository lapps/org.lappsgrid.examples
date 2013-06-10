package org.lappsgrid.api;

/**
 * Exception when found no such data source.
 *
 * @author Di Wang
 */
public class NoSuchDataSourceException extends DataSourceException{

   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = 7624347855738751011L;

   public NoSuchDataSourceException()
   {
      super();
   }

   public NoSuchDataSourceException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public NoSuchDataSourceException(String message)
   {
      super(message);
   }

   public NoSuchDataSourceException(Throwable cause)
   {
      super(cause);
   }
}
