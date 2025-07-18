# MotorPH Employee Management System

## Overview  
This is a Java Swing desktop application for managing employee records with full Create/Read/Update/Delete (CRUD) functionality. The application features a secure login system and loads employee data from CSV files. It provides a modern, professional interface for viewing employee information and generating payslips.

---

## Features

**1. Login Authentication**  
- Secure login with username/password validation
- Default credentials: `admin` / `admin123`
- Application exits if login fails or is canceled

**2. Employee Management**  
- **View:** Display all employees in a professional table format
- **Add:** Add new employee records with complete information
- **Update:** Edit existing employee information
- **Delete:** Remove employee records with confirmation
- **View Details:** View detailed employee information and payslip

**3. Modern UI**  
- Clean, professional interface with color-coded buttons
- Responsive table with alternating row colors
- Hover effects and intuitive navigation
- Proper error handling and user feedback

---

## Project Structure

```
MO-IT03/
├── bin/                          # Compiled Java classes
│   └── com/motorph/employeeapp/
│       ├── gui/                  # GUI classes
│       ├── model/                # Data models
│       ├── repository/           # Data access layer
│       └── pay/                  # Payroll calculations
├── src/                          # Java source code
│   └── com/motorph/employeeapp/
│       ├── gui/                  # GUI source files
│       ├── model/                # Model source files
│       ├── repository/           # Repository source files
│       └── pay/                  # Payroll source files
├── lib/                          # External JAR dependencies
│   ├── opencsv-5.8.jar          # CSV reading library
│   ├── commons-lang3-3.12.0.jar # Utilities
│   ├── jcalendar-1.4.jar        # Date picker component
│   └── flatlaf-3.1.jar          # Modern look and feel
├── data/                         # Application data
│   └── MotorPH Employee Record.csv # Employee database
├── docs/                         # Documentation
├── archive/                      # Archived/old files
└── README.md                     # This file
```

---

## Prerequisites  
- Java Development Kit (JDK) 8 or higher
- Windows Command Prompt or PowerShell

---

## How to Compile & Run

### Step 1: Navigate to Project Directory
```cmd
cd C:\Users\leian\MO-IT03\MO-IT03
```

### Step 2: Compile All Java Files
```cmd
javac -cp "lib/*" -d bin src/com/motorph/employeeapp/gui/*.java src/com/motorph/employeeapp/model/*.java src/com/motorph/employeeapp/repository/*.java src/com/motorph/employeeapp/pay/*.java
```

### Step 3: Run the Application
```cmd
java -cp "C:\Users\leian\MO-IT03\MO-IT03\bin;C:\Users\leian\MO-IT03\MO-IT03\lib\*" com.motorph.employeeapp.gui.EmployeeManagementFrame
```

### Login Credentials
- **Username:** `admin`
- **Password:** `admin123`

---

## Technologies Used
- **Programming Language:** Java
- **GUI Framework:** Java Swing
- **Data Storage:** CSV files
- **Libraries:** OpenCSV, JCalendar, FlatLaf, Commons Lang

---

## Key Classes

| Class | Purpose |
|-------|---------|
| `EmployeeManagementFrame` | Main application window with login and employee table |
| `Employee` | Employee data model |
| `CsvEmployeeRepository` | CSV file data access |
| `AddRecordDialog` | Dialog for adding new employees |
| `UpdateDialog` | Dialog for editing employee information |
| `PayslipSplitDialog` | Employee details and payslip viewer |

---

## Data File

The application uses `data/MotorPH Employee Record.csv` which contains:
- Employee ID, Name, Contact Information
- Government IDs (SSS, PhilHealth, TIN, Pag-IBIG)
- Position, Salary, and other employment details

---

## Important Notes

1. **Always run from the MO-IT03 directory** to ensure correct file paths
2. **The CSV file must exist** in the `data/` folder
3. **All JAR dependencies must be** in the `lib/` folder
4. **Use absolute paths** if relative paths don't work in your environment

---

## Academic Purpose
This project was developed as part of our coursework for Computer Programming 2 with the guidance of Mr. John Ray Hernani

---

## Authors
- Ghian Renzen Arboleda - lr.grarboleda@mmdc.mcl.edu.ph
- Catherine Kate Plenos - lr.ckplenos@mmdc.mcl.edu.ph  
- Leianna Khay Cruz - lr.lkcruz@mmdc.mcl.edu.ph