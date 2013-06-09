package org.lappsgrid.api;

import jp.go.nict.langrid.commons.rpc.intf.Service;

/**
 * A SimpleDataSource returns {@link Data} objects in response to
 * a query. 
 * <p>
 * The type of Data returned by a data source depends on the data 
 * source implementation. Possibilities include, but are not limited
 * to, entire documents, segments of documents, XML documents (or
 * fragments), single words, or lists of annotations.
 * 
 * @author Keith Suderman
 *
 */
@Service(namespace="lapps:simple-datasource")
public interface SimpleDataSource {
	Data query(long type, String query);
}
