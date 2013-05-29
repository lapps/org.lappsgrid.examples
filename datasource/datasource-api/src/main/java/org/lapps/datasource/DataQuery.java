package org.lapps.datasource;

/**
 * Queries over {@link DataSource} to select matching documents.
 * Possible queries include:
 * 1) IR query for lucene/indri e.g. "(president AND Obama)";
 * 2) regular expression matching query e.g. "(president .* Obama)" over text field and/or annotation field
 * 3) composite query built from several queries e.g. "(query1 OR !query2)"
 *
 * @author Di Wang
 */
public class DataQuery extends Data {
  
//  public DataQuery(String discriminator, String payload){
//    this.discriminator = discriminator;
//    this.payload = payload;
//  }
  
  private static final long serialVersionUID = 8009560790223408090L;

  public DataQuery(){
    super();
    setDiscriminator("QUERY");
  }
  
  public DataQuery(String queryStr){
    this();
    setPayload(queryStr);
  }
  
}
