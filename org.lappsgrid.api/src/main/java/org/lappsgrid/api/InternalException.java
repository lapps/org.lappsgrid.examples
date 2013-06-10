package org.lappsgrid.api;

/**
 * An exception type to be thrown and caught internally by a LAPPS service.
 * <p>
 * These exceptions should never be exposed to the user.
 * 
 * @author Keith Suderman
 *
 */
public class InternalException extends LappsException
{
   private static final long serialVersionUID = 1L;

   public InternalException()
   {
      super();
   }

   public InternalException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public InternalException(String message)
   {
      super(message);
   }

   public InternalException(Throwable cause)
   {
      super(cause);
   }

}
