package org.lappsgrid.example;

import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.lappsgrid.api.Data;
import org.lappsgrid.discriminator.Types;
import org.lappsgrid.serialization.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class TestWhitespaceTokenizer {
    Data input = null;
    String target = null;

    @Before
    public void setUp() throws IOException {
        input = new Data(Types.TEXT);
        input.setPayload("Hi, how are you?");
    }

    @After
    public void tearDown(){

    }

    @Test
    public void test(){
        WhitespaceTokenizer wt = new WhitespaceTokenizer();
        Data output = wt.execute(input);
        System.out.println(output.getPayload());
    }
}