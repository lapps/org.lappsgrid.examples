package org.lappsgrid.api;

import jp.go.nict.langrid.commons.rpc.intf.Service;

import org.lappsgrid.api.Data;

@Service(namespace="lapps:service")
public interface WebService
{
   /**
    * Returns the set of data types that must be present in the
    * input to the {@link #execute(Data)} method
    */
   long[] requires();
   
   /**
    * Returns the set of data types that will be included in the output.
    */
   long[] produces();
   
   /**
    * Executes a web service on the given input.
    */
   Data execute(Data input);
}
