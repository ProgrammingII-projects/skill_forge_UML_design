package view;

import model.*;
import model.database_manager.CourseModel;
import model.database_manager.UserModel;
import controller.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginFrame extends JFrame {
    private UserModel userModel;
    private CourseModel courseModel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241); // Indigo
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(31, 41, 55);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);
    private static final Color FOCUS_COLOR = PRIMARY_COLOR;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public LoginFrame(UserModel userModel, CourseModel courseModel) {
        this.userModel = userModel;
        this.courseModel = courseModel;
        
        initializeFrame();
        createUI();
    }
    
    private void initializeFrame() {
        setTitle("Skill Forge - Login");
        setSize(480, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default if system L&F fails
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
        cardPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // White card container
        JPanel whiteCard = new RoundedPanel(20);
        whiteCard.setBackground(CARD_COLOR);
        whiteCard.setLayout(new BoxLayout(whiteCard, BoxLayout.Y_AXIS));
        whiteCard.setBorder(new EmptyBorder(50, 50, 50, 50));
        whiteCard.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        
        // Title section
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Email field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(LABEL_FONT);
        emailLabel.setForeground(TEXT_COLOR);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        emailField = createStyledTextField();
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(new EmptyBorder(20, 0, 8, 0));
        
        passwordField = createStyledPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        loginButton = createPrimaryButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        signupButton = createSecondaryButton("Create Account");
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignup();
            }
        });
        
        // Add focus listeners for better UX
        addFocusListeners(emailField);
        addFocusListeners(passwordField);
        
        // Assemble components
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(signupButton);
        
        whiteCard.add(titleLabel);
        whiteCard.add(subtitleLabel);
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
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
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
    
    // Custom rounded panel class
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
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            AuthController authController = new AuthController(userModel);
            User user = authController.login(email, password);
            
            dispose();
            
            if ("instructor".equalsIgnoreCase(user.getRole())) {
                new InstructorDashboardFrame(user, userModel, courseModel).setVisible(true);
            } else {
                new StudentDashboardFrame(user, userModel, courseModel).setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openSignup() {
        SignupFrame signupFrame = new SignupFrame(userModel, courseModel);
        signupFrame.setVisible(true);
    }
}
