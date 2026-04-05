# Task Management System 

## 📌 Project Overview
This application is a **Task Management System** designed to help project managers track employees and their assigned tasks. The project was developed for the *Fundamental Programming Techniques* course at the **Technical University of Cluj-Napoca (TUCN)**.

The system focuses on efficient workload management, utilizing the **Composite Design Pattern** to handle hierarchies of tasks and **Java Serialization** for data persistence.

## 🛠️ Key Features
* **Employee Management**: Functionality to add and view employees.
* **Task Hierarchy**: 
    * **SimpleTask**: Calculates duration based on start and end hours.
    * **ComplexTask**: A composite structure that can contain other Simple or Complex tasks.
* **Assignment Logic**: Methods to assign tasks to specific employees and update their status to "Completed" or "Uncompleted".
* **Advanced Statistics (Utility Class)**:
    * Filter and sort employees with a work duration greater than 40 hours.
    * Generate reports on the count of completed/uncompleted tasks per employee.
* **Persistence**: All application data is saved and loaded using **Object Serialization**.

## 🏗️ Architecture & Requirements
The project follows a **Layered Architectural Pattern** and adheres to the following technical constraints:
* **Java 17+**: Implementation of `sealed` abstract classes for the Task hierarchy.
* **Swing GUI**: A graphical user interface for all manager operations.
* **Clean Code**: 
    * Methods are limited to a maximum of 30 lines.
    * Classes are limited to a maximum of 300 lines (excluding UI).
    * Use of `foreach` loops for collections.

## 📂 Deliverables
* **Source Code**: Java files following strict naming conventions.
* **UML Diagrams**: Use Case, Package, and Class diagrams (draw.io) included in the repository.

## 🚀 Installation & Running
To run this project locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone [https://github.com/pop-dragos/Task_Management_System.git](https://github.com/pop-dragos/Task_Management_System.git)
