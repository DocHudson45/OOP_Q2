package predictive;

public class Sigs2WordsMap {

    public static void main(String[] args) {
        Dictionary dict = new DictionaryMapImpl();

        long start = System.nanoTime();

        for (String sig : args) {
            System.out.print(sig + ": ");
            System.out.println(dict.signatureToWords(sig));
        }

        long end = System.nanoTime();
        System.out.println("Elapsed (MapImpl) = " + (end - start) / 1_000_000.0 + " ms");
    }
}
