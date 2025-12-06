package tn.esprit.studentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import tn.esprit.studentmanagement.entities.Student;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver", 
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.h2.console.enabled=false"
})
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testSaveStudent() {
        // Given
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");

        // When
        Student savedStudent = studentRepository.save(student);

        // Then
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testFindById_Exists() {
        // Given
        Student student = new Student();
        student.setFirstName("Jane");
        student.setLastName("Smith");
        student.setEmail("jane.smith@example.com");
        Student savedStudent = studentRepository.save(student);

        // When
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    public void testFindById_NotExists() {
        // When
        Optional<Student> foundStudent = studentRepository.findById(999L);

        // Then
        assertThat(foundStudent).isEmpty();
    }

    @Test
    public void testFindAll() {
        // Given
        Student student1 = new Student();
        student1.setFirstName("Alice");
        student1.setLastName("Johnson");
        student1.setEmail("alice@example.com");

        Student student2 = new Student();
        student2.setFirstName("Bob");
        student2.setLastName("Brown");
        student2.setEmail("bob@example.com");

        studentRepository.save(student1);
        studentRepository.save(student2);

        // When
        List<Student> students = studentRepository.findAll();

        // Then
        assertThat(students).hasSize(2);
    }

    @Test
    public void testFindByEmail() {
        // Given
        student.setFirstName("Charlie");
        student.setLastName("Davis");
        Student student = new Student();
        student.setEmail("charlie@example.com");
        studentRepository.save(student);

        // When
        Optional<Student> foundStudent = studentRepository.findByEmail("charlie@example.com");

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getFirstName()).isEqualTo("Charlie");
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Student student = new Student();
        student.setFirstName("David");
        student.setLastName("Wilson");
        student.setEmail("david@example.com");
        Student savedStudent = studentRepository.save(student);

        // When
        studentRepository.deleteById(savedStudent.getId());
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        // Then
        assertThat(foundStudent).isEmpty();
    }

    // Remove or comment out the testFindByDepartment method since the repository method no longer exists
    /*
    @Test
    public void testFindByDepartment() {
        // This test is no longer valid since findByDepartment method was removed
        // Given
        Student student = new Student();
        student.setFirstName("Eve");
        student.setLastName("Miller");
        student.setEmail("eve@example.com");
        studentRepository.save(student);

        // When
        List<Student> students = studentRepository.findByDepartment("Computer Science");

        // Then
        assertThat(students).isEmpty(); // This would fail now
    }
    */
}
