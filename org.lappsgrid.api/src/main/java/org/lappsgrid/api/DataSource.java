package org.lappsgrid.api;

import jp.go.nict.langrid.commons.rpc.intf.Service;


/**
 * A DataSource returns {@link Data} objects. 
 * <p>
 * The type of Data returned by a data source depends on the data 
 * source implementation. Possibilities include, but are not limited
 * to, entire documents, segments of documents, XML documents (or
 * fragments), single words, or lists of annotations.
 * 
 * @author Keith Suderman
 *
 */
@Service(namespace="lapps:data:source")
public interface DataSource
{
   /**
    * Returns a list of the ID values for the objects managed by
    * this data source.
    * @return
    */
   Data list();
   
   /**
    * Returns the data object corresponding to the given id.
    * <p>
    * If there is no such object then this method should return
    * an error {@link Data} object and use the <code>payload</code>
    * field to return an error message. 
    * 
    * @param id The id value of the object to be returned.
    * @return
    */
   Data get(String id);
   
   /**
    * Returns a list of the ID values of the data objects that match
    * the given query.  
    * 
    * @param query
    * @return
    */
   Data query(String query);
}
