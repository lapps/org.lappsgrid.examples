package org.lappsgrid.core;

import org.lappsgrid.api.Data;
import org.lappsgrid.discriminator.Types;

/**
 * Queries over {@link org.lappsgrid.api.DataSource DataSource} to select matching documents.
 * Possible queries include:
 * 1) IR query for lucene/indri e.g. "(president AND Obama)";
 * 2) regular expression matching query e.g. "(president .* Obama)" over text field and/or annotation field
 * 3) composite query built from several queries e.g. "(query1 OR !query2)"
 *
 * @author Di Wang
 */
public class DataQuery extends Data {
  
  private static final long serialVersionUID = 8009560790223408090L;

  public DataQuery(){
    super(Types.QUERY);
  }
  
  public DataQuery(String queryStr){
    this();
    setPayload(queryStr);
  }
  
}
