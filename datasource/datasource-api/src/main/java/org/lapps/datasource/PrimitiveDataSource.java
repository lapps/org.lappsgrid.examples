package org.lapps.datasource;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * A low-level data source api can be queried.
 * 
 * @author Di Wang
 * 
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface PrimitiveDataSource {

  /**
   * queries the data source.
   *
   * @param query the input query
   * @return the result of the query over data source
   */
  @WebMethod
  public Data query(@WebParam(name = "query") DataQuery query);

}
