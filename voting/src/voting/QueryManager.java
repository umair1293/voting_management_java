package voting;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class QueryManager {
    
    /**
     * Get total number of votes
     */
    public static int getTotalVotes() {
        int totalVotes = 0;
        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total_voters FROM voters");
            
            if (resultSet.next()) {
                totalVotes = resultSet.getInt("total_voters");
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalVotes;
    }
    
    /**
     * Get votes by party with percentages
     */
    public static void getVotesByParty(JFrame parentFrame) {
        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            
            String query = "SELECT party_voted_for, COUNT(*) AS party_votes, " +
                          "ROUND((COUNT(*) * 100) / (SELECT COUNT(*) FROM voters), 2) AS vote_percentage " +
                          "FROM voters GROUP BY party_voted_for";
            ResultSet resultSet = statement.executeQuery(query);
            
            // Create table model
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Party");
            columnNames.add("Votes");
            columnNames.add("Percentage");
            
            Vector<Vector<Object>> data = new Vector<>();
            
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("party_voted_for"));
                row.add(resultSet.getInt("party_votes"));
                row.add(resultSet.getDouble("vote_percentage") + "%");
                data.add(row);
            }
            
            // Create and display results in a dialog
            JDialog resultDialog = new JDialog(parentFrame, "Voting Results", true);
            resultDialog.setSize(500, 300);
            resultDialog.setLocationRelativeTo(parentFrame);
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable resultTable = new JTable(model);
            resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            resultTable.setRowHeight(25);
            
            resultDialog.add(new JScrollPane(resultTable));
            resultDialog.setVisible(true);
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error retrieving vote data: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Check for duplicate voter IDs (potential fraud)
     */
    public static void checkDuplicateVoters(JFrame parentFrame) {
        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            
            String query = "SELECT hashed_voter_id, COUNT(*) as vote_count " +
                          "FROM voters " +
                          "GROUP BY hashed_voter_id " +
                          "HAVING COUNT(*) > 1";
            ResultSet resultSet = statement.executeQuery(query);
            
            if (!resultSet.isBeforeFirst()) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "No duplicate voters found.", 
                    "Fraud Check", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Create table model
                Vector<String> columnNames = new Vector<>();
                columnNames.add("Hashed Voter ID");
                columnNames.add("Vote Count");
                
                Vector<Vector<Object>> data = new Vector<>();
                
                while (resultSet.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(resultSet.getString("hashed_voter_id"));
                    row.add(resultSet.getInt("vote_count"));
                    data.add(row);
                }
                
                // Create and display results in a dialog
                JDialog fraudDialog = new JDialog(parentFrame, "Potential Fraud Detected", true);
                fraudDialog.setSize(500, 300);
                fraudDialog.setLocationRelativeTo(parentFrame);
                
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                JTable fraudTable = new JTable(model);
                fraudTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                fraudTable.setRowHeight(25);
                
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new JLabel("Warning: Duplicate voter IDs detected!", JLabel.CENTER), BorderLayout.NORTH);
                panel.add(new JScrollPane(fraudTable), BorderLayout.CENTER);
                
                fraudDialog.add(panel);
                fraudDialog.setVisible(true);
            }
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error checking for duplicate voters: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Get voting trend by date
     */
    public static void getVotingTrend(JFrame parentFrame) {
        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            
            String query = "SELECT DATE(created_at) as vote_date, COUNT(*) as daily_votes " +
                          "FROM voters " +
                          "GROUP BY DATE(created_at) " +
                          "ORDER BY vote_date";
            ResultSet resultSet = statement.executeQuery(query);
            
            // Create table model
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Date");
            columnNames.add("Votes");
            
            Vector<Vector<Object>> data = new Vector<>();
            
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("vote_date"));
                row.add(resultSet.getInt("daily_votes"));
                data.add(row);
            }
            
            // Create and display results in a dialog
            JDialog trendDialog = new JDialog(parentFrame, "Voting Trends", true);
            trendDialog.setSize(400, 300);
            trendDialog.setLocationRelativeTo(parentFrame);
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable trendTable = new JTable(model);
            trendTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            trendTable.setRowHeight(25);
            
            trendDialog.add(new JScrollPane(trendTable));
            trendDialog.setVisible(true);
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error retrieving voting trends: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Get the most recent votes
     */
    public static void getRecentVotes(JFrame parentFrame, int limit) {
        try {
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT id, party_voted_for, created_at " +
                "FROM voters " +
                "ORDER BY created_at DESC " +
                "LIMIT ?"
            );
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            
            // Create table model
            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Party");
            columnNames.add("Timestamp");
            
            Vector<Vector<Object>> data = new Vector<>();
            
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("party_voted_for"));
                row.add(resultSet.getTimestamp("created_at"));
                data.add(row);
            }
            
            // Create and display results in a dialog
            JDialog recentDialog = new JDialog(parentFrame, "Recent Votes", true);
            recentDialog.setSize(500, 300);
            recentDialog.setLocationRelativeTo(parentFrame);
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable recentTable = new JTable(model);
            recentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            recentTable.setRowHeight(25);
            
            recentDialog.add(new JScrollPane(recentTable));
            recentDialog.setVisible(true);
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error retrieving recent votes: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Run custom SQL query and display results
     */
    public static void runCustomQuery(JFrame parentFrame, String sqlQuery) {
        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            
            // Get column metadata
            int columnCount = resultSet.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(resultSet.getMetaData().getColumnName(i));
            }
            
            // Get data
            Vector<Vector<Object>> data = new Vector<>();
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                data.add(row);
            }
            
            // Create and display results in a dialog
            JDialog queryDialog = new JDialog(parentFrame, "Custom Query Results", true);
            queryDialog.setSize(600, 400);
            queryDialog.setLocationRelativeTo(parentFrame);
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable queryTable = new JTable(model);
            queryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            queryTable.setRowHeight(25);
            
            queryDialog.add(new JScrollPane(queryTable));
            queryDialog.setVisible(true);
            
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, 
                "Error executing query: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 