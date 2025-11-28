package tn.esprit.studentmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    
    @ManyToOne
    private Department department;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    // Simplified constructor for testing
    public Student(Long id, String firstName, String lastName, String email, String departmentName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = "";
        this.dateOfBirth = LocalDate.now();
        this.phoneNumber = "";
        this.department = null;
        this.enrollments = Collections.emptyList();
    }
}
