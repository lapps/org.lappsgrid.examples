package org.lapps.datasource.trecgenomics;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.lapps.datasource.Data;
import org.lapps.datasource.DataQuery;
import org.lapps.datasource.PrimitiveDataSource;

/**
 * A fake data source mimic trec genomic dataset
 * 
 * @author Di Wang
 */
@WebService
public class TrecPrimitiveDataSource implements PrimitiveDataSource {

  @WebMethod
  @Override
  public Data query(DataQuery query) {

    if (!query.getDiscriminator().equals("QUERY")) {
      throw new IllegalArgumentException("Query type is not supported.");
    }

    String[] queryWords = query.getPayload().split("\\s+");
    if (queryWords.length < 2 || !queryWords[0].toLowerCase().equals("select")) {
      throw new IllegalArgumentException("Query format is not supported.");
    }

    if (queryWords.length == 2) {
      Data result = new Data();
      result.setDiscriminator("DOCUMENT");
      result.setPayload("DOCUMENT No." + queryWords[1]);
      return result;
    } else {
      Data result = new Data();
      result.setDiscriminator("IDs");
      StringBuffer payloadBuf = new StringBuffer();
      for (int i = 1; i < queryWords.length; i++) {
        int docId = Integer.parseInt(queryWords[i]);
        payloadBuf.append(docId * 13).append(" ");
      }
      result.setPayload(payloadBuf.toString());
      return result;
    }
  }

}
