package org.lappsgrid.discriminator;

/**
 * Static final definitions of the most commonly used discriminator types.
 *
 * @author Keith Suderman
 */
public final class Types
{
    public static final long ERROR = get("error");
    public static final long OK = get("ok");
    public static final long META = get("meta");
    public static final long TEXT = get("text");
    public static final long XML = get("xml");
    public static final long INDEX = get("index");
    public static final long QUERY = get("query");
    public static final long STRING_LIST = get("string-list");

    public static final long LIST = get("list");
    public static final long GET = get("get");
    public static final long REGEX = get("regex");
    public static final long COMPOSITE = get("composite");

    public static final long DOCUMENT = get("document");
    public static final long GATE = get("gate");
    public static final long UIMA = get("uima");
    public static final long STANFORD = get("stanford");
    public static final long OPENNLP = get("opennlp");
    public static final long PTB = get("ptb");
    public static final long GRAF = get("graf");
    /*
    public static final long GATE_DOCUMENT = get("gate-document");
    public static final long GATE_SENTENCE_ANNOTAION = get("gate-sentence");
    public static final long GATE_TOKEN_ANNOTATION = get("gate-token");
    public static final long GATE_POS_ANNOTATION = get("gate-pos");
    public static final long GRAF_RESOURCE_HEADER = get("graf-res-hdr");
    public static final long GRAF_DOCUMENT_HEADER = get("graf-doc-hdr");
    public static final long GRAF_STANDOFF_XML = get("graf-standoff");
    public static final long QUERY_LUCENE = get("lucene");
    public static final long QUERY_JQUERY = get("jquery");
    public static final long QUERY_REGEX = get("regex");
    */
    public static final long TOKEN = get("token");
    public static final long SENTENCE = get("sentence");
    public static final long POS = get("pos");
    public static final long NAMED_ENTITES = get("ne");
    public static final long PERSON = get("person");
    public static final long DATE = get("date");
    public static final long LOCATION = get("location");
    public static final long ORGANIZATION = get("organization");


    // Don't allow instances of this class to be created.
    private Types()
    {
    }

    private static long get(String name)
    {
        return DiscriminatorRegistry.get(name);
    }
}
