/*-
 * Copyright 2015 The Language Application Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.lappsgrid.example;

import org.lappsgrid.core.DataFactory;
import static org.lappsgrid.discriminator.Discriminators.Uri;

import org.lappsgrid.metadata.IOSpecification;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.*;
import org.lappsgrid.serialization.lif.*;

import org.lappsgrid.api.WebService;
import org.lappsgrid.vocabulary.Features;

import java.util.Map;

/**
 * A Tokenizer that simply splits the input on whitespace.
 */
public class WhitespaceTokenizer implements WebService
{
	/**
	 * The Json String required by getMetadata()
	 */
	private String metadata;


	public WhitespaceTokenizer()
	{
		// Create and populate the metadata object
		ServiceMetadata metadata = new ServiceMetadata();
		metadata.setName(this.getClass().getName());
		metadata.setDescription("Whitespace tokenizer");
		metadata.setVersion(Version.getVersion());
		metadata.setLicense(Uri.APACHE2);
		metadata.setVendor("http://www.anc.org");

		IOSpecification requires = new IOSpecification();
		requires.addFormat(Uri.TEXT);
		requires.addFormat(Uri.LAPPS);
		requires.addFormat(Uri.LIF);
		requires.setEncoding("UTF-8");
		metadata.setRequires(requires);

		IOSpecification produces = new IOSpecification();
		produces.addFormat(Uri.LAPPS);
		produces.setEncoding("UTF-8");
		produces.addAnnotation(Uri.TOKEN);
		metadata.setProduces(produces);

		// Serialize the metadata to a string and save for the
		// getMetadata() method.
		Data<ServiceMetadata> data = new Data<ServiceMetadata>(Uri.META, metadata);
		this.metadata = data.asPrettyJson();
	}

	@Override
	public String execute(String json)
	{
		Data data = Serializer.parse(json, Data.class);
		if (checkType(data, Uri.ERROR))
		{
			// Return errors unmodified.
			return json;
		}

		Container container;
		if (checkType(data, Uri.LAPPS))
		{
			container = new Container((Map) data.getPayload());
		}
		else if (checkType(data, Uri.TEXT))
		{
			container = new Container();
			container.setText(data.getPayload().toString());
		}
		else
		{
			String message = String.format("Unsupported discriminator type: %s", data.getDiscriminator());
			return DataFactory.error(message);
		}
		tokenize(container);
		data = new DataContainer(container);
		return data.asJson();
	}

	@Override
	public String getMetadata()
	{
		return metadata;
	}


	/**
	 * Splits the container's text into whitespace delimited tokens.
	 *
	 * Token annotations will be added to a new View in the container.
	 */
	protected void tokenize(Container container)
	{
		View view = container.newView();
		view.addContains(Uri.TOKEN, this.getClass().getName(), "whitespace");

		char[] buffer = container.getText().toCharArray();
		int start = skipWhiteSpace(buffer, 0);
		int id = -1;
		while (start < buffer.length)
		{
			int end = skipCharacters(buffer, start);
			Annotation a = view.newAnnotation("tok" + (++id), Uri.TOKEN, start, end);
			a.addFeature(Features.Token.WORD, new String(buffer, start, end-start));
			start = skipWhiteSpace(buffer, end);
		}
	}

	/**
	 * Skip over all whitespace characters in the buffer starting at the
	 * specified index.
	 *
	 * @return The index of the first non-whitespace character in the buffer,
	 *         or buffer.length if the buffer only contains whitespace after
	 *         the index.
	 */
	protected int skipWhiteSpace(char[] buffer, int index)
	{
		while (index < buffer.length && Character.isWhitespace(buffer[index]))
		{
			++index;
		}
		return index;
	}

	/**
	 * Returns the position of the first whitespace character in the buffer
	 * after the specified index.  Returns buffer.length if the buffer does
	 * not contain any whitespace after the specified index.
	 */
	protected int skipCharacters(char[] buffer, int index)
	{
		while (index < buffer.length && !Character.isWhitespace(buffer[index]))
		{
			++index;
		}
		return index;
	}

	/**
	 * Returns true if the Data object's discriminator is equal to the
	 * specified type.
	 */
	private boolean checkType(Data<? extends Object> data, String type)
	{
		return type.equals(data.getDiscriminator());
	}
}
