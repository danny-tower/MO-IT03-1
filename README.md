# MotorPH Employee App

This is a simple Java-based payroll management and employee app system built for the MotorPH IT03 project.

**Project Overview**
The MotorPH Employee Management System represents a Java-based desktop application specifically designed to efficiently manage employee payroll, attendance, and salary components for MotorPH. This system features a graphical user interface (GUI) developed using Java Swing, which facilitates the streamlining of various human resources tasks, including attendance logging, payroll calculation, and reporting.

**Project Goals**
-Automate Payroll Processing
Eliminate manual computation of salaries by automating the calculation of gross pay, deductions (tax, SSS, etc.), overtime, and bonuses based on attendance data.
-Simplify Employee Attendance Tracking
Provide a user-friendly interface for logging employee time-in and time-out records to ensure accurate and real-time attendance data.
-Enable Accurate and Transparent Reporting
Generate payroll reports and breakdowns (daily, weekly, monthly) that are easy to interpret and can be exported as CSV or PDF for official records.
-Ensure Data Security and Access Control
Protect payroll data with a secure login system, allowing only authorized users (e.g., HR/Admin) to access and manage sensitive employee information.
-Enhance Usability Through GUI Design
Deliver a desktop-based system with a clean, intuitive Java Swing interface tailored for HR or payroll personnel without requiring technical expertise.
-Support Modular and Maintainable Development
Design the application in a modular fashion to allow future enhancements such as database integration, multi-user access, and audit logs.

Key Features

Attendance Logging – Tracks employee check-in/out times

Payroll Calculation – Automates salary computation based on attendance and pay rules

Breakdown Display – Shows daily hours worked, deductions, bonuses

 Export Reports – Generates payroll summaries in CSV or PDF

 Swing GUI Interface – Easy-to-use desktop interface for non-technical users

## Features

- Java Swing GUI for payroll
- Attendance logging
- overtime, tax, deduction calculations
- Admin authentication

##  Project Structure
motorphemployeeapp/
├── src/
│ └── your Java classes
├── README.md

Initial Architecture Diagram (Text Description)
+------------------------+
|      User/Admin       |
+-----------+------------+
            |
            v
+-----------+------------+
|       Java Swing GUI   |
| (Login, Attendance,    |
|  Payroll Screens)      |
+-----------+------------+
            |
            v
+-----------+------------+
|   Application Logic    |
| (Controllers/Services) |
+-----------+------------+
            |
     +------+------+
     |             |
     v             v
+----+----+    +---+-----------------+
| Payroll |    | Attendance Manager  |
| Engine  |    | (Time-in/out Logs)  |
+----+----+    +---+-----------------+
     |              |
     v              v
+-------------------+---------------+
|   In-Memory Data Store or Files   |
|  (CSV, TXT, or Serialized Objects)|
+-----------------------------------+


