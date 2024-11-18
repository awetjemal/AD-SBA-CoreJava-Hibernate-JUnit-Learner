package sba.sms.services;

import jakarta.persistence.TypedQuery;
import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.models.StudentCourse;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {

    private final SessionFactory factory = new Configuration().configure().buildSessionFactory();
    private final CourseService courseService = new CourseService();
    @Override
    public List<Student> getAllStudents() {
        Session session = factory.openSession();
        List<Student> students = new ArrayList<>();
        String hql = "SELECT s FROM Student s";
        TypedQuery<Student> query = session.createQuery(hql, Student.class);
        try {
            students = query.getResultList();
            return students;
        }catch (HibernateException e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            session.close();
        }

    }

    @Override
    public void createStudent(Student student) {
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.persist(student);
        session.getTransaction().commit();
    }

    @Override
    public Student getStudentByEmail(String email) {
        Session session = factory.openSession();
        String hql = "FROM Student s WHERE s.email = :email";
        TypedQuery<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("email", email);

        try {
            return  query.getSingleResult();
        }catch (HibernateException e) {
            //System.out.println(e.getMessage());
            return null;
        }finally {
            session.close();

        }
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Session session = factory.openSession();
        Student student = getStudentByEmail(email);
        if (student != null)
            return student.getPassword().equals(password);
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session session = factory.openSession();
        Student student = getStudentByEmail(email);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseId(courseId);
        studentCourse.setStudentId(student.getId());
        session.getTransaction().begin();
        session.persist(studentCourse);
        session.getTransaction().commit();

    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session session = factory.openSession();
        Student student = getStudentByEmail(email);
        String hql = "SELECT courseId FROM StudentCourse s WHERE s.studentId = :sId";
        TypedQuery<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("sId", student.getId());
        List<Integer> studentCoursesIds = new ArrayList<>();
        List<Course> studentCourses = new ArrayList<>();
        try{
            studentCoursesIds = query.getResultList();
            for(Integer courseId : studentCoursesIds){
                studentCourses.add(courseService.getCourseById(courseId));
            }
            return  studentCourses;
        }catch (HibernateException e){
            System.out.println(e.getMessage());
            return null;
        }finally {
            session.close();
        }
        //return null;
    }
}
