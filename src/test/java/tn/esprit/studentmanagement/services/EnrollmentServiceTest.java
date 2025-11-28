package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEnrollments() {
        Enrollment e1 = new Enrollment();
        Enrollment e2 = new Enrollment();
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Enrollment> result = enrollmentService.getAllEnrollments();
        assertEquals(2, result.size());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentById() {
        Enrollment e = new Enrollment();
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e));

        Enrollment result = enrollmentService.getEnrollmentById(1L);
        assertSame(e, result);
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveEnrollment() {
        Enrollment e = new Enrollment();
        when(enrollmentRepository.save(e)).thenReturn(e);

        Enrollment result = enrollmentService.saveEnrollment(e);
        assertSame(e, result);
        verify(enrollmentRepository, times(1)).save(e);
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteEnrollment(1L);
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}