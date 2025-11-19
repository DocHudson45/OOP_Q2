package predictive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Implementasi Dictionary menggunakan tree (trie) atas digit 2–9.
 * Tiap node menyimpan:
 *   - children[0..7]  → digit 2..9
 *   - words           → kata yang signature-nya PERSIS sama dengan path ke node itu
 */
public class DictionaryTreeImpl implements Dictionary {

    /** Node untuk tiap digit di tree. */
    private static class Node {
        Node[] children = new Node[8]; // index 0 = '2', 1 = '3', ..., 7 = '9'
        Set<String> words = new HashSet<>();
    }

    private final Node root = new Node();

    public DictionaryTreeImpl() {
        File dictFile = new File("words");

        try (Scanner scanner = new Scanner(dictFile)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (!word.matches("[a-z]+")) {
                    continue;
                }

                String sig = PredictivePrototype.wordToSignature(word);
                insert(sig, word);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file 'words' not found.");
            e.printStackTrace();
        }
    }

    /** Masukkan satu kata ke dalam tree berdasarkan signature-nya. */
    private void insert(String signature, String word) {
        Node current = root;
        for (int i = 0; i < signature.length(); i++) {
            char c = signature.charAt(i);
            if (c < '2' || c > '9') {
                return; // signature tidak valid, abaikan
            }
            int index = c - '2';
            if (current.children[index] == null) {
                current.children[index] = new Node();
            }
            current = current.children[index];
        }
        current.words.add(word);
    }

    /** Helper: jalan di tree mengikuti signature, kembalikan node terakhir. */
    private Node getNode(String signature) {
        Node current = root;
        for (int i = 0; i < signature.length(); i++) {
            char c = signature.charAt(i);
            if (c < '2' || c > '9') {
                return null;
            }
            int index = c - '2';
            current = current.children[index];
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    /** Exact match: ambil kata-kata di node untuk signature ini. */
    @Override
    public Set<String> signatureToWords(String signature) {
        Node node = getNode(signature);
        if (node == null) {
            return new HashSet<>();
        }
        return new HashSet<>(node.words);
    }

    /** Prefix match: kumpulkan semua kata di subtree mulai dari prefix. */
    @Override
    public Set<String> signatureToWordsPrefix(String prefixSignature) {
        Set<String> result = new HashSet<>();
        Node node = getNode(prefixSignature);
        if (node == null) {
            return result;
        }
        collectWords(node, result);
        return result;
    }

    /** DFS kumpulkan semua kata di bawah node ini. */
    private void collectWords(Node node, Set<String> result) {
        result.addAll(node.words);
        for (Node child : node.children) {
            if (child != null) {
                collectWords(child, result);
            }
        }
    }
}
