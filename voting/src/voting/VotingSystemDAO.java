package voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VotingSystemDAO {
    private Connection connection;

    public VotingSystemDAO() {
        this.connection = DBManager.getConnection();
    }

    public void insertVoterDetails(String hashedName, String hashedAge, String hashedVoterId, String party) {
        String sql = "INSERT INTO voters (hashed_name, hashed_age, hashed_voter_id, party_voted_for) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hashedName);
            pstmt.setString(2, hashedAge);
            pstmt.setString(3, hashedVoterId);
            pstmt.setString(4, party);
            
            pstmt.executeUpdate();
            System.out.println("Vote registered successfully!");
        } catch (SQLException e) {
            System.err.println("Error registering vote: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        DBManager.closeConnection();
    }
} 