package com.jobportal;

import java.util.Scanner;
import java.util.ArrayList;

public class JobManager {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<String> applications = new ArrayList<>();

        // ================= LOGIN =================

        System.out.println("\n===== LOGIN =====");

        String correctUsername = "Devaki";
        String correctPassword = "1234";

        boolean loginSuccessful = false;

        for (int attempt = 1; attempt <= 3; attempt++) {

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (username.equalsIgnoreCase(correctUsername)
                    && password.equals(correctPassword)) {

                System.out.println("Login successful!");
                loginSuccessful = true;
                break;

            } else {

                System.out.println("Invalid username or password.");
                System.out.println("Attempts remaining: " + (3 - attempt));
            }
        }

        if (!loginSuccessful) {

            System.out.println("Too many failed attempts.");
            System.out.println("Login blocked.");

            scanner.close();
            return;
        }

        // ================= JOBS =================

        Job job1 = new Job(
                1,
                "Junior Java Developer",
                "Tech Solutions",
                "Hyderabad",
                "Fresher"
        );

        Job job2 = new Job(
                2,
                "Software Engineer",
                "Digital Technologies",
                "Bangalore",
                "0-2 Years"
        );

        Job job3 = new Job(
                3,
                "Web Developer",
                "Web Solutions",
                "Chennai",
                "Fresher"
        );

        Job job4 = new Job(
                4,
                "Java Developer",
                "ABC Technologies",
                "Bangalore",
                "Fresher"
        );

        Job job5 = new Job(
                5,
                "Software Engineer",
                "Tech Innovations",
                "Chennai",
                "Fresher"
        );

        // ================= MAIN MENU =================

        int choice;

