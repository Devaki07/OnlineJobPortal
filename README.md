# Online Job Portal

A full-stack Online Job Portal developed using Java, MySQL, HTML, CSS, and JavaScript.

The application allows candidates to register, log in, browse available jobs, apply for jobs, track application status, and manage their profile. An admin dashboard is provided to view applications and update application statuses.

## Features

### Candidate Features
- User registration
- User login and logout
- Browse available jobs
- Search jobs
- Apply for jobs
- Prevent duplicate job applications
- View submitted applications
- Track application status
- View profile information

### Admin Features
- Admin dashboard
- View job applications
- View application statistics
- Update application status
- Candidate status updates are reflected in the candidate dashboard

## Technologies Used

- **Backend:** Java
- **Database:** MySQL
- **Frontend:** HTML5, CSS3, JavaScript
- **Database Connectivity:** JDBC
- **Server:** Java HTTP Server
- **Driver:** MySQL Connector/J
- **Version Control:** Git & GitHub

## Project Structure

```text
OnlineJobPortal/
│
├── backend/
│   ├── lib/
│   │   └── mysql-connector-j-26.7.0.jar
│   │
│   └── src/
│       ├── Main.java
│       └── com/jobportal/
│           ├── ApplicationDAO.java
│           ├── DatabaseConnection.java
│           ├── DatabaseTest.java
│           ├── Job.java
│           ├── JobDAO.java
│           ├── JobDAOTest.java
│           ├── JobManager.java
│           ├── JobServer.java
│           ├── User.java
│           └── UserDAO.java
│
├── frontend/
│   ├── admin.html
│   ├── index.html
│   ├── jobs.html
│   ├── login.html
│   ├── my-applications.html
│   ├── profile.html
│   ├── register.html
│   └── style.css
│
├── .gitignore
└── README.md
