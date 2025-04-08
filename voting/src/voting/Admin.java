package voting;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Admin extends JFrame {

    private JTextField passwordField;
    private Color primaryColor = new Color(25, 118, 210);
    private Color secondaryColor = new Color(245, 245, 245);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public Admin() {
        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(secondaryColor);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Admin Authentication", JLabel.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(primaryColor);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        inputPanel.setBackground(secondaryColor);

        JLabel label = new JLabel("Enter Password:");
        label.setFont(labelFont);
        
        passwordField = new JPasswordField();
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(10, 10, 10, 10)));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JButton loginButton = createStyledButton("Login", primaryColor, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(150, 40));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.add(loginButton);

        inputPanel.add(label);
        inputPanel.add(passwordField);
        inputPanel.add(buttonPanel);

        // Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(secondaryColor);
        JButton backButton = createStyledButton("Back", new Color(100, 100, 100), Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
        });
        bottomPanel.add(backButton);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordField.getText();
                if (password.equals("hehe")) {
                    openAdminPanel();
                } else {
                    JOptionPane.showMessageDialog(Admin.this, 
                        "Incorrect password!", 
                        "Authentication Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void openAdminPanel() {
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.setVisible(true);
        dispose(); // Close the login window
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
                new Admin().setVisible(true);
            }
        });
    }
}