        while (true) {

            System.out.println("\n===== ONLINE JOB PORTAL =====");
            System.out.println("1. Search Jobs");
            System.out.println("2. View All Jobs");
            System.out.println("3. Apply for Job");
            System.out.println("4. View My Applications");
            System.out.println("5. Logout");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            // ================= SEARCH JOBS =================

            if (choice == 1) {

                System.out.print(
                        "Enter job title, location, company, experience or Job ID: "
                );

                String searchText = scanner.nextLine();

                System.out.println("\n===== SEARCH RESULTS =====");
                System.out.println("Search: " + searchText);

                int resultCount = 0;

                if (job1.matchesLocation(searchText)
                        || job1.matchesTitle(searchText)
                        || job1.matchesCompany(searchText)
                        || job1.matchesExperience(searchText)
                        || job1.matchesId(searchText)) {

                    job1.displayJob();
                    resultCount++;
                }

                if (job2.matchesLocation(searchText)
                        || job2.matchesTitle(searchText)
                        || job2.matchesCompany(searchText)
                        || job2.matchesExperience(searchText)
                        || job2.matchesId(searchText)) {

                    job2.displayJob();
                    resultCount++;
                }

                if (job3.matchesLocation(searchText)
                        || job3.matchesTitle(searchText)
                        || job3.matchesCompany(searchText)
                        || job3.matchesExperience(searchText)
                        || job3.matchesId(searchText)) {

                    job3.displayJob();
                    resultCount++;
                }

                if (job4.matchesLocation(searchText)
                        || job4.matchesTitle(searchText)
                        || job4.matchesCompany(searchText)
                        || job4.matchesExperience(searchText)
                        || job4.matchesId(searchText)) {

                    job4.displayJob();
                    resultCount++;
                }

                if (job5.matchesLocation(searchText)
                        || job5.matchesTitle(searchText)
                        || job5.matchesCompany(searchText)
                        || job5.matchesExperience(searchText)
                        || job5.matchesId(searchText)) {

                    job5.displayJob();
                    resultCount++;
                }

                if (resultCount == 0) {

                    System.out.println("No jobs found matching your search.");

                } else {

                    System.out.println("Found " + resultCount + " job(s).");
                }

            // ================= VIEW ALL JOBS =================

            } else if (choice == 2) {

                System.out.println("\n===== ALL JOBS =====");

                job1.displayJob();
                job2.displayJob();
                job3.displayJob();
                job4.displayJob();
                job5.displayJob();

            // ================= APPLY FOR JOB =================

            } else if (choice == 3) {

                System.out.print("Enter Job ID to apply: ");

                int jobId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter your name: ");

                String applicantName = scanner.nextLine();

                System.out.print(
                        "Are you sure you want to apply? (yes/no): "
                );

                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("no")) {

                    System.out.println("Application cancelled.");
                    continue;
                }

                boolean alreadyApplied = false;

                for (String application : applications) {

                    if (application.startsWith(
                            applicantName + " - Job ID: " + jobId + " - ")) {

                        alreadyApplied = true;
                        break;
                    }
                }

                if (alreadyApplied) {

                    System.out.println(
                            "You have already applied for this job."
                    );

                    continue;
                }

                // Job 1

                if (jobId == 1) {

                    job1.displayJob();

                    System.out.println(
                            "Application submitted successfully!"
                    );

                    System.out.println("Applicant: " + applicantName);
                    System.out.println(
                            "Applied Job: Junior Java Developer"
                    );
                    System.out.println("Company: Tech Solutions");

                    applications.add(
                            applicantName
                            + " - Job ID: 1 - Junior Java Developer - Tech Solutions"
                    );

                // Job 2

                } else if (jobId == 2) {

                    job2.displayJob();

                    System.out.println(
                            "Application submitted successfully!"
                    );

                    System.out.println("Applicant: " + applicantName);
                    System.out.println(
                            "Applied Job: Software Engineer"
                    );
                    System.out.println(
                            "Company: Digital Technologies"
                    );

                    applications.add(
                            applicantName
                            + " - Job ID: 2 - Software Engineer - Digital Technologies"
                    );

                // Job 3

                } else if (jobId == 3) {

                    job3.displayJob();

                    System.out.println(
                            "Application submitted successfully!"
                    );

                    System.out.println("Applicant: " + applicantName);
                    System.out.println("Applied Job: Web Developer");
                    System.out.println("Company: Web Solutions");

                    applications.add(
                            applicantName
                            + " - Job ID: 3 - Web Developer - Web Solutions"
                    );

                // Job 4

                } else if (jobId == 4) {

                    job4.displayJob();

                    System.out.println(
                            "Application submitted successfully!"
                    );

                    System.out.println("Applicant: " + applicantName);
                    System.out.println("Applied Job: Java Developer");
                    System.out.println("Company: ABC Technologies");

                    applications.add(
                            applicantName
                            + " - Job ID: 4 - Java Developer - ABC Technologies"
                    );

                // Job 5

                } else if (jobId == 5) {

                    job5.displayJob();

                    System.out.println(
                            "Application submitted successfully!"
                    );

                    System.out.println("Applicant: " + applicantName);
                    System.out.println(
                            "Applied Job: Software Engineer"
                    );
                    System.out.println("Company: Tech Innovations");

                    applications.add(
                            applicantName
                            + " - Job ID: 5 - Software Engineer - Tech Innovations"
                    );

                } else {

                    System.out.println("Invalid Job ID.");
                }

            // ================= VIEW APPLICATIONS =================

            } else if (choice == 4) {

                System.out.println("\n===== MY APPLICATIONS =====");

                if (applications.isEmpty()) {

                    System.out.println("No applications found.");

                } else {

                    for (String application : applications) {

                        System.out.println(application);
                    }
                }

            // ================= LOGOUT =================

            } else if (choice == 5) {

                System.out.println("Logging out...");
                break;

            // ================= EXIT =================

            } else if (choice == 6) {

                System.out.println(
                        "Thank you for using Online Job Portal!"
                );

                break;

            // ================= INVALID CHOICE =================

            } else {

                System.out.println(
                        "Invalid choice. Please enter 1, 2, 3, 4, 5, or 6"
                );
            }
        }

        scanner.close();
    }
}