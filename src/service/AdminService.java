package service;

import dao.CourseDAO;
import dao.UserDAO;
import dao.AdminDAO;
import model.Course;
import model.User;



public class AdminService {

    private  CourseDAO courseDAO;
    private  AdminDAO adminDAO;
    private  StudentService studentService;

    public AdminService(CourseDAO courseDAO, AdminDAO AdminDAO) {
        this.courseDAO = courseDAO;
        this.adminDAO = AdminDAO;
    }

    public void approveCourse(String courseID) {
        courseDAO.updateCourseStatus(courseID ,"approved");
    }   

    public void disapproveCourse(String courseID) {
        courseDAO.updateCourseStatus(courseID ,"disapproved");
        
    }   

    public void viewAnalytics(String STudentID) {
        
    }   

    


    

   
}
