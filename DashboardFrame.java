package src;

import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends Frame implements ActionListener {
    private String username;
    private Button addButton, viewButton, updateButton, deleteButton, searchButton, logoutButton;
    
    public DashboardFrame(String username) {
        this.username = username;
        setTitle("Police Record System - Dashboard");
        setSize(600, 500);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        // Welcome Label
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBounds(200, 50, 300, 30);
        add(welcomeLabel);
        
        Label titleLabel = new Label("Police Record Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(150, 100, 300, 25);
        add(titleLabel);
        
        // Buttons
        addButton = createButton("Add New Record", 150, 160);
        viewButton = createButton("View All Records", 150, 210);
        updateButton = createButton("Update Record", 150, 260);
        deleteButton = createButton("Delete Record", 150, 310);
        searchButton = createButton("Search Record", 150, 360);
        logoutButton = createButton("Logout", 150, 410);
        
        add(addButton);
        add(viewButton);
        add(updateButton);
        add(deleteButton);
        add(searchButton);
        add(logoutButton);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setVisible(true);
    }
    
    private Button createButton(String text, int x, int y) {
        Button button = new Button(text);
        button.setBounds(x, y, 300, 40);
        button.setBackground(new Color(70, 130, 200));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(this);
        return button;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            new AddRecordFrame();
        } else if (e.getSource() == viewButton) {
            new ViewRecordsFrame();
        } else if (e.getSource() == updateButton) {
            new UpdateRecordFrame();
        } else if (e.getSource() == deleteButton) {
            new DeleteRecordFrame();
        } else if (e.getSource() == searchButton) {
            new SearchRecordFrame();
        } else if (e.getSource() == logoutButton) {
            new LoginFrame();
            dispose();
        }
    }
}