package tn.esprit.studentmanagement.repositories;

import tn.esprit.studentmanagement.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find student by email
    Optional<Student> findByEmail(String email);
    
    // Fix: Query by department name through the relationship
    @Query("SELECT s FROM Student s WHERE s.department.name = :departmentName")
    List<Student> findByDepartmentName(@Param("departmentName") String departmentName);
    
    // Alternative: If you want to keep the original method name but query by department ID
    List<Student> findByDepartmentIdDepartment(Long departmentId);
}
