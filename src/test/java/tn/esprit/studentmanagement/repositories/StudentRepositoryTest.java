package tn.esprit.studentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import tn.esprit.studentmanagement.entities.Student;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testFindById_Exists() {
        // Given
        Student student = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student savedStudent = entityManager.persistAndFlush(student);

        // When
        Optional<Student> found = studentRepository.findById(savedStudent.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
        assertEquals("john.doe@example.com", found.get().getEmail());
    }

    @Test
    void testFindById_NotExists() {
        // When
        Optional<Student> found = studentRepository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        // Given
        Student student1 = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student student2 = new Student(null, "Jane", "Smith", "jane.smith@example.com", "Mathematics");
        
        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        // When
        List<Student> students = studentRepository.findAll();

        // Then
        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getFirstName().equals("John")));
        assertTrue(students.stream().anyMatch(s -> s.getFirstName().equals("Jane")));
    }

    @Test
    void testSaveStudent() {
        // Given
        Student student = new Student(null, "Alice", "Johnson", "alice.johnson@example.com", "Physics");

        // When
        Student savedStudent = studentRepository.save(student);

        // Then
        assertNotNull(savedStudent.getId());
        assertEquals("Alice", savedStudent.getFirstName());
        assertEquals("Johnson", savedStudent.getLastName());
        assertEquals("alice.johnson@example.com", savedStudent.getEmail());
        assertEquals("Physics", savedStudent.getDepartment());
    }

    @Test
    void testDeleteStudent() {
        // Given
        Student student = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student savedStudent = entityManager.persistAndFlush(student);

        // When
        studentRepository.deleteById(savedStudent.getId());
        entityManager.flush();

        // Then
        Optional<Student> deleted = studentRepository.findById(savedStudent.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByEmail() {
        // Given
        Student student = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        entityManager.persistAndFlush(student);

        // When
        Optional<Student> found = studentRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("john.doe@example.com", found.get().getEmail());
    }

    @Test
    void testFindByDepartment() {
        // Given
        Student student1 = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student student2 = new Student(null, "Jane", "Smith", "jane.smith@example.com", "Computer Science");
        Student student3 = new Student(null, "Alice", "Johnson", "alice.johnson@example.com", "Mathematics");
        
        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        // When
        List<Student> computerScienceStudents = studentRepository.findByDepartment("Computer Science");

        // Then
        assertEquals(2, computerScienceStudents.size());
        assertTrue(computerScienceStudents.stream().allMatch(s -> s.getDepartment().equals("Computer Science")));
    }
}
