import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TypingRaceGUI {
    private JFrame frame;
    private JPanel mainPanel;

    private JComboBox<String> passageBox;
    private JTextArea customPassageArea;
    private JComboBox<Integer> seatCountBox;
    private JCheckBox autocorrectBox, caffeineBox, nightShiftBox;

    private JTextField[] nameFields = new JTextField[6];
    private JTextField[] symbolFields = new JTextField[6];
    private JComboBox<String>[] colourBoxes = new JComboBox[6];
    private JComboBox<String>[] styleBoxes = new JComboBox[6];
    private JComboBox<String>[] keyboardBoxes = new JComboBox[6];
    private JComboBox<String>[] accessoryBoxes = new JComboBox[6];

    private ArrayList<JTextPane> passagePanes;
    private ArrayList<JProgressBar> progressBars;

    private int[] leaderboardPoints = new int[6];
    private int[] winCounts = new int[6];
    private double[] personalBestWpm = new double[6];
    private int[] totalEarnings = new int[6];

    private final String placeholder = "Type your custom passage here...";

    private final String[] defaultNames = {
        "TURBOFINGERS", "QWERTY_QUEEN", "HUNT_N_PECK",
        "SPEED_DEMON", "KEY_SMASHER", "CODE_TYPER"
    };

    private final char[] defaultSymbols = {'1', '2', '3', '4', '5', '6'};

    public TypingRaceGUI() {
        frame = new JFrame("Typing Race Simulator - GUI");
        frame.setSize(1050, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        createSetupScreen(2);
    }

    private void createSetupScreen(int selectedSeats) {
        mainPanel.removeAll();

        JLabel title = new JLabel("Typing Race Simulator", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        passageBox = new JComboBox<>(new String[] {
            "Short: The quick brown fox jumps.",
            "Medium: Java programming helps build strong problem solving skills.",
            "Long: Object oriented programming allows classes and objects to work together clearly.",
            "Custom Passage"
        });

        customPassageArea = new JTextArea(placeholder, 3, 40);
        customPassageArea.setLineWrap(true);
        customPassageArea.setWrapStyleWord(true);
        customPassageArea.setForeground(Color.GRAY);

        customPassageArea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (customPassageArea.getText().equals(placeholder)) {
                    customPassageArea.setText("");
                    customPassageArea.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (customPassageArea.getText().trim().isEmpty()) {
                    customPassageArea.setText(placeholder);
                    customPassageArea.setForeground(Color.GRAY);
                }
            }
        });

        seatCountBox = new JComboBox<>(new Integer[] {2, 3, 4, 5, 6});
        seatCountBox.setSelectedItem(selectedSeats);
        seatCountBox.addActionListener(e -> createSetupScreen((Integer) seatCountBox.getSelectedItem()));

        autocorrectBox = new JCheckBox("Autocorrect Mode - slide back amount is halved");
        caffeineBox = new JCheckBox("Caffeine Mode - first 10 turns boosted, later burnout risk increases");
        nightShiftBox = new JCheckBox("Night Shift - all accuracies slightly reduced");

        content.add(row("Passage Selection:", passageBox));
        content.add(row("Custom Passage:", new JScrollPane(customPassageArea)));
        content.add(row("Seat Count:", seatCountBox));
        content.add(row("Difficulty:", autocorrectBox));
        content.add(row("", caffeineBox));
        content.add(row("", nightShiftBox));

        createTypistControls();

        for (int i = 0; i < selectedSeats; i++) {
            JPanel box = new JPanel(new GridLayout(6, 2, 5, 5));
            box.setBorder(BorderFactory.createTitledBorder("Typist " + (i + 1) + " Customisation"));

            box.add(new JLabel("Name:"));
            box.add(nameFields[i]);

            box.add(new JLabel("Symbol:"));
            box.add(symbolFields[i]);

            box.add(new JLabel("Colour:"));
            box.add(colourBoxes[i]);

            box.add(new JLabel("Typing Style:"));
            box.add(styleBoxes[i]);

            box.add(new JLabel("Keyboard Type:"));
            box.add(keyboardBoxes[i]);

            box.add(new JLabel("Accessory:"));
            box.add(accessoryBoxes[i]);

            content.add(box);
        }

        JTextArea impact = new JTextArea(
            "Attribute Impact Guide:\n" +
            "Touch Typist: balanced high accuracy.\n" +
            "Hunt & Peck: lower accuracy, lower burnout risk.\n" +
            "Phone Thumbs: faster first 10 turns, more mistakes.\n" +
            "Voice-to-Text: higher speed but unstable.\n" +
            "Mechanical: small accuracy boost.\n" +
            "Touchscreen: higher mistype risk.\n" +
            "Stenography: speed boost but more burnout risk.\n" +
            "Wrist Support: reduces burnout.\n" +
            "Energy Drink: boosts first half, reduces second half.\n" +
            "Noise-Cancelling Headphones: reduces mistypes.\n\n" +
            "Sponsor & Prize System:\n" +
            "1st place earns 100 coins, 2nd earns 60, 3rd earns 30, others earn 10.\n" +
            "KeyCorp bonus: +50 coins for finishing with no burnout.\n" +
            "SpeedBank bonus: +30 coins for WPM above 60."
        );
        impact.setEditable(false);
        impact.setLineWrap(true);
        impact.setWrapStyleWord(true);
        content.add(row("Attribute Impact:", new JScrollPane(impact)));

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> startRace());

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(content), BorderLayout.CENTER);
        mainPanel.add(startButton, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel row(String label, Component component) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        JLabel left = new JLabel(label);
        left.setPreferredSize(new Dimension(180, 30));
        panel.add(left, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(1000, 90));
        return panel;
    }

    private void createTypistControls() {
        for (int i = 0; i < 6; i++) {
            nameFields[i] = new JTextField(defaultNames[i]);
            symbolFields[i] = new JTextField(String.valueOf(defaultSymbols[i]));

            colourBoxes[i] = new JComboBox<>(new String[] {
                "Blue", "Green", "Red", "Orange", "Purple", "Pink"
            });

            styleBoxes[i] = new JComboBox<>(new String[] {
                "Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"
            });

            keyboardBoxes[i] = new JComboBox<>(new String[] {
                "Mechanical", "Membrane", "Touchscreen", "Stenography"
            });

            accessoryBoxes[i] = new JComboBox<>(new String[] {
                "None", "Wrist Support", "Energy Drink", "Noise-Cancelling Headphones"
            });
        }
    }

    private String getPassage() {
        String selected = (String) passageBox.getSelectedItem();

        if (selected.equals("Custom Passage")) {
            String custom = customPassageArea.getText();

            if (custom.equals(placeholder) || custom.trim().isEmpty()) {
                return "Custom passage was empty so this default passage is being used.";
            }

            return custom;
        }

        return selected.substring(selected.indexOf(":") + 2);
    }

    private void startRace() {
        mainPanel.removeAll();

        String passage = getPassage();
        int length = passage.length();
        int seats = (Integer) seatCountBox.getSelectedItem();

        ArrayList<Typist> typists = new ArrayList<>();

        for (int i = 0; i < seats; i++) {
            String name = nameFields[i].getText().trim();
            char symbol = symbolFields[i].getText().isEmpty()
                ? defaultSymbols[i]
                : symbolFields[i].getText().charAt(0);

            Typist typist = new Typist(symbol, name, calculateAccuracy(i));
            typists.add(typist);
        }

        JLabel title = new JLabel("Live Typing Race", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel racePanel = new JPanel();
        racePanel.setLayout(new BoxLayout(racePanel, BoxLayout.Y_AXIS));

        passagePanes = new ArrayList<>();
        progressBars = new ArrayList<>();

        for (int i = 0; i < seats; i++) {
            Typist t = typists.get(i);

            JPanel lane = new JPanel(new BorderLayout(5, 5));
            lane.setBorder(BorderFactory.createTitledBorder(
                t.getSymbol() + " " + t.getName() +
                " | " + styleBoxes[i].getSelectedItem() +
                " | " + keyboardBoxes[i].getSelectedItem() +
                " | " + accessoryBoxes[i].getSelectedItem()
            ));

            JTextPane textPane = new JTextPane();
            textPane.setEditable(false);
            textPane.setFont(new Font("Monospaced", Font.PLAIN, 16));
            textPane.setText(passage);

            JProgressBar bar = new JProgressBar(0, length);
            bar.setStringPainted(true);
            bar.setString(t.getName() + " 0/" + length);
            bar.setForeground(getColour((String) colourBoxes[i].getSelectedItem()));

            passagePanes.add(textPane);
            progressBars.add(bar);

            lane.add(new JScrollPane(textPane), BorderLayout.CENTER);
            lane.add(bar, BorderLayout.SOUTH);
            lane.setPreferredSize(new Dimension(900, 105));

            racePanel.add(lane);
        }

        JTextArea statsArea = new JTextArea(11, 40);
        statsArea.setEditable(false);
        statsArea.setText("Race running...");

        JButton backButton = new JButton("Back to Setup");
        backButton.addActionListener(e -> createSetupScreen(seats));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        bottom.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(racePanel), BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();

        runRace(typists, passage, length, statsArea);
    }

    private double calculateAccuracy(int index) {
        double accuracy = 0.70;

        String style = (String) styleBoxes[index].getSelectedItem();
        String keyboard = (String) keyboardBoxes[index].getSelectedItem();
        String accessory = (String) accessoryBoxes[index].getSelectedItem();

        if (style.equals("Touch Typist")) accuracy += 0.15;
        if (style.equals("Hunt & Peck")) accuracy -= 0.10;
        if (style.equals("Phone Thumbs")) accuracy -= 0.05;
        if (style.equals("Voice-to-Text")) accuracy += 0.05;

        if (keyboard.equals("Mechanical")) accuracy += 0.05;
        if (keyboard.equals("Touchscreen")) accuracy -= 0.10;
        if (keyboard.equals("Stenography")) accuracy += 0.10;

        if (accessory.equals("Noise-Cancelling Headphones")) accuracy += 0.05;

        if (nightShiftBox.isSelected()) accuracy -= 0.10;

        if (accuracy < 0.0) accuracy = 0.0;
        if (accuracy > 1.0) accuracy = 1.0;

        return accuracy;
    }

    private void runRace(ArrayList<Typist> typists, String passage, int length, JTextArea statsArea) {
        new Thread(() -> {
            boolean finished = false;
            int turn = 0;
            int[] burnoutCounts = new int[typists.size()];
            int[] mistypeCounts = new int[typists.size()];
            long startTime = System.currentTimeMillis();

            while (!finished) {
                turn++;

                for (int i = 0; i < typists.size(); i++) {
                    Typist t = typists.get(i);
                    boolean wasBurntOut = t.isBurntOut();
                    int before = t.getProgress();

                    advanceWithModifiers(t, i, turn);

                    if (!wasBurntOut && t.isBurntOut()) {
                        burnoutCounts[i]++;
                    }

                    if (t.getProgress() < before) {
                        mistypeCounts[i]++;
                    }

                    progressBars.get(i).setValue(t.getProgress());
                    progressBars.get(i).setString(
                        t.getSymbol() + " " + t.getName() + " - " + t.getProgress() + "/" + length
                    );

                    updateHighlight(passagePanes.get(i), passage, t.getProgress());

                    if (t.getProgress() >= length) {
                        finished = true;
                    }
                }

                try {
                    Thread.sleep(140);
                } catch (Exception e) {
                    System.out.println("Race interrupted.");
                }
            }

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;
            double wpm = (length / 5.0) / (seconds / 60.0);

            int winnerIndex = getWinnerIndex(typists, length);
            Typist winner = typists.get(winnerIndex);

            winCounts[winnerIndex]++;
            updateLeaderboardPoints(typists);
            updateEarnings(typists, burnoutCounts, wpm);

            if (wpm > personalBestWpm[winnerIndex]) {
                personalBestWpm[winnerIndex] = wpm;
            }

            statsArea.setText(buildStats(typists, winner, seconds, wpm, burnoutCounts, mistypeCounts));
            JOptionPane.showMessageDialog(frame, "Winner: " + winner.getName());

        }).start();
    }

    private void advanceWithModifiers(Typist t, int index, int turn) {
        if (t.isBurntOut()) {
            t.recoverFromBurnout();
            return;
        }

        double accuracy = t.getAccuracy();

        String style = (String) styleBoxes[index].getSelectedItem();
        String keyboard = (String) keyboardBoxes[index].getSelectedItem();
        String accessory = (String) accessoryBoxes[index].getSelectedItem();

        if (caffeineBox.isSelected() && turn <= 10) accuracy += 0.20;
        if (style.equals("Phone Thumbs") && turn <= 10) accuracy += 0.10;
        if (style.equals("Voice-to-Text")) accuracy += 0.05;
        if (accessory.equals("Energy Drink") && turn <= 15) accuracy += 0.15;
        if (accessory.equals("Energy Drink") && turn > 15) accuracy -= 0.10;

        if (accuracy > 1.0) accuracy = 1.0;
        if (accuracy < 0.0) accuracy = 0.0;

        if (Math.random() < accuracy) {
            t.typeCharacter();
        }

        double mistypeChance = (1 - accuracy) * 0.30;

        if (keyboard.equals("Touchscreen")) mistypeChance += 0.05;
        if (accessory.equals("Noise-Cancelling Headphones")) mistypeChance -= 0.05;

        if (mistypeChance < 0.0) mistypeChance = 0.0;

        if (Math.random() < mistypeChance) {
            int slide = autocorrectBox.isSelected() ? 1 : 2;
            t.slideBack(slide);
        }

        double burnoutChance = 0.05 * accuracy * accuracy;

        if (style.equals("Hunt & Peck")) burnoutChance -= 0.02;
        if (keyboard.equals("Stenography")) burnoutChance += 0.03;
        if (accessory.equals("Wrist Support")) burnoutChance -= 0.03;
        if (accessory.equals("Energy Drink") && turn > 15) burnoutChance += 0.06;
        if (caffeineBox.isSelected() && turn > 10) burnoutChance += 0.05;

        if (burnoutChance < 0.0) burnoutChance = 0.0;

        if (Math.random() < burnoutChance) {
            t.burnOut(3);
        }
    }

    private void updateLeaderboardPoints(ArrayList<Typist> typists) {
        ArrayList<Typist> ordered = new ArrayList<>(typists);
        ordered.sort((a, b) -> b.getProgress() - a.getProgress());

        for (int rank = 0; rank < ordered.size(); rank++) {
            int originalIndex = typists.indexOf(ordered.get(rank));

            if (rank == 0) leaderboardPoints[originalIndex] += 3;
            else if (rank == 1) leaderboardPoints[originalIndex] += 2;
            else if (rank == 2) leaderboardPoints[originalIndex] += 1;
        }
    }

    private void updateEarnings(ArrayList<Typist> typists, int[] burnoutCounts, double winnerWpm) {
        ArrayList<Typist> ordered = new ArrayList<>(typists);
        ordered.sort((a, b) -> b.getProgress() - a.getProgress());

        for (int rank = 0; rank < ordered.size(); rank++) {
            int originalIndex = typists.indexOf(ordered.get(rank));

            int coins;

            if (rank == 0) coins = 100;
            else if (rank == 1) coins = 60;
            else if (rank == 2) coins = 30;
            else coins = 10;

            if (burnoutCounts[originalIndex] == 0) {
                coins += 50;
            }

            if (rank == 0 && winnerWpm > 60) {
                coins += 30;
            }

            totalEarnings[originalIndex] += coins;
        }
    }

    private String buildStats(ArrayList<Typist> typists, Typist winner, double seconds, double wpm,
                              int[] burnoutCounts, int[] mistypeCounts) {
        StringBuilder sb = new StringBuilder();

        sb.append("Winner: ").append(winner.getName()).append("\n");
        sb.append("Time Taken: ").append(String.format("%.2f", seconds)).append(" seconds\n");
        sb.append("Winner WPM: ").append(String.format("%.2f", wpm)).append("\n\n");

        sb.append("Performance Metrics:\n");

        for (int i = 0; i < typists.size(); i++) {
            Typist t = typists.get(i);

            double accuracyPercent = 100.0 - (mistypeCounts[i] * 10.0);
            if (accuracyPercent < 0) accuracyPercent = 0;

            sb.append(t.getName())
              .append(" | Accuracy Rating: ").append(String.format("%.2f", t.getAccuracy()))
              .append(" | Accuracy %: ").append(String.format("%.1f", accuracyPercent)).append("%")
              .append(" | Burnouts: ").append(burnoutCounts[i])
              .append(" | Mistypes: ").append(mistypeCounts[i])
              .append(" | PB WPM: ").append(String.format("%.2f", personalBestWpm[i]))
              .append("\n");
        }

        sb.append("\nOption A - Leaderboard & Ranking System:\n");

        for (int i = 0; i < typists.size(); i++) {
            String title = "Rookie";

            if (winCounts[i] >= 3) title = "Speed Demon";
            if (burnoutCounts[i] == 0 && typists.get(i).getProgress() > 0) title = "Iron Fingers";

            sb.append(typists.get(i).getName())
              .append(" | Points: ").append(leaderboardPoints[i])
              .append(" | Wins: ").append(winCounts[i])
              .append(" | Title: ").append(title)
              .append("\n");
        }

        sb.append("\nOption B - Sponsor & Prize System:\n");
        sb.append("Sponsors: KeyCorp gives +50 coins for no burnout. SpeedBank gives +30 coins if winner WPM > 60.\n");

        for (int i = 0; i < typists.size(); i++) {
            sb.append(typists.get(i).getName())
              .append(" | Total Earnings: ")
              .append(totalEarnings[i])
              .append(" coins\n");
        }

        return sb.toString();
    }

    private int getWinnerIndex(ArrayList<Typist> typists, int length) {
        for (int i = 0; i < typists.size(); i++) {
            if (typists.get(i).getProgress() >= length) return i;
        }
        return 0;
    }

    private void updateHighlight(JTextPane pane, String passage, int progress) {
        StyledDocument doc = pane.getStyledDocument();

        Style normal = pane.addStyle("normal", null);
        Style done = pane.addStyle("done", null);

        StyleConstants.setForeground(normal, Color.BLACK);
        StyleConstants.setBackground(done, Color.BLUE);
        StyleConstants.setForeground(done, Color.WHITE);

        try {
            doc.remove(0, doc.getLength());

            int completed = Math.min(progress, passage.length());

            doc.insertString(0, passage.substring(0, completed), done);
            doc.insertString(doc.getLength(), passage.substring(completed), normal);
        } catch (Exception e) {
            System.out.println("Highlight failed.");
        }
    }

    private Color getColour(String colourName) {
        if (colourName.equals("Green")) return Color.GREEN;
        if (colourName.equals("Red")) return Color.RED;
        if (colourName.equals("Orange")) return Color.ORANGE;
        if (colourName.equals("Purple")) return new Color(128, 0, 128);
        if (colourName.equals("Pink")) return Color.PINK;

        return Color.BLUE;
    }

    public void startRaceGUI() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TypingRaceGUI().startRaceGUI();
    }
}