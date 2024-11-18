package sba.sms.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_courses")
@Data
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "course_id")
    private Integer courseId;
}
