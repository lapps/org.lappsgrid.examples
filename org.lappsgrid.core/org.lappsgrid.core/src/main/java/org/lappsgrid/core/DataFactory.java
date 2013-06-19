package org.lappsgrid.core;

import org.lappsgrid.api.Data;
import org.lappsgrid.discriminator.Types;

import java.util.List;

/**
 * A factory class for creating {@link org.lappsgrid.api.Data Data} objects
 * with the most commonly used types.
 *
 * @author Keith Suderman
 */
public class DataFactory
{

    // Prevent instances of the DataFactory class from being created.
    protected DataFactory()
    {
    }

    public static Data ok()
    {
        return new Data(Types.OK);
    }

    public static Data error(String errorMessage)
    {
        return new Data(Types.ERROR, errorMessage);
    }

    public static Data query(String queryString)
    {
        return new Data(Types.QUERY, queryString);
    }

//   public static Data jquery(String queryString) {
//      return new Data(Types.QUERY_JQUERY, queryString);
//   }

//   public static Data lucene(String queryString) {
//      return new Data(Types.LUCENE, queryString);
//   }
//
//   public static Data regex(String regex) {
//      return new Data(Types.QUERY_REGEX, regex);
//   }

    public static Data get(String id)
    {
        return new Data(Types.GET, id);
    }

    public static Data list()
    {
        return new Data(Types.LIST);
    }

    public static Data index(String[] items)
    {
        if (items.length == 0)
        {
            return new Data(Types.INDEX, "");
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(items[0]);
        for (int i = 1; i < items.length; ++i)
        {
            buffer.append(" ");
            buffer.append(items[i]);
        }
        return new Data(Types.INDEX, buffer.toString());
    }

    public static Data index(String items)
    {
        return new Data(Types.INDEX, items);
    }

    public static Data stringList(String[] items)
    {
        StringBuilder buffer = new StringBuilder(4096);
        for (String item : items)
        {
            buffer.append(item);
            buffer.append('\n');
        }
        return new Data(Types.STRING_LIST, buffer.toString());
    }

    public static Data stringList(List<String> list)
    {
        StringBuilder buffer = new StringBuilder(4096);
        for (String item : list)
        {
            buffer.append(item);
            buffer.append('\n');
        }
        return new Data(Types.STRING_LIST, buffer.toString());
    }

    public static Data text(String text)
    {
        return new Data(Types.TEXT, text);
    }

    public static Data document(String document)
    {
        return new Data(Types.DOCUMENT, document);
    }

    public static Data xml(String xml)
    {
        return new Data(Types.XML, xml);
    }

    public static Data gateDocument(String document)
    {
        return new Data(Types.GATE, document);
    }
}

