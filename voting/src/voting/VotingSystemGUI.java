package voting;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.FontMetrics;

public class VotingSystemGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private JTextField nameField;
    private JTextField ageField;
    private JTextField voterIdField;
    private Color primaryColor = new Color(25, 118, 210);
    private Color secondaryColor = new Color(245, 245, 245);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public VotingSystemGUI() {
        setTitle("E-Voting System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(secondaryColor);

        // Create multiple step panels for data collection
        JPanel welcomePanel = createWelcomePanel();
        JPanel namePanel = createNamePanel();
        JPanel agePanel = createAgePanel();
        JPanel voterIdPanel = createVoterIdPanel();
        JPanel partyPanel = createPartyPanel();

        cardPanel.add(welcomePanel, "welcome");
        cardPanel.add(namePanel, "name");
        cardPanel.add(agePanel, "age");
        cardPanel.add(voterIdPanel, "voterId");
        cardPanel.add(partyPanel, "party");

        add(cardPanel);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel welcomeLabel = new JLabel("Welcome to E-Voting System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcomeLabel.setForeground(primaryColor);
        
        JLabel subLabel = new JLabel("Your vote matters. Let's get started.", JLabel.CENTER);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subLabel.setForeground(new Color(100, 100, 100));
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        centerPanel.add(welcomeLabel, gbc);
        centerPanel.add(subLabel, gbc);
        
        JButton startButton = createStyledButton("Start Voting Process", primaryColor, Color.WHITE);
        startButton.setPreferredSize(new Dimension(250, 50));
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startButton.addActionListener(e -> cardLayout.show(cardPanel, "name"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.add(startButton);
        
        JButton adminButton = createStyledButton("Admin Panel", new Color(100, 100, 100), Color.WHITE);
        adminButton.addActionListener(e -> new Admin().setVisible(true));
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(secondaryColor);
        bottomPanel.add(adminButton);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createNamePanel() {
        JPanel panel = createStepPanel("Enter Your Full Name", "1/3");
        nameField = new JTextField();
        nameField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(10, 10, 10, 10)));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel inputPanel = new JPanel(new BorderLayout(0, 10));
        inputPanel.setBackground(secondaryColor);
        inputPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        
        inputPanel.add(nameLabel, BorderLayout.NORTH);
        inputPanel.add(nameField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        
        JButton backButton = createStyledButton("Back", new Color(100, 100, 100), Color.WHITE);
        JButton nextButton = createStyledButton("Next", primaryColor, Color.WHITE);
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "welcome"));
        nextButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cardLayout.show(cardPanel, "age");
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAgePanel() {
        JPanel panel = createStepPanel("Enter Your Age", "2/3");
        ageField = new JTextField();
        ageField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(10, 10, 10, 10)));
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel inputPanel = new JPanel(new BorderLayout(0, 10));
        inputPanel.setBackground(secondaryColor);
        inputPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel ageLabel = new JLabel("Age (must be 18 or older):");
        ageLabel.setFont(labelFont);
        
        inputPanel.add(ageLabel, BorderLayout.NORTH);
        inputPanel.add(ageField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        
        JButton backButton = createStyledButton("Back", new Color(100, 100, 100), Color.WHITE);
        JButton nextButton = createStyledButton("Next", primaryColor, Color.WHITE);
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "name"));
        nextButton.addActionListener(e -> {
            try {
                String age = ageField.getText().trim();
                if (age.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please enter your age", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int ageInt = Integer.parseInt(age);
                if (ageInt < 18) {
                    JOptionPane.showMessageDialog(panel, 
                        "You must be at least 18 years old to vote", 
                        "Age Verification Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                cardLayout.show(cardPanel, "voterId");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Please enter a valid number for age", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createVoterIdPanel() {
        JPanel panel = createStepPanel("Enter Your Voter ID", "3/3");
        voterIdField = new JTextField();
        voterIdField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(10, 10, 10, 10)));
        voterIdField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel inputPanel = new JPanel(new BorderLayout(0, 10));
        inputPanel.setBackground(secondaryColor);
        inputPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel voterIdLabel = new JLabel("Voter ID Number:");
        voterIdLabel.setFont(labelFont);
        
        inputPanel.add(voterIdLabel, BorderLayout.NORTH);
        inputPanel.add(voterIdField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        
        JButton backButton = createStyledButton("Back", new Color(100, 100, 100), Color.WHITE);
        JButton nextButton = createStyledButton("Continue to Vote", primaryColor, Color.WHITE);
        
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "age"));
        nextButton.addActionListener(e -> {
            String voterId = voterIdField.getText().trim();
            if (voterId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter your Voter ID", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cardLayout.show(cardPanel, "party");
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createStepPanel(String title, String step) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(secondaryColor);
        
        JLabel titleLabel = new JLabel(title, JLabel.LEFT);
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(primaryColor);
        
        JLabel stepLabel = new JLabel("Step " + step, JLabel.RIGHT);
        stepLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        stepLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(stepLabel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        return panel;
    }

    private JPanel createPartyPanel() {
        JPanel partyPanel = new JPanel(new BorderLayout(20, 20));
        partyPanel.setBackground(secondaryColor);
        partyPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        JLabel headerLabel = new JLabel("Cast Your Vote", JLabel.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(primaryColor);
        
        // Party options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        optionsPanel.setBackground(secondaryColor);
        optionsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Load images using absolute path
        ImageIcon bjpIcon = loadImageCorrectly("bjp_logo.png", 100, 100);
        ImageIcon incIcon = loadImageCorrectly("inc_logo.png", 100, 100);
        ImageIcon aapIcon = loadImageCorrectly("aap_logo.png", 100, 100);
        
        // Create party cards
        JPanel bjpCard = createPartyCard("BJP", bjpIcon, "Bharatiya Janata Party");
        JPanel incCard = createPartyCard("INC", incIcon, "Indian National Congress");
        JPanel aapCard = createPartyCard("AAP", aapIcon, "Aam Aadmi Party");
        JPanel notaCard = createPartyCard("NOTA", null, "None of the Above");
        
        optionsPanel.add(bjpCard);
        optionsPanel.add(incCard);
        optionsPanel.add(aapCard);
        optionsPanel.add(notaCard);
        
        // Add to main panel
        partyPanel.add(headerLabel, BorderLayout.NORTH);
        partyPanel.add(optionsPanel, BorderLayout.CENTER);
        
        // Add back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(secondaryColor);
        JButton backButton = createStyledButton("Change Details", new Color(100, 100, 100), Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "voterId"));
        bottomPanel.add(backButton);
        
        partyPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return partyPanel;
    }
    
    private JPanel createPartyCard(String partyCode, ImageIcon icon, String partyName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(15, 15, 15, 15)));
        card.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(partyName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton voteButton = new JButton("Vote");
        voteButton.setFont(buttonFont);
        voteButton.setBackground(primaryColor);
        voteButton.setForeground(Color.WHITE);
        voteButton.setBorderPainted(false);
        voteButton.setFocusPainted(false);
        voteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        voteButton.addActionListener(e -> registerVote(partyCode));
        
        // Add components to card
        card.add(Box.createVerticalStrut(10));
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(iconLabel);
            card.add(Box.createVerticalStrut(15));
        } else {
            JLabel notaLabel = new JLabel("NOTA");
            notaLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            notaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(notaLabel);
            card.add(Box.createVerticalStrut(15));
        }
        
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(voteButton);
        
        return card;
    }
    
    private ImageIcon loadImageCorrectly(String filename, int width, int height) {
        try {
            // Try multiple paths to locate the image
            String[] possiblePaths = {
                filename,                                   // Current directory
                "src/voting/" + filename,                   // Relative source path
                "voting/src/voting/" + filename,            // Project relative path
                System.getProperty("user.dir") + "/src/voting/" + filename, // Absolute path from user directory
                new File("").getAbsolutePath() + "/src/voting/" + filename  // Absolute path from working directory
            };
            
            for (String path : possiblePaths) {
                File file = new File(path);
                if (file.exists()) {
                    System.out.println("Found image at: " + path);
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
            }
            
            // If file wasn't found in the filesystem, try class resource
            java.net.URL imgURL = getClass().getResource("/" + filename);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            
            System.err.println("Could not find image: " + filename);
            return createPlaceholderIcon(width, height, partyInitials(filename));
        } catch (Exception e) {
            System.err.println("Error loading image: " + filename);
            e.printStackTrace();
            return createPlaceholderIcon(width, height, partyInitials(filename));
        }
    }
    
    private String partyInitials(String filename) {
        if (filename.contains("bjp")) return "BJP";
        if (filename.contains("inc")) return "INC";
        if (filename.contains("aap")) return "AAP";
        return "?";
    }
    
    private ImageIcon createPlaceholderIcon(int width, int height, String text) {
        // Create placeholder image with party initials
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fill background
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, width, height);
        
        // Draw text
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, (height - textHeight) / 2 + fm.getAscent());
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private void registerVote(String party) {
        String name = nameField.getText();
        String age = ageField.getText();
        String voterId = voterIdField.getText();

        // Hash name, age, and voter ID
        String hashedName = hashString(name);
        String hashedAge = hashString(age);
        String hashedVoterId = hashString(voterId);

        // Register vote with hashed details
        VotingSystemDAO dao = new VotingSystemDAO();
        dao.insertVoterDetails(hashedName, hashedAge, hashedVoterId, party);
        dao.closeConnection();

        // Show confirmation message with animation
        showVoteConfirmation(party);
    }
    
    private void showVoteConfirmation(String party) {
        JDialog dialog = new JDialog(this, "Vote Confirmation", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel(new ImageIcon(createCheckMarkImage(100, 100)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Vote Successfully Cast!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(46, 125, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel partyLabel = new JLabel("You voted for: " + party);
        partyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        partyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton closeButton = createStyledButton("Return to Start", primaryColor, Color.WHITE);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> {
            dialog.dispose();
            // Clear fields and navigate back to welcome page
            nameField.setText("");
            ageField.setText("");
            voterIdField.setText("");
            cardLayout.show(cardPanel, "welcome");
        });
        
        panel.add(Box.createVerticalStrut(20));
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(partyLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(closeButton);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private Image createCheckMarkImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw green circle
        g2d.setColor(new Color(46, 125, 50));
        g2d.fillOval(0, 0, width, height);
        
        // Draw white checkmark
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(width/10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int[] xPoints = {width/4, width/2, width*3/4};
        int[] yPoints = {height/2, height*3/4, height/3};
        g2d.drawPolyline(xPoints, yPoints, 3);
        
        g2d.dispose();
        return image;
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VotingSystemGUI();
            }
        });
    }
}

