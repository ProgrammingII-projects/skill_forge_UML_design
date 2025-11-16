package view;

import controller.*;
import model.*;
import model.database_manager.CourseModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class LessonEditorFrame extends JFrame {
    private model.Course course;
    private CourseModel courseModel;
    private LessonController lessonController;
    private JList<String> lessonList;
    private DefaultListModel<String> listModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public LessonEditorFrame(model.Course course, CourseModel cm) {
        this.course = course;
        this.courseModel = cm;
        this.lessonController = new LessonController(courseModel);
        
        initializeFrame();
        createUI();
        refreshLessonList();
    }
    
    private void initializeFrame() {
        setTitle("Skill Forge - Lessons: " + course.getTitle());
        setSize(700, 550);
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
        
        // Left side - Lesson list
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setOpaque(false);
        
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
        
        JScrollPane scrollPane = new JScrollPane(lessonList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right side - Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        addButton = createPrimaryButton("Add Lesson");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(200, 45));
        addButton.addActionListener(e -> addLesson());
        
        editButton = createPrimaryButton("Edit Lesson");
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(200, 45));
        editButton.addActionListener(e -> editLesson());
        
        deleteButton = createDangerButton("Delete Lesson");
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.setMaximumSize(new Dimension(200, 45));
        deleteButton.addActionListener(e -> deleteLesson());
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(deleteButton);
        
        mainContent.add(listPanel, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.EAST);
        
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
    
    private void refreshLessonList() {
        course = courseModel.findById(course.getCourseId()).get();
        listModel.clear();
        for (model.Lesson l : course.getLessons()) {
            listModel.addElement(l.getTitle());
        }
    }
    
    private void addLesson() {
        String title = JOptionPane.showInputDialog(this, "Enter lesson title:", "Add Lesson", JOptionPane.PLAIN_MESSAGE);
        if (title == null || title.trim().isEmpty()) return;
        
        String content = JOptionPane.showInputDialog(this, "Enter lesson content:", "Add Lesson", JOptionPane.PLAIN_MESSAGE);
        if (content == null) return;
        
        try {
            String lessonId = UUID.randomUUID().toString();
            model.Lesson lesson = new model.Lesson(lessonId, title.trim(), content.trim());
            lessonController.addLesson(course.getCourseId(), lesson);
            refreshLessonList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editLesson() {
        int index = lessonList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a lesson", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        model.Lesson lesson = course.getLessons().get(index);
        
        String title = JOptionPane.showInputDialog(this, "Enter lesson title:", lesson.getTitle(), JOptionPane.PLAIN_MESSAGE);
        if (title == null || title.trim().isEmpty()) return;
        
        String content = JOptionPane.showInputDialog(this, "Enter lesson content:", lesson.getContent(), JOptionPane.PLAIN_MESSAGE);
        if (content == null) return;
        
        try {
            model.Lesson updated = new model.Lesson(lesson.getLessonId(), title.trim(), content.trim());
            lessonController.editLesson(course.getCourseId(), updated);
            refreshLessonList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteLesson() {
        int index = lessonList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a lesson", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        model.Lesson lesson = course.getLessons().get(index);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete: " + lesson.getTitle() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                lessonController.deleteLesson(course.getCourseId(), lesson.getLessonId());
                refreshLessonList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
