package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PredictivePrototype {

    public static String wordToSignature(String word) {
        StringBuffer signature = new StringBuffer();

        word = word.toLowerCase();

        for (char c : word.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                if (c <= 'c') signature.append('2');
                else if (c <= 'f') signature.append('3');
                else if (c <= 'i') signature.append('4');
                else if (c <= 'l') signature.append('5');
                else if (c <= 'o') signature.append('6');
                else if (c <= 's') signature.append('7');
                else if (c <= 'v') signature.append('8');
                else signature.append('9');
            } else {
                signature.append(' ');
            }
        }
        return signature.toString();
    }

    public static Set<String> signatureToWords(String signature) {
        Set<String> result = new HashSet<>();

        try {

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

    private static boolean isValidWord(String word) {
        return word.matches("[a-z]+");
    }
}