@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "John", "Doe", "john.doe@example.com", "Computer Science");
    }

    @Test
    void testGetStudentById_Found() {
        // FIX: Repository returns Optional
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetStudentById_NotFound() {
        // FIX: Return Optional.empty() for not found
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(1L);

        assertNull(result);
    }

    @Test
    void testSaveStudent() {
        Student newStudent = new Student(null, "Jane", "Doe", "jane.doe@example.com", "Math");
        Student savedStudent = new Student(2L, "Jane", "Doe", "jane.doe@example.com", "Math");
        
        // FIX: Use the correct method name
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.saveStudent(newStudent);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void testDeleteStudent() {
        // FIX: Use doNothing() for void repository methods
        doNothing().when(studentRepository).deleteById(1L);

        // Assuming your service method calls the repository
        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
