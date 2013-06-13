package org.lappsgrid.client;

import java.util.Iterator;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.DataSource;
import org.lappsgrid.api.DataSourceIterator;
import org.lappsgrid.core.DataFactory;

/**
 * An iterator implementation that is able to iterate over all the elements
 * in a DataSource object.
 * @author Keith Suderman
 *
 */
public class ListDataSourceIterator implements DataSourceIterator
{
   protected DataSource dataSource;
   protected Iterator<String> iterator;

   public ListDataSourceIterator(DataSource dataSource, List<String> keys)
   {
      this.dataSource = dataSource;
      this.iterator = keys.iterator();
   }

   @Override
   public boolean hasNext()
   {
      return iterator.hasNext();
   }

   @Override
   public Data next()
   {
      if (!iterator.hasNext())
      {
         return DataFactory.error("No such element.");
      }

      String key = iterator.next();
      return dataSource.query(DataFactory.get(key));
   }

   @Override
   public void remove()
   {
      throw new UnsupportedOperationException(
            "Removing elements via an iterator is not allowed.");
   }

}
