package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        // Use the actual constructor with all required fields
        student = new Student(1L, "John", "Doe", "john.doe@example.com", "123 Street", 
                            LocalDate.of(2000, 1, 1), "123456789", null, Collections.emptyList());
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(null);

        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStudent() throws Exception {
        Student newStudent = new Student(null, "Jane", "Doe", "jane.doe@example.com", "456 Street", 
                                       LocalDate.of(2001, 1, 1), "987654321", null, Collections.emptyList());
        Student savedStudent = new Student(2L, "Jane", "Doe", "jane.doe@example.com", "456 Street", 
                                         LocalDate.of(2001, 1, 1), "987654321", null, Collections.emptyList());
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/deleteStudent/1"))
                .andExpect(status().isOk());
    }
}
