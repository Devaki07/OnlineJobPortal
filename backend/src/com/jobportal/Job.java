package com.jobportal;

public class Job {

    private int id;
    private String title;
    private String company;
    private String location;
    private String experience;

    public Job(int id, String title, String company,
               String location, String experience) {

        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.experience = experience;
    }

    // Getters for web server
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getExperience() {
        return experience;
    }

    public void displayJob() {

        System.out.println("Job ID: " + id);
        System.out.println("Job Title: " + title);
        System.out.println("Company: " + company);
        System.out.println("Location: " + location);
        System.out.println("Experience: " + experience);
        System.out.println("-----------------------------");
    }

    public boolean matchesLocation(String searchLocation) {

        return location.toLowerCase()
                .contains(searchLocation.toLowerCase());
    }

    public boolean matchesTitle(String searchText) {

        return title.toLowerCase()
                .contains(searchText.toLowerCase());
    }

    public boolean matchesCompany(String searchCompany) {

        return company.toLowerCase()
                .contains(searchCompany.toLowerCase());
    }

    public boolean matchesExperience(String searchExperience) {

        return experience.toLowerCase()
                .contains(searchExperience.toLowerCase());
    }

    public boolean matchesId(String searchId) {

        return String.valueOf(id).equals(searchId);
    }
}