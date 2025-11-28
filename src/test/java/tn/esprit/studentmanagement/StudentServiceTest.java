package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService; // Use the implementation class

    private Student student;

    @BeforeEach
    void setUp() {
        // Use the actual constructor with all required fields
        student = new Student(1L, "John", "Doe", "john.doe@example.com", "123 Street", 
                            LocalDate.of(2000, 1, 1), "123456789", null, Collections.emptyList());
    }

    @Test
    void testGetStudentById_Found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(1L);

        assertNull(result);
    }

    @Test
    void testSaveStudent() {
        Student newStudent = new Student(null, "Jane", "Doe", "jane.doe@example.com", "456 Street", 
                                       LocalDate.of(2001, 1, 1), "987654321", null, Collections.emptyList());
        Student savedStudent = new Student(2L, "Jane", "Doe", "jane.doe@example.com", "456 Street", 
                                         LocalDate.of(2001, 1, 1), "987654321", null, Collections.emptyList());
        
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.saveStudent(newStudent);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
