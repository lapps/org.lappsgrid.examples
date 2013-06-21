package org.lapps.datasource.trecgenomics;

import java.util.ArrayList;
import java.util.List;

import org.lapps.datasource.Data;
import org.lapps.datasource.DataQuery;
import org.lapps.datasource.DataSource;
import org.lapps.datasource.DataStreamReader;
import org.lapps.datasource.ListDataSource;
import org.lapps.datasource.ListDataStreamReader;
import org.lapps.datasource.ViewOptions;

public class TrecDataSourceClient extends ListDataSource {

  private TrecPrimitiveDataSource trecDataSource;

  public TrecDataSourceClient() {
    super();
    TrecPrimitiveDataSourceService trecService = new TrecPrimitiveDataSourceService();
    trecDataSource = trecService.getTrecPrimitiveDataSourcePort();
  }

  public TrecDataSourceClient(List<String> docId) {
    this();
    setDocIdList(docId);
  }

  @Override
  public DataSource subCorpus(DataQuery query) {
    Data list = trecDataSource.query(query);
    String[] idArr = list.getPayload().split("\\s+");
    List<String> idList = new ArrayList<String>();
    for (String id : idArr) {
      idList.add(id);
    }
    return new TrecDataSourceClient(idList);
  }

  @Override
  public DataStreamReader getDataStreamReader(ViewOptions options) {
    return new ListDataStreamReader(this, options);
  }

  @Override
  public Data getData(ViewOptions options, String documentID) {
    DataQuery dataQuery = new DataQuery("SELECT " + documentID);
    return trecDataSource.query(dataQuery);
  }

}
