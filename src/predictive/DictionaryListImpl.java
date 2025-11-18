package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DictionaryListImpl implements Dictionary {
    private List<WordSig> dictionary;

    // Constructor: Baca file dan isi ke memori
    public DictionaryListImpl() {
        dictionary = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("words")); // Pastikan file words ada di root folder
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (isValidWord(word)) {
                    // Pakai method dari Part 1 buat generate signature
                    String sig = PredictivePrototype.wordToSignature(word);
                    dictionary.add(new WordSig(word, sig));
                }
            }
            scanner.close();

            // Sort biar bisa di-Binary Search [cite: 166]
            Collections.sort(dictionary);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Helper method
    private static boolean isValidWord(String word) {
        return word.matches("[a-z]+");
    }

    @Override
    public Set<String> signatureToWords(String signature) {
        Set<String> result = new HashSet<>();

        // Karena kita cuma punya signature target, kita bikin dummy object buat dicari
        WordSig target = new WordSig("", signature);

        // Binary Search [cite: 166]
        int index = Collections.binarySearch(dictionary, target);

        // Jika ketemu (index >= 0)
        if (index >= 0) {
            // Ambil kata di index yang ketemu
            result.add(dictionary.get(index).getWords());

            // TAPI INGAT! Binary search cuma nemu SATU posisi.
            // Padahal mungkin ada banyak kata dengan signature sama di atas atau bawahnya.
            // Kita harus cek tetangga atas dan bawahnya.

            // Cek ke atas (sebelum index)
            int i = index - 1;
            while (i >= 0 && dictionary.get(i).getSignature().equals(signature)) {
                result.add(dictionary.get(i).getWords());
                i--;
            }

            // Cek ke bawah (setelah index)
            int j = index + 1;
            while (j < dictionary.size() && dictionary.get(j).getSignature().equals(signature)) {
                result.add(dictionary.get(j).getWords());
                j++;
            }
        }

        return result;
    }
}