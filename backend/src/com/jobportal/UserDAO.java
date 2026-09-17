package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // =========================
    // REGISTER USER
    // =========================

    public boolean registerUser(
            String name,
            String email,
            String password,
            String role) {

        String sql = """
                INSERT INTO users
                (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, role);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error registering user!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // CHECK EMAIL EXISTS
    // =========================

    public boolean emailExists(String email) {

        String sql = """
                SELECT id
                FROM users
                WHERE email = ?
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
                    "Error checking email!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // LOGIN USER
    // =========================

    public User loginUser(
            String email,
            String password) {

        String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ?
                AND password = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    User user = new User();

                    user.setId(
                            resultSet.getInt("id")
                    );

                    user.setName(
                            resultSet.getString("name")
                    );

                    user.setEmail(
                            resultSet.getString("email")
                    );

                    user.setPassword(
                            resultSet.getString("password")
                    );

                    user.setRole(
                            resultSet.getString("role")
                    );

                    return user;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error logging in user!"
            );

            e.printStackTrace();
        }

        return null;
    }
}