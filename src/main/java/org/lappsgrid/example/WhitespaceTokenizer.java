package org.lappsgrid.example;

import org.lappsgrid.api.ProcessingService;

import static org.lappsgrid.discriminator.Discriminators.Uri;
import org.lappsgrid.serialization.Data;
import org.lappsgrid.serialization.DataContainer;
import org.lappsgrid.serialization.Serializer;
import org.lappsgrid.serialization.lif.Annotation;
import org.lappsgrid.serialization.lif.Container;
import org.lappsgrid.serialization.lif.View;
import org.lappsgrid.vocabulary.Features;

import java.util.Map;

/**
 * Tutorial step #2. Service Implementation 
 */
public class WhitespaceTokenizer implements ProcessingService
{
    /**
     * The Json String required by getMetadata()
     */
    private String metadata;


    public WhitespaceTokenizer() {
        
        // Metadata will be discussed in step 3
        this.metadata = null;
    }

    @Override
    /**
     * getMetadata simply returns metadata populated in the constructor
     */
    public String getMetadata() {
        return metadata;
    }

    @Override
    public String execute(String input) {
        // Step #1: Parse the input.
        Data data = Serializer.parse(input, Data.class);

        // Step #2: Check the discriminator
        final String discriminator = data.getDiscriminator();
        if (discriminator.equals(Uri.ERROR)) {
            // Return the input unchanged.
            return input;
        }

        // Step #3: Extract the text.
        Container container = null;
        if (discriminator.equals(Uri.TEXT)) {
            container = new Container();
            container.setText(data.getPayload().toString());
        }
        else if (discriminator.equals(Uri.LAPPS)) {
            container = new Container((Map) data.getPayload());
        }
        else {
            // This is a format we don't accept.
            String message = String.format("Unsupported discriminator type: %s", discriminator);
            return new Data<String>(Uri.ERROR, message).asJson();
        }

        // Step #4: Create a new View
        View view = container.newView();

        // Step #5: Tokenize the text and add annotations.
        String text = container.getText();
        String[] words = text.trim().split("\\s+");
        int id = -1;
        int start = 0;
        for (String word : words) {
            start = text.indexOf(word, start);
            if (start < 0) {
                return new Data<String>(Uri.ERROR, "Unable to match word: " + word).asJson();
            }
            int end = start + word.length();
            Annotation a = view.newAnnotation("tok" + (++id), Uri.TOKEN, start, end);
            a.addFeature(Features.Token.WORD, word);
            start = end;
        }

        // Step #6: Update the view's metadata. Each view contains metadata about the
        // annotations it contains, in particular the name of the tool that produced the
        // annotations.
        view.addContains(Uri.TOKEN, this.getClass().getName(), "whitespace");

        // Step #7: Create a DataContainer with the result.
        data = new DataContainer(container);

        // Step #8: Serialize the data object and return the JSON.
        return data.asPrettyJson();
    }
}
