package org.lappsgrid.api;

/**
 * Base exception type for all Exception classes defined in the LAPPS
 * framework.
 * 
 * @author Keith Suderman
 *
 */
public class LappsException extends Exception
{
   private static final long serialVersionUID = 1L;

   public LappsException()
   {
   }

   public LappsException(String message)
   {
      super(message);
   }

   public LappsException(Throwable cause)
   {
      super(cause);
   }

   public LappsException(String message, Throwable cause)
   {
      super(message, cause);
   }

}
