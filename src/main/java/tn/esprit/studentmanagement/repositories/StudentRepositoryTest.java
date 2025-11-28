package tn.esprit.studentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import tn.esprit.studentmanagement.entities.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveStudent() {
        // Arrange
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

        // Act
        Student savedStudent = studentRepository.save(student);

        // Assert
        assertNotNull(savedStudent.getIdStudent());
        assertEquals("John", savedStudent.getFirstName());
    }

    @Test
    void testFindById() {
        // Arrange
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        Student savedStudent = studentRepository.save(student);

        // Act
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getIdStudent());

        // Assert
        assertTrue(foundStudent.isPresent());
        assertEquals("John", foundStudent.get().getFirstName());
    }

    @Test
    void testFindAll() {
        // Arrange
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@example.com");

        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@example.com");

        studentRepository.save(student1);
        studentRepository.save(student2);

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertEquals(2, students.size());
    }

    @Test
    void testDeleteStudent() {
        // Arrange
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        Student savedStudent = studentRepository.save(student);

        // Act
        studentRepository.deleteById(savedStudent.getIdStudent());

        // Assert
        Optional<Student> deletedStudent = studentRepository.findById(savedStudent.getIdStudent());
        assertFalse(deletedStudent.isPresent());
    }
}
