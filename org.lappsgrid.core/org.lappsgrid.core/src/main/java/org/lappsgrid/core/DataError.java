package org.lappsgrid.core;

import org.lappsgrid.api.Data;
import org.lappsgrid.discriminator.Types;

/**
 * A {@link org.lappsgrid.api.Data Data} object for returning ERROR conditions
 * from {@link org.lappsgrid.api.DataSource DataSource} objects.
 * 
 * @author Keith Suderman
 *
 */
public class DataError extends Data
{
   private static final long serialVersionUID = 1L;
   
   public DataError(String message)
   {
      super(Types.ERROR, message);
   }

}
