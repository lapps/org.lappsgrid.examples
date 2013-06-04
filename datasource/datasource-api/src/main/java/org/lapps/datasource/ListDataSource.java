package org.lapps.datasource;

import java.util.List;


public abstract class ListDataSource implements DataSource {
  protected List<String> docIdList;

  public List<String> getDocIdList() {
    return docIdList;
  }

  public void setDocIdList(List<String> docIdList) {
    this.docIdList = docIdList;
  }
  
  
}
