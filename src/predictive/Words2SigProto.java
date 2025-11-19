package predictive;

public class Words2SigProto {
    public static void main(String[] args) {
        for (String word : args) {
            String signature = PredictivePrototype.wordToSignature(word);
            System.out.print(signature + " ");
        }
        System.out.println();
    }
}