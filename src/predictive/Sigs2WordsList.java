package predictive;

public class Sigs2WordsList {
    public static void main(String[] args) {
        DictionaryListImpl dict = new DictionaryListImpl();

        for (String sig : args) {
            System.out.print(sig + ": ");
            System.out.println(dict.signatureToWords(sig));
        }
    }
}