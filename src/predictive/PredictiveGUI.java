package predictive;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PredictiveGUI extends JFrame {

    private final StringBuilder currentSignature = new StringBuilder();
    private final Dictionary dict;
    private List<String> currentCandidates = new ArrayList<>();
    private int currentIndex = -1;

    private final JTextArea messageArea = new JTextArea(3, 20);
    private final JLabel signatureLabel = new JLabel(" ");
    private final JLabel candidateLabel = new JLabel("Start typing...");

    public PredictiveGUI() {
        super("T9 Predictive Text");
        
        dict = new DictionaryTreeImpl(); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(mainPanel);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setOpaque(false);
        displayPanel.setBorder(new EmptyBorder(40, 25, 20, 25));

        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 28));
        messageArea.setForeground(new Color(50, 50, 50));
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setHighlighter(null);
        
        candidateLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        candidateLabel.setForeground(new Color(0, 0, 0));
        candidateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        signatureLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        signatureLabel.setForeground(new Color(120, 120, 120));
        signatureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        displayPanel.add(messageArea);
        displayPanel.add(Box.createVerticalStrut(20));
        displayPanel.add(signatureLabel);
        displayPanel.add(candidateLabel);

        mainPanel.add(displayPanel, BorderLayout.NORTH);

        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 15, 15));
        keypadPanel.setOpaque(false);
        keypadPanel.setBorder(new EmptyBorder(20, 25, 40, 25));

        Color glassWhite = new Color(255, 255, 255, 180);
        Color glassOrange = new Color(255, 160, 40, 230);
        Color textDark = new Color(40, 40, 40);
        Color textWhite = Color.WHITE;

        addKeyButton(keypadPanel, "1", "Del", glassOrange, textWhite);
        addKeyButton(keypadPanel, "2", "abc", glassWhite, textDark);
        addKeyButton(keypadPanel, "3", "def", glassWhite, textDark);

        addKeyButton(keypadPanel, "4", "ghi", glassWhite, textDark);
        addKeyButton(keypadPanel, "5", "jkl", glassWhite, textDark);
        addKeyButton(keypadPanel, "6", "mno", glassWhite, textDark);

        addKeyButton(keypadPanel, "7", "pqrs", glassWhite, textDark);
        addKeyButton(keypadPanel, "8", "tuv", glassWhite, textDark);
        addKeyButton(keypadPanel, "9", "wxyz", glassWhite, textDark);

        addKeyButton(keypadPanel, "*", "Next", glassOrange, textWhite);
        addKeyButton(keypadPanel, "0", "_", glassWhite, textDark);
        addKeyButton(keypadPanel, "#", "Send", glassOrange, textWhite);

        mainPanel.add(keypadPanel, BorderLayout.CENTER);
    }

    private void addKeyButton(JPanel panel, String bigText, String smallText, Color bg, Color fg) {
        GlassButton btn = new GlassButton(bigText, smallText, bg, fg);
        String value = bigText; 
        btn.addActionListener(e -> handleKeyPress(value));
        panel.add(btn);
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
            acceptCurrentWord();
            messageArea.append(" ");
        }

        if(currentSignature.length() > 0) {
            signatureLabel.setText("Sig: " + currentSignature.toString());
        } else {
            signatureLabel.setText(" ");
        }
    }

    private void updateCandidates() {
        if (currentSignature.length() == 0) {
            currentCandidates.clear();
            currentIndex = -1;
            candidateLabel.setText("Type to predict...");
            candidateLabel.setForeground(new Color(150,150,150));
            return;
        }

        Set<String> set = dict.signatureToWords(currentSignature.toString());
        currentCandidates = new ArrayList<>(set);

        if (currentCandidates.isEmpty()) {
            currentIndex = -1;
            candidateLabel.setText("(No match)");
            candidateLabel.setForeground(new Color(200, 50, 50));
        } else {
            currentIndex = 0;
            candidateLabel.setText(currentCandidates.get(currentIndex));
            candidateLabel.setForeground(new Color(0, 0, 0));
        }
    }

    private void showNextCandidate() {
        if (currentCandidates == null || currentCandidates.isEmpty()) return;
        currentIndex = (currentIndex + 1) % currentCandidates.size();
        candidateLabel.setText(currentCandidates.get(currentIndex));
    }

    private void acceptCurrentWord() {
        if (currentIndex >= 0 && currentIndex < currentCandidates.size()) {
            messageArea.append(currentCandidates.get(currentIndex));
        } else if (currentSignature.length() > 0) {
            messageArea.append(currentSignature.toString());
        }
        
        currentSignature.setLength(0);
        updateCandidates();
        signatureLabel.setText(" ");
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> new PredictiveGUI().setVisible(true));
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(245, 247, 250);
            Color color2 = new Color(195, 207, 226);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    class GlassButton extends JButton {
        private Color baseColor;
        private Color textColor;
        private String bigText;
        private String smallText;
        private boolean isHovered = false;

        public GlassButton(String big, String small, Color bg, Color fg) {
            this.baseColor = bg;
            this.textColor = fg;
            this.bigText = big;
            this.smallText = small;
            
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(3, 3, w - 6, h - 6, 30, 30);

            if (isHovered) {
                g2.setColor(new Color(Math.min(255, baseColor.getRed() + 20), 
                                      Math.min(255, baseColor.getGreen() + 20), 
                                      Math.min(255, baseColor.getBlue() + 20), 
                                      baseColor.getAlpha()));
            } else {
                g2.setColor(baseColor);
            }
            g2.fillRoundRect(0, 0, w - 3, h - 3, 30, 30);

            g2.setColor(textColor);
            
            Font bigFont = new Font("SansSerif", Font.PLAIN, 28);
            g2.setFont(bigFont);
            FontMetrics fmBig = g2.getFontMetrics();
            int xBig = (w - fmBig.stringWidth(bigText)) / 2;
            int yBig = (h - fmBig.getHeight()) / 2 + fmBig.getAscent() - 5;
            g2.drawString(bigText, xBig, yBig);

            if (!smallText.isEmpty()) {
                Font smallFont = new Font("SansSerif", Font.PLAIN, 12);
                g2.setFont(smallFont);
                FontMetrics fmSmall = g2.getFontMetrics();
                int xSmall = (w - fmSmall.stringWidth(smallText)) / 2;
                int ySmall = yBig + 15; 
                g2.drawString(smallText, xSmall, ySmall);
            }

            g2.dispose();
        }
    }
}
