package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DictionaryListImpl implements Dictionary {

    private final List<WordSig> entries = new ArrayList<>();

    public DictionaryListImpl() {
        File dictFile = new File("words");

        try (Scanner sc = new Scanner(dictFile)) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine().trim().toLowerCase();
                if (word.matches("[a-z]+")) {
                    entries.add(new WordSig(word));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file 'words' not found.");
        }

        Collections.sort(entries);
    }

    @Override
    public Set<String> signatureToWords(String signature) {
        Set<String> result = new HashSet<>();

        int left = 0, right = entries.size() - 1;
        int firstIdx = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = entries.get(mid).getSignature().compareTo(signature);

            if (cmp == 0) {
                firstIdx = mid;
                right = mid - 1;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (firstIdx == -1) return result;

        for (int i = firstIdx; i < entries.size(); i++) {
            WordSig ws = entries.get(i);
            if (!ws.getSignature().equals(signature)) break;
            result.add(ws.getWord());
        }

        return result;
    }
}
