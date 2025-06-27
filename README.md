# Course Management System - Java Swing

------------------------------------------------------------
Description:
------------------------------------------------------------
This is a simple Java Swing-based GUI application for managing courses, professors, and students.

The entire system is implemented in a single file: CMSProject.java

Roles supported:
1. Admin
2. Professor
3. Student

Each role has its own panel for specific actions.

------------------------------------------------------------
Features:
------------------------------------------------------------

Admin Panel:
- Add Course with associated Professor
- Add Professor
- Add Student
- View all data (courses, professors, students, enrollments)
- Logout

Professor Panel:
- View all courses assigned to the professor
- View students enrolled in the professor's courses
- Logout

Student Panel:
- View available courses
- Enroll in a course using a dropdown list
- View enrolled courses
- Logout

------------------------------------------------------------
How to Compile & Run (Using Command Prompt):
------------------------------------------------------------

1. Open Command Prompt
2. Navigate to the folder containing CMSProject.java

3. Compile:
   javac CMSProject.java

4. Run:
   java CMSProject

------------------------------------------------------------
Login Info:
------------------------------------------------------------

- Admin Username: admin
- Admin Password: admin123

- Professors and Students: Just type any name to log in.
  If not found, they are created automatically.

------------------------------------------------------------
Notes:
------------------------------------------------------------
- This version uses in-memory data only. No database or file persistence.
- GUI is styled using Java Swing and basic color theming.

------------------------------------------------------------
Author: Karthik Devini
