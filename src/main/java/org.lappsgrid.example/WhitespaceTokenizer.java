package org.lappsgrid.example;

import org.lappsgrid.core.DataFactory;
import org.lappsgrid.api.WebService;

public class WhitespaceTokenizer implements WebService {

    public WhitespaceTokenizer(){
    }

    @Override
    public String execute(String data) {
        long discriminator = data.getDiscriminator();
        if (discriminator == Types.ERROR)
        {
            return data;
        }
        else  if (discriminator == Types.TEXT) {
            String text = data.getPayload();
            Container container = new Container(false);
            container.setText(text);
            container.setLanguage("en");
            ProcessingStep processingStep = container.newStep();
            processingStep.addContains("Token", this.getClass().getName() + ":" + VERSION, "annotation:tokenizer");

            int start = 0;
            int end = 0;
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                // is digital or letter
                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                    end ++;
                    if (end == text.length()) {
                        Annotation ann = processingStep.newAnnotation("Token", start, end);
                        ann.addFeature("string", text.substring(start, end));
                    }
                } else {
                    if (end > start) {
                        Annotation ann = processingStep.newAnnotation("Token", start, end);
                        ann.addFeature("string", text.substring(start, end));
                    }
                    start = i + 1;
                    end = start;
                    if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f') {
                        // white space.
                    } else {  // punctuation
                        Annotation ann = processingStep.newAnnotation("Token", i, i + 1);
                        ann.addFeature("string", text.substring(i, i + 1));
                    }
                }
            }
            return DataFactory.json(container.toString());

        } else  if (discriminator == Types.JSON) {
            String textjson = data.getPayload();


            Container container = new Container(textjson);
            container.setLanguage("en");
            ProcessingStep processingStep = container.newStep();
            processingStep.addContains("Token", this.getClass().getName() + ":" + VERSION, "annotation:tokenizer");
            String text = container.getText();

            int start = 0;
            int end = 0;
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                // is digital or letter
                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                    end ++;
                    if (end == text.length()) {
                        Annotation ann = processingStep.newAnnotation("Token", start, end);
                        ann.addFeature("string", text.substring(start, end));
                    }
                } else {
                    if (end > start) {
                        Annotation ann = processingStep.newAnnotation("Token", start, end);
                        ann.addFeature("string", text.substring(start, end));
                    }
                    start = i + 1;
                    end = start;
                    if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f') {
                        // white space.
                    } else {  // punctuation
                        Annotation ann = processingStep.newAnnotation("Token", i, i + 1);
                        ann.addFeature("string", text.substring(i, i + 1));
                    }
                }
            }
            return DataFactory.json(container.toString());
        } else {
            String name = DiscriminatorRegistry.get(discriminator);
            String message = "Invalid input type. Expected Text but found " + name;
            return DataFactory.error(message);
        }
    }

    public Data configure(Data config) {
        return null;
    }


}