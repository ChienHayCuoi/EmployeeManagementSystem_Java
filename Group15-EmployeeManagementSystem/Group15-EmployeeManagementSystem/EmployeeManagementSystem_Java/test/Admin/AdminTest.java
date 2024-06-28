/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ADMIN
 */
public class AdminTest {
    
    public AdminTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of insertCbxDay method, of class Admin.
     */
    @Test
    public void testInsertCbxDay() {
        System.out.println("insertCbxDay");
        Admin instance = new Admin();
        instance.insertCbxDay();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertCbxMonth method, of class Admin.
     */
    @Test
    public void testInsertCbxMonth() {
        System.out.println("insertCbxMonth");
        Admin instance = new Admin();
        instance.insertCbxMonth();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertCbxYear method, of class Admin.
     */
    @Test
    public void testInsertCbxYear() {
        System.out.println("insertCbxYear");
        Admin instance = new Admin();
        instance.insertCbxYear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkBirthdayIsSelected method, of class Admin.
     */
    @Test
    public void testCheckBirthdayIsSelected() {
        System.out.println("checkBirthdayIsSelected");
        Admin instance = new Admin();
        boolean expResult = false;
        boolean result = instance.checkBirthdayIsSelected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class Admin.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Admin.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
