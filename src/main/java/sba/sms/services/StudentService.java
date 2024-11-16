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

    private SessionFactory factory = new Configuration().configure().buildSessionFactory();

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

    }

    @Override
    public List<Course> getStudentCourses(String email) {
        return List.of();
    }
}
