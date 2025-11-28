@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
    }

    @Test
    void testGetStudentById() throws Exception {
        // FIX: Use Optional.of() for repository methods that return Optional
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        // FIX: Return null or throw exception for not found
        when(studentService.getStudentById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStudent() throws Exception {
        Student newStudent = new Student(null, "Jane", "Doe", "jane.doe@example.com", "Math");
        Student savedStudent = new Student(2L, "Jane", "Doe", "jane.doe@example.com", "Math");
        
        // FIX: Use the correct method name (probably saveStudent or createStudent)
        when(studentService.saveStudent(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.com\",\"department\":\"Math\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void testDeleteStudent() throws Exception {
        // FIX: Use doNothing() for void methods
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
