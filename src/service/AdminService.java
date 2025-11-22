package service;

import dao.CourseDAO;
import dao.UserDAO;
import model.Course;
import model.User;


public class AdminService {

    private final CourseDAO courseDAO;
    private final AdminDAO adminDAO;
    private final StudentService studentService;

    public AdminService(CourseDAO courseDAO, UserDAO AdminDAO) {
        this.courseDAO = courseDAO;
        this.adminDAO = AdminDAO;
    }

    public void approveCourse(Course c) {
        c.setApproveStatus("approved");
    }

    public void disapproveCourse(Course c) {
        c.setApproveStatus("disapproved");
    }   

   
}
