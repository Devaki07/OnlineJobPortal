package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplicationDAO {

    // =========================
    // CREATE APPLICATIONS TABLE
    // =========================

    public void createTable() {

        String sql = """
                CREATE TABLE IF NOT EXISTS applications (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    job_id INT NOT NULL,
                    applicant_name VARCHAR(100) NOT NULL,
                    applicant_email VARCHAR(100) NOT NULL,
                    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(30) NOT NULL DEFAULT 'Applied',
                    FOREIGN KEY (job_id) REFERENCES jobs(id)
                )
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.executeUpdate();

            System.out.println(
                    "Applications table is ready!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error creating applications table!"
            );

            e.printStackTrace();
        }
    }


    // =========================
    // CHECK DUPLICATE APPLICATION
    // =========================

    public boolean hasAlreadyApplied(
            int jobId,
            String applicantEmail) {

        String sql = """
                SELECT COUNT(*)
                FROM applications
                WHERE job_id = ?
                AND applicant_email = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, jobId);
            statement.setString(2, applicantEmail);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error checking existing application!"
            );

            e.printStackTrace();
        }

        return false;
    }


    // =========================
    // SAVE A JOB APPLICATION
    // =========================

    public boolean applyForJob(
            int jobId,
            String applicantName,
            String applicantEmail) {

        String sql = """
                INSERT INTO applications
                (
                    job_id,
                    applicant_name,
                    applicant_email,
                    status
                )
                VALUES (?, ?, ?, 'Applied')
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, jobId);
            statement.setString(2, applicantName);
            statement.setString(3, applicantEmail);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving application!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // GET APPLICATIONS BY EMAIL
    // =========================

    public ResultSet getApplicationsByEmail(
            Connection connection,
            String applicantEmail)
            throws Exception {

        String sql = """
                SELECT
                    a.id,
                    a.job_id,
                    j.title,
                    j.company,
                    j.location,
                    j.experience,
                    a.applicant_name,
                    a.applicant_email,
                    a.applied_at,
                    a.status
                FROM applications a
                JOIN jobs j
                    ON a.job_id = j.id
                WHERE a.applicant_email = ?
                ORDER BY a.applied_at DESC
                """;

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setString(1, applicantEmail);

        return statement.executeQuery();
    }


    // =====================================================
    // ADMIN - CHECK ADMIN USER
    // =====================================================

    public boolean isAdmin(String email) {

        String sql = """
                SELECT id
                FROM users
                WHERE email = ?
                AND role = 'Admin'
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                return resultSet.next();
            }

        } catch (Exception e) {

            System.out.println(
                    "Error checking admin user!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // ADMIN - GET ALL APPLICATIONS
    // =====================================================

    public ResultSet getAllApplications(
            Connection connection)
            throws Exception {

        String sql = """
                SELECT
                    a.id,
                    a.job_id,
                    j.title,
                    j.company,
                    j.location,
                    j.experience,
                    a.applicant_name,
                    a.applicant_email,
                    a.applied_at,
                    a.status
                FROM applications a
                JOIN jobs j
                    ON a.job_id = j.id
                ORDER BY a.applied_at DESC
                """;

        PreparedStatement statement =
                connection.prepareStatement(sql);

        return statement.executeQuery();
    }


    // =====================================================
    // ADMIN - UPDATE APPLICATION STATUS
    // =====================================================

    public boolean updateApplicationStatus(
            int applicationId,
            String status) {

        String sql = """
                UPDATE applications
                SET status = ?
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, applicationId);

            int rows =
                    statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error updating application status!"
            );

            e.printStackTrace();

            return false;
        }
    }
}