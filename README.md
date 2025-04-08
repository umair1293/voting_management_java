# Voting System

A Java-based voting system with a GUI interface.

## Prerequisites

- Java JDK 17 or higher
- MySQL Server
- Eclipse IDE (recommended)
- Ant (for building)

## Setup Instructions

1. Create a MySQL database named `voting_system`:
```sql
CREATE DATABASE voting_system;
USE voting_system;

CREATE TABLE votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hashed_name VARCHAR(255) NOT NULL,
    hashed_age VARCHAR(255) NOT NULL,
    hashed_voter_id VARCHAR(255) NOT NULL,
    party VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. Download MySQL Connector/J:
   - Download mysql-connector-j-8.0.33.jar from [MySQL's website](https://dev.mysql.com/downloads/connector/j/)
   - Create a `lib` directory in the project root
   - Place the downloaded JAR file in the `lib` directory

3. Configure Database Connection:
   - Open `src/voting/DBManager.java`
   - Update the database credentials (DB_USER and DB_PASSWORD) to match your MySQL setup

4. Build and Run:
   - Using Ant:
     ```bash
     ant compile
     ant run
     ```
   - Using Eclipse:
     - Import the project into Eclipse
     - Right-click on the project and select "Run As" > "Java Application"
     - Select `voting.VotingSystemGUI` as the main class

## Project Structure

- `src/voting/` - Source code directory
  - `VotingSystemGUI.java` - Main GUI application
  - `DBManager.java` - Database connection manager
  - `VotingSystemDAO.java` - Data Access Object
  - `Admin.java` - Admin panel
- `lib/` - External libraries
- `bin/` - Compiled classes 