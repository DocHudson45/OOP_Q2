package predictive;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class PredictiveGUI extends JFrame {

    private final JTextArea messageArea = new JTextArea(4, 30);
    private final JLabel signatureLabel = new JLabel("Signature: ");
    private final JLabel candidateLabel = new JLabel("Candidate: ");

    private final StringBuilder currentSignature = new StringBuilder();
    private final Dictionary dict = new DictionaryTreeImpl();

    private List<String> currentCandidates = new ArrayList<>();
    private int currentIndex = -1;

    public PredictiveGUI() {
        super("Predictive Text (T9)");

        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(signatureLabel);
        infoPanel.add(candidateLabel);
        topPanel.add(infoPanel, BorderLayout.SOUTH);

        JPanel keypad = new JPanel(new GridLayout(4, 3, 5, 5));
        keypad.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Baris 1
        addKeyButton(keypad, "<html><center>1</center></html>", "1");
        addKeyButton(keypad, "<html><center>2<br>ABC</center></html>", "2");
        addKeyButton(keypad, "<html><center>3<br>DEF</center></html>", "3");

        // Baris 2
        addKeyButton(keypad, "<html><center>4<br>GHI</center></html>", "4");
        addKeyButton(keypad, "<html><center>5<br>JKL</center></html>", "5");
        addKeyButton(keypad, "<html><center>6<br>MNO</center></html>", "6");

        // Baris 3
        addKeyButton(keypad, "<html><center>7<br>PQRS</center></html>", "7");
        addKeyButton(keypad, "<html><center>8<br>TUV</center></html>", "8");
        addKeyButton(keypad, "<html><center>9<br>WXYZ</center></html>", "9");

        // Baris 4
        addKeyButton(keypad, "<html><center>*</center></html>", "*");
        addKeyButton(keypad, "<html><center>0</center></html>", "0");
        addKeyButton(keypad, "<html><center>#</center></html>", "#");

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(keypad, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // di tengah layar
    }

    private void addKeyButton(JPanel panel, String display, String value) {
        JButton button = new JButton(display);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.putClientProperty("value", value);
        button.addActionListener(e -> {
            String val = (String) ((JButton) e.getSource()).getClientProperty("value");
            handleKeyPress(val);
        });
        panel.add(button);
    }

     private void handleKeyPress(String key) {
        char c = key.charAt(0);

        if (c >= '2' && c <= '9') {
            currentSignature.append(c);
            updateCandidates();
        } else if (c == '1') {
            if (currentSignature.length() > 0) {
                currentSignature.setLength(currentSignature.length() - 1);
                updateCandidates();
            }
        } else if (c == '*') {
            showNextCandidate();
        } else if (c == '#') {
            acceptCurrentWord();
        } else if (c == '0') {
            messageArea.append(" ");
        }

        signatureLabel.setText("Signature: " + currentSignature);
    }

    private void updateCandidates() {
        if (currentSignature.length() == 0) {
            currentCandidates = new ArrayList<>();
            currentIndex = -1;
            candidateLabel.setText("Candidate: ");
            return;
        }

        Set<String> set = dict.signatureToWords(currentSignature.toString());
        currentCandidates = new ArrayList<>(set);

        if (currentCandidates.isEmpty()) {
            currentIndex = -1;
            candidateLabel.setText("Candidate: (no match)");
        } else {
            currentIndex = 0;
            candidateLabel.setText("Candidate: " + currentCandidates.get(currentIndex));
        }
    }

    private void showNextCandidate() {
        if (currentCandidates == null || currentCandidates.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex + 1) % currentCandidates.size();
        candidateLabel.setText("Candidate: " + currentCandidates.get(currentIndex));
    }

    private void acceptCurrentWord() {
        if (currentIndex >= 0 && currentIndex < currentCandidates.size()) {
            String word = currentCandidates.get(currentIndex);
            messageArea.append(word + " ");
        } else if (currentSignature.length() > 0) {
            messageArea.append(currentSignature.toString() + " ");
        }

        currentSignature.setLength(0);
        currentCandidates.clear();
        currentIndex = -1;
        signatureLabel.setText("Signature: ");
        candidateLabel.setText("Candidate: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PredictiveGUI().setVisible(true));
    }
}
