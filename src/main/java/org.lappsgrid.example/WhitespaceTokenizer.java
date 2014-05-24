package org.lappsgrid.example;

import org.anc.lapps.serialization.Annotation;
import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;
import org.lappsgrid.serialization.json.JSONObject;
import org.lappsgrid.serialization.json.JsonTaggerSerialization;
import org.lappsgrid.serialization.json.JsonTokenizerSerialization;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;

import java.util.*;

public class WhitespaceTokenizer implements WebService {
    public static final  String VERSION = "0.0.1-SNAPSHOT";

    public WhitespaceTokenizer(){
    }

    @Override
    public long[] requires() {
        return new long[]{};
    }

    @Override
    public long[] produces() {
        return new long[]{};
    }

    @Override
    public Data execute(Data data) {
        {
            long discriminator = data.getDiscriminator();
            if (discriminator == Types.ERROR)
            {
                return data;
            }
            else  if (discriminator == Types.TEXT) {
                String text = data.getPayload();
                JsonTokenizerSerialization json = new JsonTokenizerSerialization();
                json.setTextValue(text);
                json.setProducer(this.getClass().getName() + ":" + VERSION);
                json.setType("annotation:tokenizer");

                int start = 0;
                int end = 0;
                for (int i = 0; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    // is digital or letter
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                        end ++;
                        if (end == text.length()) {
                            JSONObject ann = json.newAnnotation();
                            json.setEnd(ann,end);
                            json.setStart(ann, start);
                            json.setWord(ann, text.substring(start, end));
                        }
                    } else {
                        if (end > start) {
                            JSONObject ann = json.newAnnotation();
                            json.setEnd(ann,end);
                            json.setStart(ann, start);
                            json.setWord(ann, text.substring(start, end));
                        }
                        start = i + 1;
                        end = start;
                        if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f') {
                            // white space.
                        } else {  // punctuation
                            JSONObject ann = json.newAnnotation();
                            json.setStart(ann, i);
                            json.setEnd(ann, i+1);
                            json.setWord(ann, text.substring(i, i + 1));
                        }
                    }
                }
                return DataFactory.json(json.toString());

            } else  if (discriminator == Types.JSON) {
                String textjson = data.getPayload();
                JsonTaggerSerialization json = new JsonTaggerSerialization(textjson);
                json.setProducer(this.getClass().getName() + ":" + VERSION);
                json.setType("annotation:tokenizer");
                String text = json.getTextValue();
                int start = 0;
                int end = 0;
                for (int i = 0; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    // is digital or letter
                    if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                        end ++;
                        if (end == text.length()) {
                            JSONObject ann = json.newAnnotation();
                            json.setEnd(ann,end);
                            json.setStart(ann, start);
                            json.setWord(ann, text.substring(start, end));
                        }
                    } else {
                        if (end > start) {
                            JSONObject ann = json.newAnnotation();
                            json.setEnd(ann,end);
                            json.setStart(ann, start);
                            json.setWord(ann, text.substring(start, end));
                        }
                        start = i + 1;
                        end = start;
                        if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f') {
                            // white space.
                        } else {  // punctuation
                            JSONObject ann = json.newAnnotation();
                            json.setStart(ann, i);
                            json.setEnd(ann, i+1);
                            json.setWord(ann, text.substring(i, i + 1));
                        }
                    }
                }
                return DataFactory.json(json.toString());
            } else {
                String name = DiscriminatorRegistry.get(discriminator);
                String message = "Invalid input type. Expected Text but found " + name;
                return DataFactory.error(message);
            }
        }
    }

}