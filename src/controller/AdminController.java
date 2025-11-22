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

    public void approveCourse(Course c) {
        adminService.approveCourse(c);
    }

    public void disapproveCourse(Course c) {
        adminService.disapproveCourse(c);
    }

    public void viewAnalytics(User s) {
        adminService.viewAnalytics(s);
    }

    public void addUsers() {
        adminService.addUsers();
    }

    public void removeUsers() {
        adminService.removeUsers();
    }

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
