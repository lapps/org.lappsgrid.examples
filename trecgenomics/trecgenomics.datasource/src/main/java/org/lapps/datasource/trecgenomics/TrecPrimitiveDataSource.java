package org.lapps.datasource.trecgenomics;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.DataSource;
import org.lappsgrid.core.DataQuery;
import org.lappsgrid.discriminator.Types;

/**
 * A fake data source mimic trec genomic dataset
 * 
 * @author Di Wang
 */

@WebService(endpointInterface = "org.lappsgrid.api.DataSource")
public class TrecPrimitiveDataSource implements DataSource {

  @WebMethod
  @Override
  public Data query(Data query) {

    long discriminator = query.getDiscriminator();
    if (discriminator == Types.LIST) {
      Data result = new Data(Types.STRING_LIST, "1 2 3 5 7");
      return result;
    } else if (discriminator == Types.QUERY) {
      String[] queryWords = query.getPayload().split("\\s+");
      if (queryWords.length < 2 || !queryWords[0].toLowerCase().equals("select")) {
        throw new IllegalArgumentException("Query format is not supported.");
      }
      if (queryWords.length == 2) {
        Data result = new Data(Types.TEXT, "DOCUMENT No." + queryWords[1]);
        return result;
      } else {
        StringBuffer payloadBuf = new StringBuffer();
        for (int i = 1; i < queryWords.length; i++) {
          int docId = Integer.parseInt(queryWords[i]);
          payloadBuf.append(docId * 13).append(" ");
        }
        Data result = new Data(Types.STRING_LIST, payloadBuf.toString());
        return result;
      }
    } else if (discriminator == Types.GET) {
      Data result = new Data(Types.TEXT, "DOCUMENT No." + query.getPayload());
      return result;
    } else {
      throw new IllegalArgumentException("Query type is not supported.");
    }

  }
}
