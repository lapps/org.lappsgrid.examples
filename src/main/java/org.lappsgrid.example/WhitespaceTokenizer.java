package org.lappsgrid.example;

import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import static org.lappsgrid.discriminator.Discriminators.Uri;
import org.lappsgrid.serialization.*;
import org.lappsgrid.serialization.lif.*;

import org.lappsgrid.api.WebService;

import java.util.Map;

public class WhitespaceTokenizer implements WebService
{

	public WhitespaceTokenizer()
	{
	}

	@Override
	public String execute(String json)
	{
		Data data = Serializer.parse(json, Data.class);
		if (is(data, Uri.ERROR))
		{
			// Return errors unmodified.
			return json;
		}

		Container container;
		if (is(data, Uri.LAPPS))
		{
			container = new Container((Map) data.getPayload());
		}
		else if (is(data, Uri.TEXT))
		{
			container = new Container();
			container.setText(data.getPayload().toString());
			container.setLanguage("en");
		}
		else
		{
			String message = String.format("Unsupported discriminator type: %s", data.getDiscriminator());
			return DataFactory.error(message);
		}

		data = new DataContainer(container);
		return data.asJson();
	}

	@Override
	public String getMetadata()
	{
		return null;
	}


	protected void tokenize(Container container)
	{

	}
	
	private boolean is(Data<? extends Object> data, String type)
	{
		return type.equals(data.getDiscriminator());
	}
}