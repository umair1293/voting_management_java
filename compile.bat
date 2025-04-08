@echo off
echo Compiling Java files...
mkdir bin 2>nul
javac -cp "D:\Documents\downloads\voting-20250402T112629Z-001\voting\lib\mysql-connector-j-9.2.0.jar" -d bin src\voting\*.java
javac -cp "D:\Documents\downloads\voting-20250402T112629Z-001\voting\lib\mysql-connector-j-9.2.0.jar" -d bin src\module-info.java
echo Compilation complete!
echo.
echo To run the application, use:
echo java -cp "D:\Documents\downloads\voting-20250402T112629Z-001\voting\bin;D:\Documents\downloads\voting-20250402T112629Z-001\voting\lib\mysql-connector-j-9.2.0.jar" voting.VotingSystemGUI
echo.
echo To run the database setup utility directly, use:
echo java -cp "D:\Documents\downloads\voting-20250402T112629Z-001\voting\bin;D:\Documents\downloads\voting-20250402T112629Z-001\voting\lib\mysql-connector-j-9.2.0.jar" voting.DBSetup 