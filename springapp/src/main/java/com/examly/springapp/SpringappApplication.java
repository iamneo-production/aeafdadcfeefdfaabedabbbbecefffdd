package com.examly.springapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.examly.model.Department;
import com.examly.model.Employee;

public class SpringappApplication {
    public static void main(String[] args) {
        // Create a Hibernate session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Department.class)
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        // Create a session
        Session session = factory.getCurrentSession();

        try {
            // Begin a transaction
            session.beginTransaction();

            // Create a department
            Department department = new Department("Development");

            // Create employees and associate them with the department
            Employee employee1 = new Employee("Vikram");
            Employee employee2 = new Employee("Vijay");

            department.addEmployee(employee1);
            department.addEmployee(employee2);

            // Save the department (and its associated employees)
            session.save(department);

            // Commit the transaction
            session.getTransaction().commit();

            // Retrieve and display department details
            session = factory.getCurrentSession();
            session.beginTransaction();

            int departmentId = 1; // Replace with the actual department ID
            Department retrievedDepartment = session.get(Department.class, departmentId);

            System.out.println("Department Details:");
            System.out.println("ID: " + retrievedDepartment.getId());
            System.out.println("Name: " + retrievedDepartment.getName());

            System.out.println("Employee Details:");
            for (Employee employee : retrievedDepartment.getEmployees()) {
                System.out.println("ID: " + employee.getId());
                System.out.println("Name: " + employee.getName());
            }

            // Commit the transaction
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }
}
