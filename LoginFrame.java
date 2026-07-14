package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends Frame implements ActionListener {
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton, clearButton;
    private Label messageLabel;
    
    public LoginFrame() {
        setTitle("Police Record Management System - Login");
        setSize(500, 400);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        // Title Label
        Label titleLabel = new Label("Police Record Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(100, 50, 300, 30);
        add(titleLabel);
        
        // Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(100, 120, 80, 25);
        add(usernameLabel);
        
        usernameField = new TextField();
        usernameField.setBounds(200, 120, 180, 25);
        add(usernameField);
        
        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(100, 170, 80, 25);
        add(passwordLabel);
        
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        passwordField.setBounds(200, 170, 180, 25);
        add(passwordField);
        
        // Buttons
        loginButton = new Button("Login");
        loginButton.setBounds(120, 230, 100, 35);
        loginButton.setBackground(new Color(70, 130, 200));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        add(loginButton);
        
        clearButton = new Button("Clear");
        clearButton.setBounds(250, 230, 100, 35);
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(this);
        add(clearButton);
        
        // Message Label
        messageLabel = new Label();
        messageLabel.setBounds(150, 290, 300, 25);
        messageLabel.setForeground(Color.RED);
        add(messageLabel);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password!");
                return;
            }
            
            if (authenticateUser(username, password)) {
                messageLabel.setText("Login Successful!");
                new DashboardFrame(username);
                dispose();
            } else {
                messageLabel.setText("Invalid username or password!");
            }
        } else if (e.getSource() == clearButton) {
            usernameField.setText("");
            passwordField.setText("");
            messageLabel.setText("");
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}