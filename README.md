# Online Job Portal

A full-stack Online Job Portal developed using Java, MySQL, HTML, CSS, and JavaScript.

## Features

### Job Seeker
- User registration
- User login
- Browse available jobs
- Apply for jobs
- View applied jobs
- Track application status
- View profile
- Logout

### Admin
- Admin dashboard
- View all job applications
- View application statistics
- Update application status
- Track applicant information

## Technologies Used

- Java
- MySQL
- HTML5
- CSS3
- JavaScript
- REST APIs
- MySQL Connector/J

## Project Structure

```text
OnlineJobPortal/
│
├── backend/
│   ├── lib/
│   │   └── mysql-connector-j-26.7.0.jar
│   │
│   └── src/
│       └── com/
│           └── jobportal/
│               ├── ApplicationDAO.java
│               ├── DatabaseConnection.java
│               ├── DatabaseTest.java
│               ├── Job.java
│               ├── JobDAO.java
│               ├── JobDAOTest.java
│               ├── JobManager.java
│               ├── JobServer.java
│               ├── User.java
│               └── UserDAO.java
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