package predictive;

public class WordSig implements Comparable<WordSig> {

    private final String word;
    private final String signature;

    public WordSig(String word, String signature) {
        this.word = word.toLowerCase();
        this.signature = signature;
    }

    public WordSig(String word) {
        this(word.toLowerCase(), PredictivePrototype.wordToSignature(word));
    }

    public String getWord() {
        return word;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public int compareTo(WordSig other) {
        int cmp = this.signature.compareTo(other.signature);
        if (cmp != 0) return cmp;
        return this.word.compareTo(other.word);
    }

    @Override
    public String toString() {
        return word + ":" + signature;
    }
}
