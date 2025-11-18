package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PredictivePrototype {

    // Method 1: Mengubah kata jadi angka (Signature)
    public static String wordToSignature(String word) {
        StringBuffer signature = new StringBuffer();

        // [EXPLANATION FOR PDF QUESTION 1.1]
        // Using StringBuffer is more efficient than using String concatenation (+ operator).
        // String is immutable in Java, so every time we use +, a new String object is created in memory.
        // StringBuffer is mutable, allowing us to modify the sequence of characters without creating new objects,
        // which saves memory and processing time, especially in loops.

        word = word.toLowerCase();

        for (char c : word.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                // Mapping letters to keypad numbers
                if (c <= 'c') signature.append('2');
                else if (c <= 'f') signature.append('3');
                else if (c <= 'i') signature.append('4');
                else if (c <= 'l') signature.append('5');
                else if (c <= 'o') signature.append('6');
                else if (c <= 's') signature.append('7');
                else if (c <= 'v') signature.append('8');
                else signature.append('9');
            } else {
                signature.append(' '); // Replace non-alphabetic chars with space
            }
        }
        return signature.toString();
    }

    // Method 2: Mencari kata dari angka (Inefficient way)
    public static Set<String> signatureToWords(String signature) {
        Set<String> result = new HashSet<>();

        try {
            // [EXPLANATION FOR PDF QUESTION 1.2]
            // This implementation is inefficient because it does not store the dictionary in memory.
            // Every time this method is called, it opens the file from the disk and scans through
            // the entire dictionary line-by-line again. Disk I/O operations are significantly slower
            // than accessing data stored in RAM (memory).

            Scanner scanner = new Scanner(new File("words"));

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();

                if (isValidWord(word)) {
                    if (wordToSignature(word).equals(signature)) {
                        result.add(word);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: Dictionary file 'words' not found.");
        }

        return result;
    }

    // Helper method
    private static boolean isValidWord(String word) {
        return word.matches("[a-z]+");
    }
}