package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
        student2 = new Student(2L, "Jane", "Smith", "jane.smith@example.com", "Mathematics");
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(1L);

        assertNull(result);
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveStudent() {
        Student newStudent = new Student(null, "Alice", "Johnson", "alice.johnson@example.com", "Physics");
        Student savedStudent = new Student(3L, "Alice", "Johnson", "alice.johnson@example.com", "Physics");
        
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.saveStudent(newStudent);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Alice", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void testUpdateStudent() {
        Student existingStudent = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
        Student updatedStudent = new Student(1L, "John", "Doe Updated", "john.updated@example.com", "Computer Science");
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Student result = studentService.saveStudent(updatedStudent);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Doe Updated", result.getLastName());
        assertEquals("john.updated@example.com", result.getEmail());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(updatedStudent);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveStudent_NullStudent() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.saveStudent(null);
        });
    }

    @Test
    void testDeleteStudent_InvalidId() {
        doThrow(new IllegalArgumentException("Invalid student ID")).when(studentRepository).deleteById(-1L);

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.deleteStudent(-1L);
        });
    }
}
