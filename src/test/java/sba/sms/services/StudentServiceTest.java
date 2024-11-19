package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sba.sms.utils.CommandLine.addData;

import sba.sms.services.StudentService;
import org.junit.Assert;
//import org.junit.Test;
import org.junit.jupiter.api.Assertions;

class StudentServiceTest {
    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();

    @Test
    public void getAllStudentsTest() {
        CommandLine.addData();
        List<Student> students = studentService.getAllStudents();
        Assertions.assertEquals(6, students.size());
    }

    @Test
    public void createStudentTest() {
        //given
        Student student = new Student();
        student.setName("Test");
        student.setEmail("Test@test.com");
        student.setPassword("Test123");

        //when
        Assertions.assertNull(student.getId());
        studentService.createStudent(student);

        //then
        Assertions.assertNotNull(student.getId());

    }

    @Test
    public void getStudentByEmailTest() {
        CommandLine.addData();
        Student student = studentService.getStudentByEmail("reema@gmail.com");
        Assertions.assertNotNull(student);
        Assertions.assertEquals(1, student.getId());
    }

    @Test
    public void registerStudentToCourseTest() {
        CommandLine.addData();
        Student student = studentService.getStudentByEmail("reema@gmail.com");
         studentService.registerStudentToCourse("reema@gmail.com", 4);
         Course expectedCourseObject = courseService.getCourseById(4);
         List<Course> actualCoursesList = studentService.getStudentCourses("reema@gmail.com");
         Assertions.assertTrue(actualCoursesList.contains(expectedCourseObject));
    }


}