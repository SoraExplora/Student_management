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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudents() {
        // Arrange
        Student student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("John");
        
        Student student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Jane");
        
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertEquals(2, students.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Found() {
        // Arrange
        Student student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("John");
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testGetStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateStudent() {
        // Arrange
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john.doe@example.com");
        
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student savedStudent = studentService.createStudent(student);

        // Assert
        assertNotNull(savedStudent);
        assertEquals("John", savedStudent.getFirstName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        // Arrange
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }
}
