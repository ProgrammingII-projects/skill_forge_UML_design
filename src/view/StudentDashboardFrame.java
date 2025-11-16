package view;

import javax.swing.*;
import model.*;
import model.database_manager.CourseModel;
import model.database_manager.UserModel;
import controller.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class StudentDashboardFrame extends JFrame {
    private User user;
    private UserModel userModel;
    private CourseModel courseModel;
    private CourseController courseController;
    private StudentController studentController;
    private JList<String> courseList;
    private DefaultListModel<String> listModel;
    private JButton enrollButton;
    private JButton viewButton;
    private List<Course> allCourses;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public StudentDashboardFrame(User u, UserModel um, CourseModel cm) {
        this.user = u;
        this.userModel = um;
        this.courseModel = cm;
        this.courseController = new CourseController(courseModel);
        this.studentController = new StudentController(userModel, courseModel);
        
        initializeFrame();
        createUI();
        refreshCourseList();
    }
    
    private void initializeFrame() {
        setTitle("Skill Forge - Student Dashboard");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default
        }
    }
    
    private void createUI() {
        // Main container with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(99, 102, 241),
                    getWidth(), getHeight(), new Color(139, 92, 246)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // White card container
        JPanel whiteCard = new RoundedPanel(20);
        whiteCard.setBackground(CARD_COLOR);
        whiteCard.setLayout(new BorderLayout());
        whiteCard.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Available Courses");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel userLabel = new JLabel("Welcome, " + user.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(107, 114, 128));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);
        
        // Left side - Course list
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setOpaque(false);
        
        JLabel listTitle = new JLabel("Course List");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(TEXT_COLOR);
        listTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        listModel = new DefaultListModel<>();
        courseList = new JList<>(listModel);
        courseList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setBackground(Color.WHITE);
        courseList.setForeground(TEXT_COLOR);
        courseList.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(courseList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right side - Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        enrollButton = createPrimaryButton("Enroll");
        enrollButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        enrollButton.setMaximumSize(new Dimension(200, 45));
        enrollButton.addActionListener(e -> enrollInCourse());
        
        viewButton = createPrimaryButton("View Lessons");
        viewButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewButton.setMaximumSize(new Dimension(200, 45));
        viewButton.addActionListener(e -> viewLessons());
        
        JButton myCoursesButton = createPrimaryButton("My Enrolled Courses");
        myCoursesButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        myCoursesButton.setMaximumSize(new Dimension(200, 45));
        myCoursesButton.addActionListener(e -> showMyCourses());
        
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 45));
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(enrollButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(myCoursesButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(logoutButton);
        
        mainContent.add(listPanel, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.EAST);
        
        whiteCard.add(headerPanel, BorderLayout.NORTH);
        whiteCard.add(mainContent, BorderLayout.CENTER);
        
        contentPanel.add(whiteCard, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(PRIMARY_DARK);
                } else if (getModel().isRollover()) {
                    g2d.setColor(PRIMARY_DARK);
                } else {
                    g2d.setColor(PRIMARY_COLOR);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        return button;
    }
    
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(243, 244, 246));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(249, 250, 251));
                } else {
                    g2d.setColor(new Color(255, 255, 255));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        return button;
    }
    
    private class RoundedPanel extends JPanel {
        private int cornerRadius;
        
        public RoundedPanel(int radius) {
            super();
            cornerRadius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2d.dispose();
        }
    }
    
    private void refreshCourseList() {
        allCourses = courseController.getAllCourses();
        listModel.clear();
        for (Course c : allCourses) {
            boolean enrolled = user.getEnrolledCourses().contains(c.getCourseId());
            String status = enrolled ? "[ENROLLED] " : "";
            String progressInfo = "";
            if (enrolled) {
                List<String> completed = user.getProgress().getOrDefault(c.getCourseId(), new java.util.ArrayList<>());
                int total = c.getLessons().size();
                int done = completed.size();
                double percentage = total > 0 ? (done * 100.0 / total) : 0;
                progressInfo = " - Progress: " + done + "/" + total + " (" + String.format("%.1f", percentage) + "%)";
            }
            listModel.addElement(status + c.getTitle() + " by " + getInstructorName(c.getInstructorId()) + 
                " (" + c.getLessons().size() + " lessons)" + progressInfo);
        }
    }
    
    private String getInstructorName(String instructorId) {
        Optional<User> opt = userModel.findById(instructorId);
        return opt.isPresent() ? opt.get().getUsername() : "Unknown";
    }
    
    private void enrollInCourse() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = allCourses.get(index);
        
        if (user.getEnrolledCourses().contains(course.getCourseId())) {
            JOptionPane.showMessageDialog(this, "You are already enrolled in this course", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            studentController.enrollStudent(user.getUserId(), course.getCourseId());
            user = userModel.findById(user.getUserId()).get();
            refreshCourseList();
            JOptionPane.showMessageDialog(this, "Successfully enrolled in: " + course.getTitle(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewLessons() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = allCourses.get(index);
        
        if (!user.getEnrolledCourses().contains(course.getCourseId())) {
            JOptionPane.showMessageDialog(this, "You must enroll in this course first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LessonViewerFrame viewer = new LessonViewerFrame(course.getCourseId(), courseModel, studentController, user.getUserId());
        viewer.setVisible(true);
        viewer.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                user = userModel.findById(user.getUserId()).get();
            }
        });
    }
    
    private void showMyCourses() {
        List<String> enrolledIds = user.getEnrolledCourses();
        if (enrolledIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You are not enrolled in any courses", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder("My Enrolled Courses:\n\n");
        for (String courseId : enrolledIds) {
            Optional<Course> opt = courseController.findById(courseId);
            if (opt.isPresent()) {
                Course c = opt.get();
                List<String> completed = user.getProgress().getOrDefault(courseId, new java.util.ArrayList<>());
                int total = c.getLessons().size();
                int done = completed.size();
                double percentage = total > 0 ? (done * 100.0 / total) : 0;
                sb.append("- ").append(c.getTitle())
                  .append(" - ").append(done).append("/").append(total)
                  .append(" lessons completed (").append(String.format("%.1f", percentage)).append("%)\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "My Courses", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        dispose();
        LoginFrame loginFrame = new LoginFrame(userModel, courseModel);
        loginFrame.setVisible(true);
    }
}
