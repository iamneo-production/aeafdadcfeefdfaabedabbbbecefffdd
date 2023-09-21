package com.examly.springapp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.examly.model.Department;
import com.examly.model.Employee;

public class SpringappApplicationTests {

    private SessionFactory factory;

    @Before
    public void setUp() {
        // Initialize Hibernate session factory using the test configuration
        factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Department.class)
            .addAnnotatedClass(Employee.class)
            .buildSessionFactory();
    }

    @Test
    public void testOneToMannyRelationship() {
        // Create a department with employees and save them
        Department department = new Department("HR");
        Employee employee1 = new Employee("John Doe");
        Employee employee2 = new Employee("Jane Smith");

        department.addEmployee(employee1);
        department.addEmployee(employee2);

        Session session = factory.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            // Save the department (and its associated employees)
            session.save(department);

            tx.commit();

            // Retrieve the department from the database
            session = factory.getCurrentSession();
            session.beginTransaction();

            int departmentId = department.getId(); // Use the ID of the saved department
            Department retrievedDepartment = session.get(Department.class, departmentId);

            assertNotNull(retrievedDepartment);
            assertEquals("HR", retrievedDepartment.getName());
            assertEquals(2, retrievedDepartment.getEmployees().size());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @After
    public void tearDown() {
        // Close the session factory after tests
        if (factory != null) {
            factory.close();
        }
    }
}
