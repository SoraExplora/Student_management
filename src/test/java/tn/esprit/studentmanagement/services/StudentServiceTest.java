package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testFindAllStudents() {
        // Given - Use the correct constructor with 5 parameters
        Student student1 = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student student2 = new Student(2L, "Jane", "Smith", "jane.smith@example.com", "Mathematics");
        List<Student> expectedStudents = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // When - Use the correct method name from your service
        List<Student> actualStudents = studentService.retrieveAllStudents();

        // Then
        assertNotNull(actualStudents);
        assertEquals(2, actualStudents.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testFindStudentById() {
        // Given
        Long studentId = 1L;
        Student expectedStudent = new Student(studentId, "John", "Doe", "john.doe@example.com", "Computer Science");
        
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(expectedStudent));

        // When
        Student actualStudent = studentService.retrieveStudent(studentId);

        // Then
        assertNotNull(actualStudent);
        assertEquals(studentId, actualStudent.getId());
        assertEquals("John", actualStudent.getFirstName());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void testAddStudent() {
        // Given
        Student studentToAdd = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student savedStudent = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");

        when(studentRepository.save(studentToAdd)).thenReturn(savedStudent);

        // When
        Student result = studentService.addStudent(studentToAdd);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentRepository, times(1)).save(studentToAdd);
    }

    @Test
    public void testUpdateStudent() {
        // Given
        Student studentToUpdate = new Student(1L, "John", "Doe Updated", "john.updated@example.com", "Computer Science");
        
        when(studentRepository.save(studentToUpdate)).thenReturn(studentToUpdate);

        // When
        Student result = studentService.updateStudent(studentToUpdate);

        // Then
        assertNotNull(result);
        assertEquals("Doe Updated", result.getLastName());
        assertEquals("john.updated@example.com", result.getEmail());
        verify(studentRepository, times(1)).save(studentToUpdate);
    }

    @Test
    public void testRemoveStudent() {
        // Given
        Long studentId = 1L;

        // When
        studentService.removeStudent(studentId);

        // Then
        verify(studentRepository, times(1)).deleteById(studentId);
    }
}
