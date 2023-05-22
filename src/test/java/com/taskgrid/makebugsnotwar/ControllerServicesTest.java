package com.taskgrid.makebugsnotwar;

import com.taskgrid.makebugsnotwar.service.ControllerServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerServicesTest {
    private ControllerServices controllerServices;

    @Autowired
    ControllerServicesTest(ControllerServices controllerServices){
        this.controllerServices = controllerServices;
    }

    @Test
    void testCapitalLettersOnlyFirstname(){
        String  str = "ABCDEFGH";
        String result = controllerServices.checkFirstname(str);
        assertEquals("Abcdefgh", result, "Test succesfull.");
    }
    @Test
    void nonCapitalLettersOnlyFirstname(){
        String str = "abcdefgh";
        String result = controllerServices.checkFirstname(str);
        assertEquals("Abcdefgh", result, "Test complete.");
    }

    @Test
    void testCapitalLettersOnlyLastname(){
        String str = "ABCDEFGH";
        String result = controllerServices.checkFirstname(str);
        assertEquals("Abcdefgh", result, "Test complete.");
    }

    @Test
    void nonCapitalLettersOnlyLastname(){
        String str = "abcdefgh";
        String result = controllerServices.checkFirstname(str);
        assertEquals("Abcdefgh", result, "Test complete.");
    }

    @Test
    void checkUserNameMaxLength(){
        String str = "ThirtyCharAmountLengthUsername";
        boolean result = controllerServices.checkUsername(str);
        assertFalse(result, "Test complete.");
    }
    @Test
    void checkUserNameLegalCharacters(){
        String str = "abcdefæøåABCDEFÆØÅ";
        boolean result = controllerServices.checkUsername(str);
        assertTrue(result, "Test complete.");
    }
    @Test
    void checkUserNameIllegalCharacters(){
        String str = "abcd#";
        boolean result = controllerServices.checkUsername(str);
        assertFalse(result, "Test complete.");
    }
    @Test
    void checkUserNameDigits(){
        String str = "123456789";
        boolean result = controllerServices.checkUsername(str);
        assertTrue(result, "Test complete.");
    }
    @Test
    void checkPasswordCorrectLength18(){
        String str = "123456789123456789";
        boolean result = controllerServices.checkPassword(str);
        assertTrue(result, "Test complete.");
    }
    @Test
    void checkPasswordMaxLength350(){
        String str = "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345" +
                "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345" +
                "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345" +
                "12345123451234512345123451234512345123451234512345";
        boolean result = controllerServices.checkPassword(str);
        assertFalse(result, "Test  complete.");
    }
    @Test
    void checkPasswordMinLength15(){
        String str = "123456789123456";
        boolean result = controllerServices.checkPassword(str);
        assertFalse(result, "Test complete");
    }
    @Test
    void checkEmailCorrect(){
        String str = "abc@abc.dk";
        boolean result = controllerServices.checkEmail(str);
        assertTrue(result, "Test complete.");
    }
    @Test
    void checkEmailNoSnabelA(){
        String str = "abc.abc.dk";
        boolean result = controllerServices.checkEmail(str);
        assertFalse(result, "Test complete.");
    }
    @Test
    void checkEmailNoDot(){
        String str = "abc@abcdk";
        boolean result = controllerServices.checkEmail(str);
        assertFalse(result, "Test complete.");
    }
}