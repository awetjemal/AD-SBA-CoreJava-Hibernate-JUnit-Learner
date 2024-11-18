package sba.sms.services;

import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI{
    private final SessionFactory factory = new Configuration().configure().buildSessionFactory();
    @Override
    public void createCourse(Course course) {
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.persist(course);
        session.getTransaction().commit();
    }

    @Override
    public Course getCourseById(int courseId) {
        Session session = factory.openSession();
        String hql = "from Course where id = :id";
        TypedQuery<Course> query = session.createQuery(hql, Course.class);
        query.setParameter("id", courseId);
        try {
            return query.getSingleResult();
        }catch (HibernateException e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            session.close();
        }

    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Session session = factory.openSession();
        String hql = "from Course";
        TypedQuery<Course> query = session.createQuery(hql, Course.class);
        try {
            courses = query.getResultList();
            return courses;
        }catch (HibernateException e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            session.close();
        }

    }
}
