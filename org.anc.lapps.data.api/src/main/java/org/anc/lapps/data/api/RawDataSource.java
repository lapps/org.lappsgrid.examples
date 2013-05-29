package org.anc.lapps.data.api;

import jp.go.nict.langrid.commons.rpc.intf.Service;

@Service
public interface RawDataSource {
	Data query(Data query);
	Data query(long type, String query);
}
