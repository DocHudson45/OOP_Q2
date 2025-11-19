package predictive;

import java.util.Set;

public interface Dictionary {
    public Set<String> signatureToWords(String signature);

    default Set<String> signatureToWordsPrefix(String prefixSignature) {
        return signatureToWords(prefixSignature);
    }
}