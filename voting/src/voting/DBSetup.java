package voting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DBSetup extends JFrame {
    
    private JTextArea logArea;
    private JButton executeButton;
    private JButton closeButton;
    private JComboBox<String> scriptSelector;
    private Color primaryColor = new Color(25, 118, 210);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
    
    public DBSetup() {
        setTitle("Database Setup");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Execute SQL Scripts", JLabel.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(primaryColor);
        
        // Script selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectLabel = new JLabel("Select Script: ");
        selectLabel.setFont(textFont);
        
        scriptSelector = new JComboBox<>(new String[] {"database_setup.sql", "Custom Query"});
        scriptSelector.setFont(textFont);
        
        selectorPanel.add(selectLabel);
        selectorPanel.add(scriptSelector);
        
        // Log area
        logArea = new JTextArea();
        logArea.setEditable(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        // Load the default script content
        loadScriptContent("database_setup.sql");
        
        scriptSelector.addActionListener(e -> {
            String selected = (String) scriptSelector.getSelectedItem();
            if ("database_setup.sql".equals(selected)) {
                loadScriptContent(selected);
            } else {
                logArea.setText("-- Enter your custom SQL queries here\n\n-- Example:\nSELECT * FROM voters;");
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        executeButton = new JButton("Execute SQL");
        executeButton.setFont(textFont);
        executeButton.setBackground(primaryColor);
        executeButton.setForeground(Color.WHITE);
        executeButton.setFocusPainted(false);
        
        closeButton = new JButton("Close");
        closeButton.setFont(textFont);
        
        buttonPanel.add(executeButton);
        buttonPanel.add(closeButton);
        
        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(selectorPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event handlers
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL();
            }
        });
        
        closeButton.addActionListener(e -> dispose());
    }
    
    private void loadScriptContent(String filename) {
        try {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            logArea.setText(content.toString());
        } catch (IOException e) {
            logArea.setText("-- Could not load " + filename + "\n-- Error: " + e.getMessage() + 
                          "\n\n-- Enter your SQL queries manually:");
        }
    }
    
    private void executeSQL() {
        String sqlContent = logArea.getText();
        if (sqlContent.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No SQL statements to execute.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection conn = DBManager.getConnection();
            Statement stmt = conn.createStatement();
            
            // Split the SQL content into individual queries
            List<String> queries = splitQueries(sqlContent);
            
            // Execute each query
            int successCount = 0;
            StringBuilder results = new StringBuilder();
            
            for (String query : queries) {
                query = query.trim();
                if (!query.isEmpty()) {
                    try {
                        boolean isResultSet = stmt.execute(query);
                        
                        // For SELECT queries, we'd show the result in the QueryManager
                        if (isResultSet && query.toUpperCase().trim().startsWith("SELECT")) {
                            results.append("Query executed: ").append(query.substring(0, Math.min(40, query.length()))).append("...\n");
                            // We're just indicating it was successful; QueryManager would handle showing the results
                            results.append("SELECT query successful. Use the Custom SQL panel to see results.\n\n");
                        } else {
                            // For non-SELECT queries, just indicate success
                            results.append("Executed: ").append(query.substring(0, Math.min(40, query.length()))).append("...\n");
                            results.append("Success.\n\n");
                        }
                        successCount++;
                    } catch (SQLException ex) {
                        results.append("Error in query: ").append(query.substring(0, Math.min(40, query.length()))).append("...\n");
                        results.append("Error: ").append(ex.getMessage()).append("\n\n");
                    }
                }
            }
            
            // Show results
            JTextArea resultArea = new JTextArea(results.toString());
            resultArea.setEditable(false);
            resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            resultArea.setLineWrap(true);
            resultArea.setWrapStyleWord(true);
            
            JScrollPane scrollPane = new JScrollPane(resultArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Executed " + successCount + " of " + queries.size() + " queries", 
                JOptionPane.INFORMATION_MESSAGE);
            
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error connecting to database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<String> splitQueries(String sqlContent) {
        List<String> queries = new ArrayList<>();
        StringBuilder currentQuery = new StringBuilder();
        
        for (String line : sqlContent.split("\n")) {
            // Skip comments and empty lines
            if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                continue;
            }
            
            currentQuery.append(line).append(" ");
            
            // If the line ends with a semicolon, it's the end of a query
            if (line.trim().endsWith(";")) {
                queries.add(currentQuery.toString());
                currentQuery = new StringBuilder();
            }
        }
        
        // Add any remaining query
        if (currentQuery.length() > 0) {
            queries.add(currentQuery.toString());
        }
        
        return queries;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new DBSetup().setVisible(true);
        });
    }
} 