class AdminPanel extends JFrame {
    private Color primaryColor = new Color(25, 118, 210);
    private Color secondaryColor = new Color(245, 245, 245);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(secondaryColor);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(secondaryColor);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JLabel headerLabel = new JLabel("Election Management", JLabel.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(primaryColor);

        // Create tabbed pane for different admin functions
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Basic functions panel
        JPanel basicPanel = createBasicFunctionsPanel();
        tabbedPane.addTab("Basic Functions", basicPanel);
        
        // Advanced queries panel
        JPanel advancedPanel = createAdvancedQueriesPanel();
        tabbedPane.addTab("Advanced Queries", advancedPanel);
        
        // Custom SQL panel
        JPanel customSqlPanel = createCustomSqlPanel();
        tabbedPane.addTab("Custom SQL", customSqlPanel);
        
        // Database Setup Panel
        JPanel dbSetupPanel = createDatabaseSetupPanel();
        tabbedPane.addTab("Database Setup", dbSetupPanel);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Exit button
        JButton exitButton = createStyledButton("Exit Admin Panel", new Color(211, 47, 47), Color.WHITE);
        exitButton.addActionListener(e -> {
            dispose();
            new VotingSystemGUI().setVisible(true); // Show the login page again
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(secondaryColor);
        bottomPanel.add(exitButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private JPanel createBasicFunctionsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JButton resultButton = createStyledButton("View Results", primaryColor, Color.WHITE);
        JButton analyticsButton = createStyledButton("View Analytics", primaryColor, Color.WHITE);
        JButton exportButton = createStyledButton("Export Data", primaryColor, Color.WHITE);

        panel.add(resultButton);
        panel.add(analyticsButton);
        panel.add(exportButton);
        
        resultButton.addActionListener(e -> showResult());
        analyticsButton.addActionListener(e -> performAnalytics());
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Export functionality not implemented yet", 
            "Not Available", 
            JOptionPane.INFORMATION_MESSAGE));
        
        return panel;
    }
    
    private JPanel createAdvancedQueriesPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 15));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JButton fraudButton = createStyledButton("Check Duplicate Votes", primaryColor, Color.WHITE);
        JButton trendButton = createStyledButton("Voting Trends by Date", primaryColor, Color.WHITE);
        JButton recentButton = createStyledButton("Recent 10 Votes", primaryColor, Color.WHITE);
        JButton hourlyButton = createStyledButton("Voting Patterns by Hour", primaryColor, Color.WHITE);

        panel.add(fraudButton);
        panel.add(trendButton);
        panel.add(recentButton);
        panel.add(hourlyButton);
        
        fraudButton.addActionListener(e -> QueryManager.checkDuplicateVoters(this));
        trendButton.addActionListener(e -> QueryManager.getVotingTrend(this));
        recentButton.addActionListener(e -> QueryManager.getRecentVotes(this, 10));
        hourlyButton.addActionListener(e -> {
            String hourlyQuery = "SELECT HOUR(created_at) as hour_of_day, COUNT(*) as votes_count " +
                                "FROM voters GROUP BY HOUR(created_at) ORDER BY hour_of_day";
            QueryManager.runCustomQuery(this, hourlyQuery);
        });
        
        return panel;
    }
    
    private JPanel createCustomSqlPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel instructionLabel = new JLabel("Enter custom SQL query:");
        instructionLabel.setFont(labelFont);
        
        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        queryTextArea.setLineWrap(true);
        queryTextArea.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(10, 10, 10, 10)));
        
        // Sample queries to help the admin
        String sampleQuery = "-- Example: Party distribution\n" +
                          "SELECT party_voted_for, COUNT(*) AS count,\n" +
                          "ROUND((COUNT(*) * 100) / (SELECT COUNT(*) FROM voters), 1) AS percentage\n" +
                          "FROM voters\n" +
                          "GROUP BY party_voted_for;";
        queryTextArea.setText(sampleQuery);
        
        JButton runButton = createStyledButton("Run Query", primaryColor, Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(secondaryColor);
        buttonPanel.add(runButton);
        
        panel.add(instructionLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(queryTextArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        runButton.addActionListener(e -> {
            String query = queryTextArea.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a SQL query", 
                    "Empty Query", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Only allow SELECT queries for safety
            if (!query.toLowerCase().startsWith("select")) {
                JOptionPane.showMessageDialog(this, 
                    "Only SELECT queries are allowed for security reasons", 
                    "Query Restricted", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            QueryManager.runCustomQuery(this, query);
        });
        
        return panel;
    }
    
    private JPanel createDatabaseSetupPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(secondaryColor);
        panel.setBorder(new EmptyBorder(20, 50, 20, 50));
        
        JLabel instructionLabel = new JLabel(
            "<html><body style='width: 350px'>"+
            "Use this panel to execute SQL scripts and set up your database. "+
            "You can run the included database_setup.sql file or write custom SQL statements."+
            "</body></html>"
        );
        instructionLabel.setFont(labelFont);
        
        JButton openButton = createStyledButton("Open SQL Script Editor", primaryColor, Color.WHITE);
        JButton setupButton = createStyledButton("Run Database Setup", primaryColor, Color.WHITE);
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setBackground(secondaryColor);
        
        JLabel iconLabel = new JLabel(new ImageIcon("src/voting/database_icon.png"));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        centerPanel.add(iconLabel);
        centerPanel.add(openButton);
        centerPanel.add(setupButton);
        
        panel.add(instructionLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Event handlers
        openButton.addActionListener(e -> {
            DBSetup dbSetup = new DBSetup();
            dbSetup.setVisible(true);
        });
        
        setupButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "This will execute database_setup.sql and create/modify database tables.\n" +
                "Do you want to continue?",
                "Confirm Database Setup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DBManager.getConnection();
                    Statement stmt = conn.createStatement();
                    
                    // Execute the necessary setup queries
                    stmt.execute("CREATE DATABASE IF NOT EXISTS voting_system;");
                    stmt.execute("USE voting_system;");
                    stmt.execute("CREATE TABLE IF NOT EXISTS voters (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "hashed_name VARCHAR(255) NOT NULL," +
                                "hashed_age VARCHAR(255) NOT NULL," +
                                "hashed_voter_id VARCHAR(255) NOT NULL," +
                                "party_voted_for VARCHAR(50) NOT NULL," +
                                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
                                
                    JOptionPane.showMessageDialog(this,
                        "Database setup completed successfully!",
                        "Setup Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    stmt.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error setting up database: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void showResult() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "hehe");
            Statement statement = connection.createStatement();

            // Query to get total votes cast for each party
            String totalVotesQuery = "SELECT party_voted_for, COUNT(*) AS total_votes FROM voters GROUP BY party_voted_for";
            ResultSet totalVotesResult = statement.executeQuery(totalVotesQuery);

            int totalVotesCast = 0;
            while (totalVotesResult.next()) {
                totalVotesCast += totalVotesResult.getInt("total_votes");
            }

            // Query to get total votes for each party and calculate percentage
            String resultQuery = "SELECT party_voted_for, COUNT(*) AS party_votes, " +
                                 "ROUND((COUNT(*) * 100) / " + totalVotesCast + ", 2) AS vote_percentage " +
                                 "FROM voters GROUP BY party_voted_for";
            ResultSet resultSet = statement.executeQuery(resultQuery);

            // Create a styled dialog for results
            JDialog resultDialog = new JDialog(this, "Election Results", true);
            resultDialog.setSize(500, 400);
            resultDialog.setLocationRelativeTo(this);
            
            JPanel resultPanel = new JPanel(new BorderLayout(20, 20));
            resultPanel.setBackground(Color.WHITE);
            resultPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Election Results", JLabel.CENTER);
            titleLabel.setFont(headerFont);
            titleLabel.setForeground(primaryColor);
            
            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setBackground(Color.WHITE);
            
            JLabel totalVotesLabel = new JLabel("Total Votes Cast: " + totalVotesCast);
            totalVotesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            totalVotesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dataPanel.add(totalVotesLabel);
            dataPanel.add(Box.createVerticalStrut(20));
            
            // Create result table
            String[] columnNames = {"Party", "Votes", "Percentage"};
            Object[][] data = new Object[4][3]; // Assuming 4 parties maximum
            
            int rowIndex = 0;
            while (resultSet.next()) {
                String party = resultSet.getString("party_voted_for");
                int partyVotes = resultSet.getInt("party_votes");
                double votePercentage = resultSet.getDouble("vote_percentage");
                
                data[rowIndex][0] = party;
                data[rowIndex][1] = partyVotes;
                data[rowIndex][2] = votePercentage + "%";
                rowIndex++;
            }
            
            JTable resultTable = new JTable(data, columnNames);
            resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            resultTable.setRowHeight(30);
            resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            resultTable.getTableHeader().setBackground(new Color(240, 240, 240));
            
            JScrollPane scrollPane = new JScrollPane(resultTable);
            scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            dataPanel.add(scrollPane);
            
            JButton closeButton = new JButton("Close");
            closeButton.setFont(buttonFont);
            closeButton.addActionListener(e -> resultDialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(closeButton);
            
            resultPanel.add(titleLabel, BorderLayout.NORTH);
            resultPanel.add(dataPanel, BorderLayout.CENTER);
            resultPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            resultDialog.add(resultPanel);
            resultDialog.setVisible(true);

            totalVotesResult.close();
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error connecting to database: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performAnalytics() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/voting_system", "root", "hehe");
            Statement statement = connection.createStatement();

            // Query to count total voters
            String totalVotersQuery = "SELECT COUNT(*) AS total_voters FROM voters";
            ResultSet totalVotersResult = statement.executeQuery(totalVotersQuery);
            
            int totalVoters = 0;
            if (totalVotersResult.next()) {
                totalVoters = totalVotersResult.getInt("total_voters");
            }

            // Create a styled dialog for analytics
            JDialog analyticsDialog = new JDialog(this, "Voting Analytics", true);
            analyticsDialog.setSize(500, 400);
            analyticsDialog.setLocationRelativeTo(this);
            
            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Voting System Analytics", JLabel.CENTER);
            titleLabel.setFont(headerFont);
            titleLabel.setForeground(primaryColor);
            
            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setBackground(Color.WHITE);
            
            JLabel totalVotersLabel = new JLabel("Total Voters: " + totalVoters);
            totalVotersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            totalVotersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dataPanel.add(totalVotersLabel);
            dataPanel.add(Box.createVerticalStrut(20));
            
            // Party distribution
            JLabel partyDistributionLabel = new JLabel("Party Distribution:");
            partyDistributionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            partyDistributionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dataPanel.add(partyDistributionLabel);
            
            String partyQuery = "SELECT party_voted_for, COUNT(*) AS count, " +
                               "ROUND((COUNT(*) * 100) / " + totalVoters + ", 1) AS percentage " +
                               "FROM voters GROUP BY party_voted_for";
            ResultSet partyResult = statement.executeQuery(partyQuery);
            
            JPanel partyPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            partyPanel.setBackground(Color.WHITE);
            partyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            while (partyResult.next()) {
                String party = partyResult.getString("party_voted_for");
                int count = partyResult.getInt("count");
                double percentage = partyResult.getDouble("percentage");
                
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                
                JLabel nameLabel = new JLabel(party);
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                
                JProgressBar bar = new JProgressBar(0, 100);
                bar.setValue((int)percentage);
                bar.setStringPainted(true);
                bar.setString(count + " votes (" + percentage + "%)");
                bar.setForeground(primaryColor);
                
                row.add(nameLabel, BorderLayout.WEST);
                row.add(bar, BorderLayout.CENTER);
                
                partyPanel.add(row);
            }
            
            dataPanel.add(partyPanel);
            dataPanel.add(Box.createVerticalStrut(20));
            
            JButton closeButton = new JButton("Close");
            closeButton.setFont(buttonFont);
            closeButton.addActionListener(e -> analyticsDialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(closeButton);
            
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(new JScrollPane(dataPanel), BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            analyticsDialog.add(mainPanel);
            analyticsDialog.setVisible(true);

            partyResult.close();
            totalVotersResult.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error connecting to database: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
