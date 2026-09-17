package com.jobportal;

import java.util.List;

public class JobDAOTest {

    public static void main(String[] args) {

        JobDAO jobDAO = new JobDAO();

        // Make sure the table exists
        jobDAO.createTable();

        // Load existing jobs from MySQL
        List<Job> jobs = jobDAO.getAllJobs();

        System.out.println("Jobs loaded from MySQL: " + jobs.size());

        for (Job job : jobs) {
            job.displayJob();
        }
    }
}
