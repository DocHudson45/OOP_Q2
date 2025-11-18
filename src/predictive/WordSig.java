package predictive;

public class WordSig implements Comparable<WordSig> {
    private String words;
    private String signature;

    public WordSig(String words, String signature) {
        this.words = words;
        this.signature = signature;
    }

    public String getWords() {
        return words;
    }

    public String getSignature() {
        return signature;
    }

    // Method wajib karena implement Comparable
    // Kita compare berdasarkan SIGNATURE-nya, bukan katanya.
    @Override
    public int compareTo(WordSig ws) {
        return this.signature.compareTo(ws.signature);
    }
}