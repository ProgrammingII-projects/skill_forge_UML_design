package view;

import javax.swing.*;
import model.Course;
import model.Lesson;
import model.User;
import model.database_manager.CourseModel;
import model.database_manager.UserModel;
import controller.StudentController;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class LessonViewerFrame extends JFrame {
    private String courseId;
    private CourseModel courseModel;
    private StudentController studentController;
    private UserModel userModel;
    private String studentId;
    private Course course;
    private JList<String> lessonList;
    private DefaultListModel<String> listModel;
    private JTextArea contentArea;
    private JLabel progressLabel;
    private JButton markCompleteButton;
    private int currentLessonIndex = -1;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public LessonViewerFrame(String courseId, CourseModel cm, StudentController sc, String studentId) {
        this.courseId = courseId;
        this.courseModel = cm;
        this.studentController = sc;
        this.studentId = studentId;
        
        // Get UserModel from StudentController
        try {
            java.lang.reflect.Field field = StudentController.class.getDeclaredField("userModel");
            field.setAccessible(true);
            this.userModel = (UserModel) field.get(sc);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access UserModel", e);
        }
        
        Optional<Course> opt = courseModel.findById(courseId);
        if (!opt.isPresent()) {
            JOptionPane.showMessageDialog(null, "Course not found", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        this.course = opt.get();
        
        initializeFrame();
        createUI();
        refreshLessonList();
        updateProgress();
    }
    
    private void initializeFrame() {
        setTitle("Skill Forge - Lesson Viewer: " + course.getTitle());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        JLabel titleLabel = new JLabel("Lessons");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);
        
        // Left side - Lesson list and controls
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JLabel listTitle = new JLabel("Lesson List");
        listTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listTitle.setForeground(TEXT_COLOR);
        listTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        listModel = new DefaultListModel<>();
        lessonList = new JList<>(listModel);
        lessonList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.setBackground(Color.WHITE);
        lessonList.setForeground(TEXT_COLOR);
        lessonList.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        lessonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showLesson();
            }
        });
        
        JScrollPane listScroll = new JScrollPane(lessonList);
        listScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        listScroll.setOpaque(false);
        listScroll.getViewport().setBackground(Color.WHITE);
        
        // Progress and button panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        progressLabel = new JLabel();
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        progressLabel.setForeground(TEXT_COLOR);
        progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        markCompleteButton = createPrimaryButton("Mark as Complete");
        markCompleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        markCompleteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        markCompleteButton.addActionListener(e -> markComplete());
        
        controlPanel.add(progressLabel);
        controlPanel.add(Box.createVerticalStrut(12));
        controlPanel.add(markCompleteButton);
        
        leftPanel.add(listTitle, BorderLayout.NORTH);
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Right side - Content area
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        
        JLabel contentLabel = new JLabel("Lesson Content");
        contentLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentLabel.setForeground(TEXT_COLOR);
        contentLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setBackground(Color.WHITE);
        contentArea.setForeground(TEXT_COLOR);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(null);
        contentScroll.setOpaque(false);
        contentScroll.getViewport().setBackground(Color.WHITE);
        
        rightPanel.add(contentLabel, BorderLayout.NORTH);
        rightPanel.add(contentScroll, BorderLayout.CENTER);
        
        mainContent.add(leftPanel, BorderLayout.WEST);
        mainContent.add(rightPanel, BorderLayout.CENTER);
        
        whiteCard.add(titleLabel, BorderLayout.NORTH);
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
    
    private void refreshLessonList() {
        course = courseModel.findById(courseId).get();
        listModel.clear();
        Optional<User> userOpt = userModel.findById(studentId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> completed = user.getProgress().getOrDefault(courseId, new java.util.ArrayList<>());
            
            for (Lesson l : course.getLessons()) {
                String status = completed.contains(l.getLessonId()) ? "✓ " : "";
                listModel.addElement(status + l.getTitle());
            }
        } else {
            for (Lesson l : course.getLessons()) {
                listModel.addElement(l.getTitle());
            }
        }
    }
    
    private void showLesson() {
        int index = lessonList.getSelectedIndex();
        if (index < 0 || index >= course.getLessons().size()) {
            contentArea.setText("");
            return;
        }
        currentLessonIndex = index;
        Lesson lesson = course.getLessons().get(index);
        contentArea.setText(lesson.getContent());
        updateProgress();
    }
    
    private void updateProgress() {
        Optional<User> userOpt = userModel.findById(studentId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> completed = user.getProgress().getOrDefault(courseId, new java.util.ArrayList<>());
            int total = course.getLessons().size();
            int done = completed.size();
            progressLabel.setText("Progress: " + done + "/" + total + " lessons completed");
        } else {
            progressLabel.setText("Progress: Unknown");
        }
    }
    
    private void markComplete() {
        if (currentLessonIndex < 0 || currentLessonIndex >= course.getLessons().size()) {
            JOptionPane.showMessageDialog(this, "Please select a lesson", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Lesson lesson = course.getLessons().get(currentLessonIndex);
        try {
            studentController.markLessonCompleted(studentId, courseId, lesson.getLessonId());
            refreshLessonList();
            updateProgress();
            JOptionPane.showMessageDialog(this, "Lesson marked as complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
