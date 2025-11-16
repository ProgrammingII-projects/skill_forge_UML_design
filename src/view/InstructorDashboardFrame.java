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

public class InstructorDashboardFrame extends JFrame {
    private User user;
    private UserModel userModel;
    private CourseModel courseModel;
    private CourseController courseController;
    private JList<String> courseList;
    private DefaultListModel<String> listModel;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton manageLessonsButton;
    private List<Course> courses;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public InstructorDashboardFrame(User u, UserModel um, CourseModel cm) {
        this.user = u;
        this.userModel = um;
        this.courseModel = cm;
        this.courseController = new CourseController(courseModel);
        
        initializeFrame();
        createUI();
        refreshCourseList();
    }
    
    private void initializeFrame() {
        setTitle("Skill Forge - Instructor Dashboard");
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
        
        JLabel titleLabel = new JLabel("My Courses");
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
        
        createButton = createPrimaryButton("Create Course");
        createButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createButton.setMaximumSize(new Dimension(200, 45));
        createButton.addActionListener(e -> createCourse());
        
        editButton = createPrimaryButton("Edit Course");
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(200, 45));
        editButton.addActionListener(e -> editCourse());
        
        manageLessonsButton = createPrimaryButton("Manage Lessons");
        manageLessonsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        manageLessonsButton.setMaximumSize(new Dimension(200, 45));
        manageLessonsButton.addActionListener(e -> manageLessons());
        
        JButton viewStudentsButton = createPrimaryButton("View Enrolled Students");
        viewStudentsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewStudentsButton.setMaximumSize(new Dimension(200, 45));
        viewStudentsButton.addActionListener(e -> viewEnrolledStudents());
        
        deleteButton = createDangerButton("Delete Course");
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.setMaximumSize(new Dimension(200, 45));
        deleteButton.addActionListener(e -> deleteCourse());
        
        JButton logoutButton = createSecondaryButton("Logout");
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 45));
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(createButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(manageLessonsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(viewStudentsButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(deleteButton);
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
    
    private JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color dangerColor = new Color(239, 68, 68);
                Color dangerDark = new Color(220, 38, 38);
                
                if (getModel().isPressed()) {
                    g2d.setColor(dangerDark);
                } else if (getModel().isRollover()) {
                    g2d.setColor(dangerDark);
                } else {
                    g2d.setColor(dangerColor);
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
        courses = courseController.getCoursesByInstructor(user.getUserId());
        listModel.clear();
        for (Course c : courses) {
            int studentCount = c.getStudents().size();
            listModel.addElement(c.getTitle() + " (" + c.getLessons().size() + " lessons, " + studentCount + " students)");
        }
    }
    
    private void createCourse() {
        CourseEditorFrame editor = new CourseEditorFrame(user.getUserId(), courseModel, true, null);
        editor.setVisible(true);
        editor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshCourseList();
            }
        });
    }
    
    private void editCourse() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = courses.get(index);
        CourseEditorFrame editor = new CourseEditorFrame(user.getUserId(), courseModel, false, course);
        editor.setVisible(true);
        editor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshCourseList();
            }
        });
    }
    
    private void manageLessons() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = courses.get(index);
        LessonEditorFrame editor = new LessonEditorFrame(course, courseModel);
        editor.setVisible(true);
        editor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshCourseList();
            }
        });
    }
    
    private void deleteCourse() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = courses.get(index);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete: " + course.getTitle() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            courseController.deleteCourse(course.getCourseId());
            refreshCourseList();
        }
    }
    
    private void viewEnrolledStudents() {
        int index = courseList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Course course = courses.get(index);
        List<String> studentIds = course.getStudents();
        
        if (studentIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in: " + course.getTitle(), "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder("Enrolled Students in: " + course.getTitle() + "\n\n");
        sb.append("Total: ").append(studentIds.size()).append(" student(s)\n\n");
        
        for (String studentId : studentIds) {
            Optional<User> opt = userModel.findById(studentId);
            if (opt.isPresent()) {
                User student = opt.get();
                List<String> completed = student.getProgress().getOrDefault(course.getCourseId(), new java.util.ArrayList<>());
                int total = course.getLessons().size();
                int done = completed.size();
                double percentage = total > 0 ? (done * 100.0 / total) : 0;
                sb.append("- ").append(student.getUsername())
                  .append(" (").append(student.getEmail()).append(")")
                  .append(" - Progress: ").append(done).append("/").append(total)
                  .append(" (").append(String.format("%.1f", percentage)).append("%)\n");
            } else {
                sb.append("- Unknown student (ID: ").append(studentId).append(")\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        dispose();
        LoginFrame loginFrame = new LoginFrame(userModel, courseModel);
        loginFrame.setVisible(true);
    }
}
