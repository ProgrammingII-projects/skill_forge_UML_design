package view;

import controller.*;
import model.*;
import model.database_manager.CourseModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CourseEditorFrame extends JFrame {
    private String instructorId;
    private CourseModel courseModel;
    private CourseController courseController;
    private model.Course existingCourse;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JButton saveButton;
    private JButton cancelButton;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color FOCUS_COLOR = PRIMARY_COLOR;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public CourseEditorFrame(String instructorId, CourseModel cm, boolean isNew, model.Course existing) {
        this.instructorId = instructorId;
        this.courseModel = cm;
        this.courseController = new CourseController(courseModel);
        this.existingCourse = existing;
        
        initializeFrame(isNew);
        createUI();
    }
    
    private void initializeFrame(boolean isNew) {
        setTitle(isNew ? "Skill Forge - Create Course" : "Skill Forge - Edit Course");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
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
        
        // Center panel (card)
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // White card container
        JPanel whiteCard = new RoundedPanel(20);
        whiteCard.setBackground(CARD_COLOR);
        whiteCard.setLayout(new BoxLayout(whiteCard, BoxLayout.Y_AXIS));
        whiteCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // Title section
        JLabel titleLabel = new JLabel(existingCourse == null ? "Create Course" : "Edit Course");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title field
        JLabel titleFieldLabel = new JLabel("Course Title");
        titleFieldLabel.setFont(LABEL_FONT);
        titleFieldLabel.setForeground(TEXT_COLOR);
        titleFieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleFieldLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        titleField = createStyledTextField();
        if (existingCourse != null) titleField.setText(existingCourse.getTitle());
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        // Description field
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(LABEL_FONT);
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(new EmptyBorder(20, 0, 8, 0));
        
        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(TEXT_COLOR);
        if (existingCourse != null) descriptionArea.setText(existingCourse.getDescription());
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scrollPane.setBorder(null);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        saveButton = createPrimaryButton("Save");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setMaximumSize(new Dimension(150, 50));
        saveButton.addActionListener(e -> saveCourse());
        
        cancelButton = createSecondaryButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setMaximumSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> dispose());
        
        // Add focus listeners
        addFocusListeners(titleField);
        descriptionArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_COLOR, 2),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        // Assemble components
        formPanel.add(titleFieldLabel);
        formPanel.add(titleField);
        formPanel.add(descLabel);
        formPanel.add(scrollPane);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(cancelButton);
        
        whiteCard.add(titleLabel);
        whiteCard.add(formPanel);
        whiteCard.add(buttonPanel);
        
        cardPanel.add(whiteCard, BorderLayout.CENTER);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_COLOR);
        return field;
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
    
    private void addFocusListeners(JComponent component) {
        component.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(FOCUS_COLOR, 2),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
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
    
    private void saveCourse() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (existingCourse == null) {
                courseController.createCourse(title, description, instructorId);
                JOptionPane.showMessageDialog(this, "Course created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                existingCourse.setTitle(title);
                existingCourse.setDescription(description);
                courseController.updateCourse(existingCourse);
                JOptionPane.showMessageDialog(this, "Course updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
