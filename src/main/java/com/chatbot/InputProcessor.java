import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations.*;

import java.util.List;
import java.util.Properties;

public class InputProcessor {
    public static void main(String[] args) {
        // Set up the Stanford CoreNLP pipeline with tokenization, sentence splitting, POS tagging, lemmatization, and named entity recognition
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        // Create the pipeline using the properties
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Sample text to process
        String text = "The cats are running in the park, and the dog runs too.";

        // Create an annotation object with the sample text
        Annotation document = new Annotation(text);

        // Annotate the document
        pipeline.annotate(document);

        // Extract sentences from the annotated document
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        // Iterate through each sentence and process tokens
        for (CoreMap sentence : sentences) {
            System.out.println("Sentence: " + sentence.toString());

            // Extract tokens (words) and print them
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (CoreLabel token : tokens) {
                // Print the original token and its lemma
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.println("Token: " + word + " | Lemma: " + lemma + " | POS: " + pos);
            }
        }
    }
}
