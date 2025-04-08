-- Create the database
CREATE DATABASE IF NOT EXISTS voting_system;
USE voting_system;

-- Create the voters table
CREATE TABLE IF NOT EXISTS voters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hashed_name VARCHAR(255) NOT NULL,
    hashed_age VARCHAR(255) NOT NULL,
    hashed_voter_id VARCHAR(255) NOT NULL,
    party_voted_for VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample queries used in the application

-- 1. Basic queries

-- Get total votes
SELECT COUNT(*) AS total_voters FROM voters;

-- Get votes by party with percentages
SELECT party_voted_for, COUNT(*) AS party_votes,
    ROUND((COUNT(*) * 100) / (SELECT COUNT(*) FROM voters), 2) AS vote_percentage
FROM voters
GROUP BY party_voted_for;

-- Party distribution with percentages
SELECT party_voted_for, COUNT(*) AS count,
    ROUND((COUNT(*) * 100) / (SELECT COUNT(*) FROM voters), 1) AS percentage
FROM voters
GROUP BY party_voted_for;

-- 2. Advanced queries

-- Find duplicated voter IDs (potential fraud)
SELECT hashed_voter_id, COUNT(*) as vote_count
FROM voters
GROUP BY hashed_voter_id
HAVING COUNT(*) > 1;

-- Get votes by timestamp (voting trend analysis)
SELECT DATE(created_at) as vote_date, COUNT(*) as daily_votes
FROM voters
GROUP BY DATE(created_at)
ORDER BY vote_date;

-- Get hourly voting pattern
SELECT HOUR(created_at) as hour_of_day, COUNT(*) as votes_count
FROM voters
GROUP BY HOUR(created_at)
ORDER BY hour_of_day;

-- Find the most recent votes
SELECT id, party_voted_for, created_at
FROM voters
ORDER BY created_at DESC
LIMIT 10;

-- Calculate vote differences between parties
WITH party_counts AS (
    SELECT party_voted_for, COUNT(*) as count
    FROM voters
    GROUP BY party_voted_for
)
SELECT p1.party_voted_for, p2.party_voted_for, 
    p1.count - p2.count as vote_difference
FROM party_counts p1, party_counts p2
WHERE p1.party_voted_for < p2.party_voted_for;

-- Create admin view for dashboard
CREATE OR REPLACE VIEW voting_summary AS
SELECT 
    COUNT(*) as total_votes,
    SUM(CASE WHEN party_voted_for = 'BJP' THEN 1 ELSE 0 END) as bjp_votes,
    SUM(CASE WHEN party_voted_for = 'Congress' THEN 1 ELSE 0 END) as congress_votes,
    SUM(CASE WHEN party_voted_for = 'AAP' THEN 1 ELSE 0 END) as aap_votes,
    MAX(created_at) as last_vote_time
FROM voters;

-- Example of a complex query showing votes per day of week
SELECT 
    DAYNAME(created_at) as day_name,
    COUNT(*) as vote_count,
    GROUP_CONCAT(DISTINCT party_voted_for ORDER BY party_voted_for) as parties
FROM voters
GROUP BY DAYNAME(created_at)
ORDER BY FIELD(day_name, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday');

-- Example of voting time distribution
SELECT 
    CASE 
        WHEN HOUR(created_at) BETWEEN 6 AND 11 THEN 'Morning (6-11)'
        WHEN HOUR(created_at) BETWEEN 12 AND 17 THEN 'Afternoon (12-17)'
        WHEN HOUR(created_at) BETWEEN 18 AND 23 THEN 'Evening (18-23)'
        ELSE 'Night (0-5)'
    END as time_of_day,
    COUNT(*) as vote_count,
    ROUND((COUNT(*) * 100) / (SELECT COUNT(*) FROM voters), 1) as percentage
FROM voters
GROUP BY time_of_day
ORDER BY vote_count DESC;

SELECT * FROM voters;

SELECT party_voted_for, COUNT(*) AS votes 
FROM voters 
GROUP BY party_voted_for;

SELECT DATE(created_at) AS date, COUNT(*) AS votes 
FROM voters 
GROUP BY date; 