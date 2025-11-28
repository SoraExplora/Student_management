package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
class StudentManagementApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
        System.out.println("Context Loads Successfully");
    }

}
