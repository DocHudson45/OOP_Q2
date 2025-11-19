package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DictionaryMapImpl implements Dictionary {

    private final Map<String, Set<String>> dictionary = new HashMap<>();

    public DictionaryMapImpl() {
        File dictFile = new File("words");

        try (Scanner scanner = new Scanner(dictFile)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (!word.matches("[a-z]+")) continue;

                String sig = PredictivePrototype.wordToSignature(word);
                dictionary.computeIfAbsent(sig, x -> new HashSet<>()).add(word);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file 'words' not found.");
        }
    }

    @Override
    public Set<String> signatureToWords(String signature) {
        return new HashSet<>(dictionary.getOrDefault(signature, new HashSet<>()));
    }

    @Override
    public Set<String> signatureToWordsPrefix(String prefixSignature) {
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : dictionary.entrySet()) {
            if (entry.getKey().startsWith(prefixSignature)) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }
}
