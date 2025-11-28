package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

   @Mock
    private IStudentService studentService;

    private Student student1;
    private Student student2;
    private Department computerScienceDept;
    private Department mathDept;
    private Department physicsDept;

    @BeforeEach
    void setUp() {
        // Create departments with empty student lists
        computerScienceDept = new Department(1L, "Computer Science", "Dr. Smith", "Building A", "123-456-7890", new ArrayList<>());
        mathDept = new Department(2L, "Mathematics", "Dr. Johnson", "Building B", "123-456-7891", new ArrayList<>());
        physicsDept = new Department(3L, "Physics", "Dr. Wilson", "Building C", "123-456-7892", new ArrayList<>());
        
        // Create students with all required fields
        student1 = new Student(1L, "John", "Doe", "john.doe@example.com", "123 Main St", 
                              LocalDate.of(2000, 1, 1), "123-456-7890", computerScienceDept, new ArrayList<>());
        student2 = new Student(2L, "Jane", "Smith", "jane.smith@example.com", "456 Oak Ave", 
                              LocalDate.of(2001, 2, 2), "123-456-7891", mathDept, new ArrayList<>());
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(student1);

        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(null);

        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk()); // Your controller returns 200 even if not found
    }

    @Test
    void testCreateStudent() throws Exception {
        // Create new student with all required fields
        Student newStudent = new Student(null, "Alice", "Johnson", "alice.johnson@example.com", 
                                        "789 Road", LocalDate.of(2002, 3, 3), "123-456-7893", 
                                        physicsDept, new ArrayList<>());
        Student savedStudent = new Student(3L, "Alice", "Johnson", "alice.johnson@example.com", 
                                          "789 Road", LocalDate.of(2002, 3, 3), "123-456-7893", 
                                          physicsDept, new ArrayList<>());
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(savedStudent);

        String studentJson = """
        {
            "firstName": "Alice",
            "lastName": "Johnson",
            "email": "alice.johnson@example.com",
            "address": "789 Road",
            "phoneNumber": "123-456-7893",
            "dateOfBirth": "2002-03-03",
            "department": {
                "id": 3,
                "name": "Physics",
                "head": "Dr. Wilson",
                "location": "Building C",
                "phone": "123-456-7892"
            }
        }
        """;

        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Johnson"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1L, "John", "Doe Updated", "john.updated@example.com", 
                                            "123 Street Updated", LocalDate.of(2000, 1, 1), "123-456-7899", 
                                            computerScienceDept, new ArrayList<>());
        
        when(studentService.saveStudent(any(Student.class))).thenReturn(updatedStudent);

        String studentJson = """
        {
            "id": 1,
            "firstName": "John",
            "lastName": "Doe Updated",
            "email": "john.updated@example.com",
            "address": "123 Street Updated",
            "phoneNumber": "123-456-7899",
            "dateOfBirth": "2000-01-01",
            "department": {
                "id": 1,
                "name": "Computer Science",
                "head": "Dr. Smith",
                "location": "Building A",
                "phone": "123-456-7890"
            }
        }
        """;

        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/deleteStudent/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
