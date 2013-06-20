package org.lappsgrid.discriminator.test;

//import org.anc.lapps.data.discriminator.DiscriminatorRegistry;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.lappsgrid.discriminator.DiscriminatorRegistry;

public class RegistryTest
{

    @Test
    public void testError()
    {
        long id = DiscriminatorRegistry.get("error");
        assertTrue(id == 0);
    }

    @Test
    public void testInvalidDiscriminatorName()
    {
        assertTrue(DiscriminatorRegistry.get("FooBar") < 0);
    }

    @Test
    public void testIsa()
    {
        isa("uima", "xml");
        isa("gate", "document");
        isa("sentence", "annotation");
        isa("token", "chunk");
    }

    @Ignore
    public void testGraf()
    {
        String[] grafMetaXml = {"graf", "meta-xml", "meta", "xml"};
        isa("graf-standoff", new String[]{"graf", "xml"});
        isa("graf-res-hdr", grafMetaXml);
        isa("graf-doc-hdr", grafMetaXml);
        isa("graf-txt", new String[]{"graf", "text"});
    }

    @Ignore
    public void testGate()
    {
        isa("gate-document", new String[]{"gate", "document"});
        isa("gate-sentence", new String[]{"gate", "sentence", "annotation"});
        isa("gate-token", new String[]{"gate", "token", "annotation"});
        isa("gate-pos", new String[]{"gate", "pos", "annotation"});
    }

    @Ignore
    public void testSpecificValue()
    {
        check("ok", 1L);
        check("meta", 2L);
        check("document", 3L);
        check("graf", 5L * 1024);
        check("uima", 6L * 1024);
        check("gate", 7L * 1024);
    }

    @Test
    public void testRoundTrip()
    {
        for (long type : DiscriminatorRegistry.types())
        {
            String name = get(type);
            long value = get(name);
            assertTrue("Round trip failed for " + type + ": " + name, type == value);
        }
    }

    protected void isa(String child, String[] parents)
    {
        for (String parent : parents)
        {
            assertTrue(child + " is not a " + parent, DiscriminatorRegistry.isa(child, parent));
        }
    }

    protected void check(String name, long value)
    {
        assertTrue("Invalid value for " + name, get(name) == value);
    }

    protected void isa(String child, String parent)
    {
        assertTrue(child + " is not a " + parent, DiscriminatorRegistry.isa(child, parent));
    }

    protected long get(String name)
    {
        return DiscriminatorRegistry.get(name);
    }

    protected String get(long value)
    {
        return DiscriminatorRegistry.get(value);
    }

    public RegistryTest()
    {
    }


}
