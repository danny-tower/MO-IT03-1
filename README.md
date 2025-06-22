# MotorPH Employee App (GUI Version)

## Overview  
This is a Java Swing desktop application that allows you to manage Employees, Payroll, and Leave Requests using full Create/Read/Update/Delete (CRUD) functionality. A simple login screen secures access. All data is stored in plain CSV files:  
- `accounts.csv`  
- `employees.csv`  
- `payroll.csv`  
- `leaverequests.csv`  

---

## Features

**1. Login Screen**  
- Credentials are loaded from `accounts.csv` (username,password)  
  - Default: `admin,admin123`  
- If login fails or is canceled, the application exits.

**2. Employees Tab**  
- **View:** displays all employees (ID, Name, Position)  
- **Add:** enter ID, Name, Position and click **Add**  
- **Update:** select a row, click **Update**, edit fields, and save  
- **Delete:** select a row and click **Delete** to remove that employee

**3. Payroll Tab**  
- **View:** displays all payroll entries (Employee ID, Amount)  
- **Add:** enter Employee ID and Amount and click **Add**  
- **Update:** select a row, click **Update**, enter new amount, and save  
- **Delete:** select a row and click **Delete** to remove that entry

**4. Leave Requests Tab**  
- **View:** displays all leave requests (Employee ID, Start Date, End Date, Reason)  
- **Submit:** fill in fields and click **Submit** to add a request  
- **Update:** select a row, click **Update**, edit dates/reason, and save  
- **Delete:** select a row and click **Delete** to remove that request

---

## Prerequisites  
- Java Development Kit (JDK) 8 or higher installed  
- A command-line terminal (PowerShell, CMD, bash, etc.)

**##Objectives**
- To automate payroll processing and reduce manual computation errors
- To track and log employee attendance efficiently
- To generate printable payroll reports and summaries
- To ensure secure and role-specific access for system users

**##Technologies Used**
- **Programming Language:** Java
- **GUI Framework:** Java Swing
- **Database:** MySQL or SQLite
- **Report Tools:** iText for PDF generation, Apache POI (optional for Excel export)

---
****USER ROLES****
| Role                  | Responsibilities                                      |
| --------------------- | ----------------------------------------------------- |
| **Admin**             | Full access to all modules, user and data management  |
| **HR Staff**          | Manage employee profiles and schedules                |
| **Finance & Payroll** | Compute salaries, generate reports, manage deductions |
| **Department Head**   | View and approve employee attendance and overtime     |
| **Employee**          | View personal attendance and payslip                  |

---

## How to Compile & Run

```bash
cd path/to/MO-IT03
javac -d . motorphemployeeapp/src/com/motorph/employeeapp/*.java
java com.motorph.employeeapp.AppGUI

Compile all source files with -d . to mirror package structure.

Run the main class.

Login using credentials from accounts.csv (default admin/admin123).

CSV Files
accounts.csv
username,password
admin,admin123

employees.csv
ID,Name,Position

payroll.csv
EmployeeID,Amount

leaverequests.csv
EmployeeID,StartDate,EndDate,Reason

Project Structure
MO-IT03/
├ .github/
├ motorphemployeeapp/
│ └ src/com/motorph/employeeapp/
│ ├ AppGUI.java
│ ├ LoginDialog.java
│ ├ Department.java
│ ├ ValidatedEmployeeGUI.java
│ ├ ValidatedPayrollGUI.java
│ └ ValidatedLeaveRequestGUI.java
├ accounts.csv
├ employees.csv
├ payroll.csv
├ leaverequests.csv
└ README.md
---

**Academic Purpose**
This project was developed as part of our coursework for Computer Programming 2, under the guidance of Mr. John Ray Hernani, and is intended to demonstrate knowledge in software engineering, database integration, and desktop application development.


Author / Contact
Ghian Renzen Arboleda
lr.grarboleda@mmdc.mcl.edu.ph

Catherine Kate Plenos
lr.ckplenos@mmdc.mcl.edu.ph
