package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTests {

    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        studentService = new StudentService();
        ReflectionTestUtils.setField(studentService, "studentRepository", studentRepository);
    }

    @Test
    void getStudentById_returnsNull_whenNotFound() {
        Long id = 42L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(id);
        assertNull(result);
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void saveStudent_returnsSameInstanceFromRepository() {
        Student s = new Student();
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.saveStudent(s);
        assertSame(s, saved);
        verify(studentRepository, times(1)).save(s);
    }
}
