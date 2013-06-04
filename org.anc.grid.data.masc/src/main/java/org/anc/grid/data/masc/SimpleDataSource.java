package org.anc.grid.data.masc;

import java.util.Set;

import org.anc.lapps.data.api.Data;
import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

public class SimpleDataSource implements org.anc.lapps.data.api.SimpleDataSource 
{
	public static final long GET = DiscriminatorRegistry.get("input-param-get");
	public static final long LIST = DiscriminatorRegistry.get("input-param-list");
	public static final long QUERY =  DiscriminatorRegistry.get("input-param-query");
	public static final long ERROR = DiscriminatorRegistry.get("error");
	
	protected DataSource source;
	
	public SimpleDataSource()
	{
		source = new DataSource();
	}
	
	public SimpleDataSource(Set<String> keys)
	{
		source = new DataSource(keys);
	}
	
	@Override
	public Data query(long type, String queryString) 
	{
		if (type == GET)
		{
			return source.get(queryString);
		}
		else if (type == LIST)
		{
			return source.list();
		}
		else if (type == QUERY)
		{
			return source.query(queryString);
		}
		return new Data(ERROR, "Invalid query type: " + type);
	}

}
