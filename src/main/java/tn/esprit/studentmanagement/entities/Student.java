package tn.esprit.studentmanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStudent;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
    // Add this constructor for testing convenience
public Student(Long id, String firstName, String lastName, String email, String department) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    // Set default values for other fields
    this.address = "";
    this.dateOfBirth = LocalDate.now();
    this.phoneNumber = "";
    this.department = null;
    this.enrollments = Collections.emptyList();
}
}
