package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS jobs (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    title VARCHAR(100),
                    company VARCHAR(100),
                    location VARCHAR(100),
                    experience VARCHAR(50)
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();

            System.out.println("Jobs table is ready!");

        } catch (Exception e) {

            System.out.println("Error creating jobs table!");
            e.printStackTrace();
        }
    }

    public void insertSampleJobs() {

        String sql = """
                INSERT INTO jobs (title, company, location, experience)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String[][] jobs = {
                {"Junior Java Developer", "Tech Solutions", "Hyderabad", "Fresher"},
                {"Software Engineer", "Digital Technologies", "Bangalore", "0-2 Years"},
                {"Web Developer", "Web Solutions", "Chennai", "Fresher"},
                {"Java Developer", "ABC Technologies", "Bangalore", "Fresher"},
                {"Software Engineer", "Tech Innovations", "Chennai", "Fresher"}
            };

            for (String[] job : jobs) {

                statement.setString(1, job[0]);
                statement.setString(2, job[1]);
                statement.setString(3, job[2]);
                statement.setString(4, job[3]);

                statement.executeUpdate();
            }

            System.out.println("5 sample jobs inserted successfully!");

        } catch (Exception e) {

            System.out.println("Error inserting sample jobs!");
            e.printStackTrace();
        }
    }

    public List<Job> getAllJobs() {

        List<Job> jobs = new ArrayList<>();

        String sql =
                "SELECT id, title, company, location, experience FROM jobs";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Job job = new Job(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("company"),
                    resultSet.getString("location"),
                    resultSet.getString("experience")
                );

                jobs.add(job);
            }

        } catch (Exception e) {

            System.out.println("Error loading jobs from database!");
            e.printStackTrace();
        }

        return jobs;
    }
}
