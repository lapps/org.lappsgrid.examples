/*
 * Copyright 2014 The Language Application Grid
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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.lappsgrid.api.WebService;

import static org.lappsgrid.discriminator.Discriminators.Uri;

import org.lappsgrid.metadata.IOSpecification;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.DataContainer;
import org.lappsgrid.serialization.Serializer;
import org.lappsgrid.serialization.lif.Annotation;
import org.lappsgrid.serialization.lif.Container;
import org.lappsgrid.serialization.lif.View;
import org.lappsgrid.vocabulary.Features;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestWhitespaceTokenizer
{

	protected WebService service;

	@Before
	public void setUp() throws IOException
	{
		service = new WhitespaceTokenizer();
	}

	@After
	public void tearDown()
	{
		service = null;
	}

	@Test
	public void testMetadata()
	{
		String json = service.getMetadata();
		assertNotNull("service.getMetadata() returned null", json);

		Data data = Serializer.parse(json, Data.class);
		assertNotNull("Unable to parse metadata json.", data);
		assertNotSame(data.getPayload().toString(), Uri.ERROR, data.getDiscriminator());

		ServiceMetadata metadata = new ServiceMetadata((Map) data.getPayload());

		assertEquals("Vendor is not correct", "http://www.lappsgrid.org", metadata.getVendor());
		assertEquals("Name is not correct", WhitespaceTokenizer.class.getName(), metadata.getName());
		assertEquals("Version is not correct.","1.0.0-SNAPSHOT" , metadata.getVersion());
		assertEquals("License is not correct", Uri.APACHE2, metadata.getLicense());

		IOSpecification produces = metadata.getProduces();
		assertEquals("Produces encoding is not correct", "UTF-8", produces.getEncoding());
		assertEquals("Too many annotation types produced", 1, produces.getAnnotations().size());
		assertEquals("Tokens not produced", Uri.TOKEN, produces.getAnnotations().get(0));
		assertEquals("Too many output formats", 1, produces.getFormat().size());
		assertEquals("LIF not produces", Uri.LAPPS, produces.getFormat().get(0));

		IOSpecification requires = metadata.getRequires();
		assertEquals("Requires encoding is not correct", "UTF-8", requires.getEncoding());
		List<String> list = requires.getFormat();
		assertTrue("LIF format not accepted.", list.contains(Uri.LAPPS));
		assertTrue("Text not accepted", list.contains(Uri.TEXT));
		list = requires.getAnnotations();
		assertEquals("Required annotations should be empty", 0, list.size());
	}

	@Test
	public void testLeadingSpaceAsText()
	{
		final String text = "   abc def";
		Container container = execute(text);
		assertEquals("Text not set correctly", text, container.getText());
		List<View> views = container.getViews();
		if (views.size() != 1)
		{
			fail(String.format("Expected 1 view. Found: %d", views.size()));
		}
		View view = views.get(0);
		assertTrue("View does not contain tokens", view.contains(Uri.TOKEN));
		List<Annotation> annotations = view.getAnnotations();
		if (annotations.size() != 2)
		{
			for (Annotation a : annotations)
			{
				System.out.println(a.getId() + " " + a.getAtType() + " " + a.getFeature(Features.Token.WORD));
			}
			fail(String.format("Expected 2 annotations. Found %d", annotations.size()));
		}
		Annotation token = annotations.get(0);
		assertEquals("Token 1: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 1: wrong start", 3L, token.getStart().longValue());
		assertEquals("Token 1: wrong end", 6L, token.getEnd().longValue());
		assertEquals("Token 1: wrong word", "abc", token.getFeature(Features.Token.WORD));

		token = annotations.get(1);
		assertEquals("Token 2: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 2: wrong start", 7L, token.getStart().longValue());
		assertEquals("Token 2: wrong end", 10L, token.getEnd().longValue());
		assertEquals("Token 2: wrong word", "def", token.getFeature(Features.Token.WORD));
	}

	@Test
	public void testLeadingSpaceAsContainer()
	{
		final String text = "   abc def";

		Container container = new Container();
		container.setText(text);
		container = execute(container);
		assertEquals("Text not set correctly", text, container.getText());
		List<View> views = container.getViews();
		if (views.size() != 1)
		{
			fail(String.format("Expected 1 view. Found: %d", views.size()));
		}
		View view = views.get(0);
		assertTrue("View does not contain tokens", view.contains(Uri.TOKEN));
		List<Annotation> annotations = view.getAnnotations();
		if (annotations.size() != 2)
		{
			fail(String.format("Expected 2 annotations. Found %d", annotations.size()));
		}
		Annotation token = annotations.get(0);
		assertEquals("Token 1: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 1: wrong start", 3L, token.getStart().longValue());
		assertEquals("Token 1: wrong end", 6L, token.getEnd().longValue());
		assertEquals("Token 1: wrong word", "abc", token.getFeature(Features.Token.WORD));

		token = annotations.get(1);
		assertEquals("Token 2: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 2: wrong start", 7L, token.getStart().longValue());
		assertEquals("Token 2: wrong end", 10L, token.getEnd().longValue());
		assertEquals("Token 2: wrong word", "def", token.getFeature(Features.Token.WORD));
	}

	@Test
	public void testNoLeadingSpace()
	{
		String text = "abc    def ";
		Container container = execute(text);

		assertEquals("Text not set correctly", text, container.getText());
		List<View> views = container.getViews();
		if (views.size() != 1)
		{
			fail(String.format("Expected 1 view. Found: %d", views.size()));
		}
		View view = views.get(0);
		assertTrue("View does not contain tokens", view.contains(Uri.TOKEN));
		List<Annotation> annotations = view.getAnnotations();
		if (annotations.size() != 2)
		{
			fail(String.format("Expected 2 annotations. Found %d", annotations.size()));
		}
		Annotation token = annotations.get(0);
		assertEquals("Token 1: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 1: wrong start", 0L, token.getStart().longValue());
		assertEquals("Token 1: wrong end", 3L, token.getEnd().longValue());
		assertEquals("Token 1: wrong word", "abc", token.getFeature(Features.Token.WORD));

		token = annotations.get(1);
		assertEquals("Token 2: wrong @type", Uri.TOKEN, token.getAtType());
		assertEquals("Token 2: wrong start", 7L, token.getStart().longValue());
		assertEquals("Token 2: wrong end", 10L, token.getEnd().longValue());
		assertEquals("Token 2: wrong word", "def", token.getFeature(Features.Token.WORD));
	}

	protected Container execute(String input)
	{
		return execute(new Data<String>(Uri.TEXT, input));
	}

	protected Container execute(Container container)
	{
		return execute(new DataContainer(container));
	}

	protected Container execute(Data data)
	{
		String json = service.execute(data.asJson());
		assertNotNull("Service returned null", json);
		DataContainer dc = Serializer.parse(json, DataContainer.class);
		assertEquals("Returned format is not LIF", Uri.LAPPS, dc.getDiscriminator());
		return dc.getPayload();
	}
}