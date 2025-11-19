package predictive;
import java.util.Set;

public class Sigs2WordsProto {
    public static void main(String[] args) {
        for (String sig : args) {
            System.out.print(sig + ": ");
            Set<String> matches = PredictivePrototype.signatureToWords(sig);
            System.out.println(matches); 
        }
    }
}
