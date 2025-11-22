package controller;

import service.AdminService;
import model.Course;
import model.User;

public class AdminController {

    private AdminService adminService;
    private CourseController CourseController;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    //approve statues
    public void approveCourse(String CourseID) {
        adminService.approveCourse(CourseID);
    }

    public void disapproveCourse(String CourseID) {
        adminService.disapproveCourse(CourseID);
    }

    //users
    public void viewAnalytics(String s) {
        adminService.viewAnalytics(s);
    }

    public void removeUsers() {
        adminService.removeUsers();
    }


    //courses
    public void addCourse(Course c) {
        CourseController.addCourse(c);
    }

    public void removeCourse(Course c) {
        CourseController.removeCourse(c);
    }

    public void editCourse(Course c) {
        CourseController.editCourse(c);
    }





}
