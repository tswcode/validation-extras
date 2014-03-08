/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tswco.validation.test;

import de.tswco.validation.constraints.impl.PersonalAusweisValidator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leon
 */
public class PersonalAusweisValidatorTest {
    
    private PersonalAusweisValidator paValidator;
    private static final String[] TEST_NUMBERS = new String[] { "LCKC04XW75", "T220001293" };
    
    public PersonalAusweisValidatorTest() {
        paValidator = new PersonalAusweisValidator();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPA() {
        for(String testNumber: TEST_NUMBERS) {
            assertTrue(paValidator.isValid(testNumber, null));
        }
    }
}
