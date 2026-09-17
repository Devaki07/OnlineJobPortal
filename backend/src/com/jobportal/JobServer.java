package com.jobportal;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class JobServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0);

        server.createContext("/register", JobServer::handleRegister);
        server.createContext("/login", JobServer::handleLogin);
        server.createContext("/jobs", JobServer::handleJobs);
        server.createContext("/apply", JobServer::handleApply);
        server.createContext("/applications", JobServer::handleApplications);

        server.createContext(
                "/admin/applications",
                JobServer::handleAdminApplications
        );

        server.createContext(
                "/admin/status",
                JobServer::handleAdminStatus
        );

        server.setExecutor(null);
        server.start();

        System.out.println("Job Portal Server started!");
        System.out.println("Register API: http://localhost:8080/register");
        System.out.println("Login API: http://localhost:8080/login");
        System.out.println("Jobs API: http://localhost:8080/jobs");
        System.out.println("Apply API: http://localhost:8080/apply");
        System.out.println("Applications API: http://localhost:8080/applications");
        System.out.println("Admin Applications API: http://localhost:8080/admin/applications");
        System.out.println("Admin Status API: http://localhost:8080/admin/status");
    }


    // =====================================================
    // POST /register
    // =====================================================

    private static void handleRegister(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Registration request: " + body);

            String name = extractString(body, "name");
            String email = extractString(body, "email");
            String password = extractString(body, "password");
            String role = extractString(body, "role");

            if (name.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || role.isEmpty()) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Please fill all fields\"}"
                );
                return;
            }

            UserDAO userDAO = new UserDAO();

            if (userDAO.emailExists(email)) {

                sendResponse(
                        exchange,
                        409,
                        "{\"success\":false,\"message\":\"Email already registered\"}"
                );
                return;
            }

            boolean success = userDAO.registerUser(
                    name,
                    email,
                    password,
                    role
            );

            if (success) {

                sendResponse(
                        exchange,
                        200,
                        "{\"success\":true,\"message\":\"Account created successfully!\"}"
                );

            } else {

                sendResponse(
                        exchange,
                        500,
                        "{\"success\":false,\"message\":\"Registration failed\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Server error\"}"
            );
        }
    }


    // =====================================================
    // POST /login
    // =====================================================

    private static void handleLogin(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Login request: " + body);

            String email = extractString(body, "email");
            String password = extractString(body, "password");

            if (email.isEmpty() || password.isEmpty()) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Email and password are required\"}"
                );
                return;
            }

            UserDAO userDAO = new UserDAO();

            User user = userDAO.loginUser(
                    email,
                    password
            );

            if (user != null) {

                StringBuilder json = new StringBuilder();

                json.append("{");
                json.append("\"success\":true,");
                json.append("\"message\":\"Login successful!\",");
                json.append("\"user\":{");

                json.append("\"id\":")
                        .append(user.getId())
                        .append(",");

                json.append("\"name\":\"")
                        .append(escapeJson(user.getName()))
                        .append("\",");

                json.append("\"email\":\"")
                        .append(escapeJson(user.getEmail()))
                        .append("\",");

                json.append("\"role\":\"")
                        .append(escapeJson(user.getRole()))
                        .append("\"");

                json.append("}");
                json.append("}");

                sendResponse(
                        exchange,
                        200,
                        json.toString()
                );

            } else {

                sendResponse(
                        exchange,
                        401,
                        "{\"success\":false,\"message\":\"Invalid email or password\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Server error\"}"
            );
        }
    }


    // =====================================================
    // GET /jobs
    // =====================================================

    private static void handleJobs(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            JobDAO jobDAO = new JobDAO();

            List<Job> jobs = jobDAO.getAllJobs();

            StringBuilder json = new StringBuilder();

            json.append("[");

            for (int i = 0; i < jobs.size(); i++) {

                Job job = jobs.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{");

                json.append("\"id\":")
                        .append(job.getId())
                        .append(",");

                json.append("\"title\":\"")
                        .append(escapeJson(job.getTitle()))
                        .append("\",");

                json.append("\"company\":\"")
                        .append(escapeJson(job.getCompany()))
                        .append("\",");

                json.append("\"location\":\"")
                        .append(escapeJson(job.getLocation()))
                        .append("\",");

                json.append("\"experience\":\"")
                        .append(escapeJson(job.getExperience()))
                        .append("\"");

                json.append("}");
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Unable to load jobs\"}"
            );
        }
    }


    // =====================================================
    // POST /apply
    // =====================================================

    private static void handleApply(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println("Application request: " + body);

            int jobId = extractInt(body, "jobId");

            String applicantName =
                    extractString(body, "applicantName");

            String applicantEmail =
                    extractString(body, "applicantEmail");

            if (jobId <= 0
                    || applicantName.isEmpty()
                    || applicantEmail.isEmpty()) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Invalid application data\"}"
                );
                return;
            }

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            boolean alreadyApplied =
                    applicationDAO.hasAlreadyApplied(
                            jobId,
                            applicantEmail
                    );

            if (alreadyApplied) {

                sendResponse(
                        exchange,
                        409,
                        "{\"success\":false,\"message\":\"You have already applied for this job!\"}"
                );
                return;
            }

            boolean success =
                    applicationDAO.applyForJob(
                            jobId,
                            applicantName,
                            applicantEmail
                    );

            if (success) {

                sendResponse(
                        exchange,
                        200,
                        "{\"success\":true,\"message\":\"Application submitted successfully!\"}"
                );

            } else {

                sendResponse(
                        exchange,
                        500,
                        "{\"success\":false,\"message\":\"Failed to save application\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Server error\"}"
            );
        }
    }


    // =====================================================
    // GET /applications?email=
    // =====================================================

    private static void handleApplications(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String query =
                    exchange.getRequestURI().getQuery();

            if (query == null || !query.startsWith("email=")) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Email is required\"}"
                );
                return;
            }

            String email =
                    URLDecoder.decode(
                            query.substring(6),
                            StandardCharsets.UTF_8
                    );

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            boolean first = true;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    ResultSet resultSet =
                            applicationDAO.getApplicationsByEmail(
                                    connection,
                                    email
                            )
            ) {

                while (resultSet.next()) {

                    if (!first) {
                        json.append(",");
                    }

                    json.append("{");

                    json.append("\"id\":")
                            .append(resultSet.getInt("id"))
                            .append(",");

                    json.append("\"jobId\":")
                            .append(resultSet.getInt("job_id"))
                            .append(",");

                    json.append("\"title\":\"")
                            .append(escapeJson(
                                    resultSet.getString("title")
                            ))
                            .append("\",");

                    json.append("\"company\":\"")
                            .append(escapeJson(
                                    resultSet.getString("company")
                            ))
                            .append("\",");

                    json.append("\"location\":\"")
                            .append(escapeJson(
                                    resultSet.getString("location")
                            ))
                            .append("\",");

                    json.append("\"experience\":\"")
                            .append(escapeJson(
                                    resultSet.getString("experience")
                            ))
                            .append("\",");

                    json.append("\"applicantName\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applicant_name")
                            ))
                            .append("\",");

                    json.append("\"applicantEmail\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applicant_email")
                            ))
                            .append("\",");

                    json.append("\"appliedAt\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applied_at")
                            ))
                            .append("\",");

                    json.append("\"status\":\"")
                            .append(escapeJson(
                                    resultSet.getString("status")
                            ))
                            .append("\"");

                    json.append("}");

                    first = false;
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Unable to load applications\"}"
            );
        }
    }


    // =====================================================
    // GET /admin/applications?email=
    // =====================================================

    private static void handleAdminApplications(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String query =
                    exchange.getRequestURI().getQuery();

            if (query == null || !query.startsWith("email=")) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Admin email is required\"}"
                );
                return;
            }

            String adminEmail =
                    URLDecoder.decode(
                            query.substring(6),
                            StandardCharsets.UTF_8
                    );

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            if (!applicationDAO.isAdmin(adminEmail)) {

                sendResponse(
                        exchange,
                        403,
                        "{\"success\":false,\"message\":\"Admin access required\"}"
                );
                return;
            }

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            boolean first = true;

            try (
                    Connection connection =
                            DatabaseConnection.getConnection();

                    ResultSet resultSet =
                            applicationDAO.getAllApplications(
                                    connection
                            )
            ) {

                while (resultSet.next()) {

                    if (!first) {
                        json.append(",");
                    }

                    json.append("{");

                    json.append("\"id\":")
                            .append(resultSet.getInt("id"))
                            .append(",");

                    json.append("\"jobId\":")
                            .append(resultSet.getInt("job_id"))
                            .append(",");

                    json.append("\"title\":\"")
                            .append(escapeJson(
                                    resultSet.getString("title")
                            ))
                            .append("\",");

                    json.append("\"company\":\"")
                            .append(escapeJson(
                                    resultSet.getString("company")
                            ))
                            .append("\",");

                    json.append("\"location\":\"")
                            .append(escapeJson(
                                    resultSet.getString("location")
                            ))
                            .append("\",");

                    json.append("\"experience\":\"")
                            .append(escapeJson(
                                    resultSet.getString("experience")
                            ))
                            .append("\",");

                    json.append("\"applicantName\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applicant_name")
                            ))
                            .append("\",");

                    json.append("\"applicantEmail\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applicant_email")
                            ))
                            .append("\",");

                    json.append("\"appliedAt\":\"")
                            .append(escapeJson(
                                    resultSet.getString("applied_at")
                            ))
                            .append("\",");

                    json.append("\"status\":\"")
                            .append(escapeJson(
                                    resultSet.getString("status")
                            ))
                            .append("\"");

                    json.append("}");

                    first = false;
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Unable to load admin applications\"}"
            );
        }
    }


    // =====================================================
    // POST /admin/status
    // =====================================================

    private static void handleAdminStatus(
            HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "{\"success\":false,\"message\":\"Method not allowed\"}"
            );
            return;
        }

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println(
                    "Admin status request: " + body
            );

            int applicationId =
                    extractInt(body, "applicationId");

            String adminEmail =
                    extractString(body, "adminEmail");

            String status =
                    extractString(body, "status");

            if (applicationId <= 0
                    || adminEmail.isEmpty()
                    || status.isEmpty()) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Invalid admin status data\"}"
                );
                return;
            }

            if (!isValidStatus(status)) {

                sendResponse(
                        exchange,
                        400,
                        "{\"success\":false,\"message\":\"Invalid application status\"}"
                );
                return;
            }

            ApplicationDAO applicationDAO =
                    new ApplicationDAO();

            if (!applicationDAO.isAdmin(adminEmail)) {

                sendResponse(
                        exchange,
                        403,
                        "{\"success\":false,\"message\":\"Admin access required\"}"
                );
                return;
            }

            boolean success =
                    applicationDAO.updateApplicationStatus(
                            applicationId,
                            status
                    );

            if (success) {

                sendResponse(
                        exchange,
                        200,
                        "{\"success\":true,\"message\":\"Application status updated successfully!\"}"
                );

            } else {

                sendResponse(
                        exchange,
                        404,
                        "{\"success\":false,\"message\":\"Application not found\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    "{\"success\":false,\"message\":\"Unable to update application status\"}"
            );
        }
    }


    // =====================================================
    // VALID STATUS
    // =====================================================

    private static boolean isValidStatus(
            String status) {

        return status.equals("Applied")
                || status.equals("Under Review")
                || status.equals("Shortlisted")
                || status.equals("Selected")
                || status.equals("Rejected");
    }


    // =====================================================
    // JSON HELPERS
    // =====================================================

    private static int extractInt(
            String json,
            String key) {

        try {

            String search =
                    "\"" + key + "\":";

            int start =
                    json.indexOf(search);

            if (start == -1) {
                return -1;
            }

            start += search.length();

            int end = start;

            while (end < json.length()
                    && Character.isDigit(
                            json.charAt(end))) {

                end++;
            }

            return Integer.parseInt(
                    json.substring(start, end)
            );

        } catch (Exception e) {

            return -1;
        }
    }


    private static String extractString(
            String json,
            String key) {

        try {

            String search =
                    "\"" + key + "\":\"";

            int start =
                    json.indexOf(search);

            if (start == -1) {
                return "";
            }

            start += search.length();

            int end =
                    json.indexOf("\"", start);

            if (end == -1) {
                return "";
            }

            return json.substring(
                    start,
                    end
            );

        } catch (Exception e) {

            return "";
        }
    }


    private static String escapeJson(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }


    // =====================================================
    // CORS
    // =====================================================

    private static void addCorsHeaders(
            HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );
    }


    // =====================================================
    // SEND RESPONSE
    // =====================================================

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (
                OutputStream outputStream =
                        exchange.getResponseBody()
        ) {

            outputStream.write(bytes);
        }
    }
}