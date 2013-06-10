package org.anc.grid.data.masc;

import java.util.Set;

import org.anc.lapps.api.Data;

public class RawDataSource implements org.anc.lapps.api.RawDataSource 
{
	protected SimpleDataSource source;
	
	public RawDataSource()
	{
		source = new SimpleDataSource();
	}
	
	public RawDataSource(Set<String> keys)
	{
		source = new SimpleDataSource(keys);		
	}
	
	@Override
	public Data query(String query) 
	{
		String[] args = query.split("\\s+");
		if (args.length == 1)
		{
			if (args[0].toLowerCase().equals("list"))
			{
				return source.query(SimpleDataSource.LIST, null);
			}
			else 
			{
				// Assume a single parameter is the id value for a
				// GET query.
				return source.query(SimpleDataSource.GET, args[0]);
			}
		}
		else if (args.length == 2)
		{
			String command = args[0].toLowerCase();
			String queryString = args[1];
			if ("get".equals(command))
			{
				return source.query(SimpleDataSource.GET, queryString);
			}
			else if ("query".equals(command))
			{
				return source.query(SimpleDataSource.QUERY, queryString);
			}
			else
			{
				return new Data(SimpleDataSource.ERROR, "Unknown query: " + command);
			}
		}
		return new Data(SimpleDataSource.ERROR, "Invalid query: " + query);
	}

}
