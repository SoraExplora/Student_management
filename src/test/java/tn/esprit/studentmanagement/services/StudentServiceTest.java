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
    public void testGetAllStudents() {
        // Given
        Student student1 = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student student2 = new Student(2L, "Jane", "Smith", "jane.smith@example.com", "Mathematics");
        List<Student> expectedStudents = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // When - Use the actual method name from your service
        List<Student> actualStudents = studentService.getAllStudents();

        // Then
        assertNotNull(actualStudents);
        assertEquals(2, actualStudents.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testGetStudentById() {
        // Given
        Long studentId = 1L;
        Student expectedStudent = new Student(studentId, "John", "Doe", "john.doe@example.com", "Computer Science");
        
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(expectedStudent));

        // When - Use the actual method name
        Student actualStudent = studentService.getStudentById(studentId);

        // Then
        assertNotNull(actualStudent);
        assertEquals(studentId, actualStudent.getId());
        assertEquals("John", actualStudent.getFirstName());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void testSaveStudent() {
        // Given
        Student studentToSave = new Student(null, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student savedStudent = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");

        when(studentRepository.save(studentToSave)).thenReturn(savedStudent);

        // When - Use the actual method name
        Student result = studentService.saveStudent(studentToSave);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentRepository, times(1)).save(studentToSave);
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Long studentId = 1L;

        // When - Use the actual method name
        studentService.deleteStudent(studentId);

        // Then
        verify(studentRepository, times(1)).deleteById(studentId);
    }
}
