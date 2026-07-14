package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddRecordFrame extends Frame implements ActionListener {
    private TextField caseNumberField, criminalNameField, crimeTypeField, locationField, officerField, ageField;
    private Choice crimeDateDay, crimeDateMonth, crimeDateYear;
    private TextArea descriptionArea;
    private Choice statusChoice, genderChoice;
    private Button submitButton, resetButton, backButton;
    private Label messageLabel;
    
    public AddRecordFrame() {
        setTitle("Add New Police Record");
        setSize(600, 650);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        int yPos = 50;
        
        // Title
        Label titleLabel = new Label("Add New Criminal Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(200, 30, 250, 30);
        add(titleLabel);
        
        // Case Number
        addField("Case Number:", 50, yPos, caseNumberField = new TextField());
        yPos += 40;
        
        // Criminal Name
        addField("Criminal Name:", 50, yPos, criminalNameField = new TextField());
        yPos += 40;
        
        // Crime Type
        addField("Crime Type:", 50, yPos, crimeTypeField = new TextField());
        yPos += 40;
        
        // Crime Date
        Label dateLabel = new Label("Crime Date:");
        dateLabel.setBounds(50, yPos, 100, 25);
        add(dateLabel);
        
        crimeDateDay = new Choice();
        crimeDateMonth = new Choice();
        crimeDateYear = new Choice();
        
        for (int i = 1; i <= 31; i++) crimeDateDay.add(String.valueOf(i));
        for (int i = 1; i <= 12; i++) crimeDateMonth.add(String.valueOf(i));
        for (int i = 2020; i <= 2026; i++) crimeDateYear.add(String.valueOf(i));
        
        crimeDateDay.setBounds(160, yPos, 60, 25);
        crimeDateMonth.setBounds(230, yPos, 60, 25);
        crimeDateYear.setBounds(300, yPos, 60, 25);
        
        add(crimeDateDay);
        add(crimeDateMonth);
        add(crimeDateYear);
        yPos += 40;
        
        // Location
        addField("Location:", 50, yPos, locationField = new TextField());
        yPos += 40;
        
        // Officer Name
        addField("Officer Name:", 50, yPos, officerField = new TextField());
        yPos += 40;
        
        // Age
        addField("Age:", 50, yPos, ageField = new TextField());
        yPos += 40;
        
        // Gender
        Label genderLabel = new Label("Gender:");
        genderLabel.setBounds(50, yPos, 100, 25);
        add(genderLabel);
        
        genderChoice = new Choice();
        genderChoice.add("Male");
        genderChoice.add("Female");
        genderChoice.add("Other");
        genderChoice.setBounds(160, yPos, 100, 25);
        add(genderChoice);
        yPos += 40;
        
        // Status
        Label statusLabel = new Label("Status:");
        statusLabel.setBounds(50, yPos, 100, 25);
        add(statusLabel);
        
        statusChoice = new Choice();
        statusChoice.add("Under Investigation");
        statusChoice.add("Pending");
        statusChoice.add("Closed");
        statusChoice.add("Arrested");
        statusChoice.setBounds(160, yPos, 150, 25);
        add(statusChoice);
        yPos += 40;
        
        // Description
        Label descLabel = new Label("Description:");
        descLabel.setBounds(50, yPos, 100, 25);
        add(descLabel);
        
        descriptionArea = new TextArea(4, 30);
        descriptionArea.setBounds(160, yPos, 350, 80);
        add(descriptionArea);
        yPos += 100;
        
        // Buttons
        submitButton = new Button("Submit Record");
        submitButton.setBounds(120, yPos, 120, 35);
        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(this);
        add(submitButton);
        
        resetButton = new Button("Reset");
        resetButton.setBounds(260, yPos, 100, 35);
        resetButton.setBackground(new Color(255, 140, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(this);
        add(resetButton);
        
        backButton = new Button("Back");
        backButton.setBounds(380, yPos, 100, 35);
        backButton.setBackground(new Color(70, 130, 200));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        add(backButton);
        
        // Message Label
        messageLabel = new Label();
        messageLabel.setBounds(120, yPos + 50, 400, 25);
        add(messageLabel);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setVisible(true);
    }
    
    private void addField(String label, int x, int y, TextField field) {
        Label lbl = new Label(label);
        lbl.setBounds(x, y, 100, 25);
        add(lbl);
        field.setBounds(160, y, 350, 25);
        add(field);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (validateFields()) {
                saveRecord();
            }
        } else if (e.getSource() == resetButton) {
            resetFields();
        } else if (e.getSource() == backButton) {
            dispose();
        }
    }
    
    private boolean validateFields() {
        if (caseNumberField.getText().isEmpty() || criminalNameField.getText().isEmpty() ||
            crimeTypeField.getText().isEmpty() || locationField.getText().isEmpty() ||
            officerField.getText().isEmpty()) {
            messageLabel.setText("Please fill all required fields!");
            return false;
        }
        return true;
    }
    
    private void saveRecord() {
        String crimeDate = crimeDateYear.getSelectedItem() + "-" + 
                          crimeDateMonth.getSelectedItem() + "-" + 
                          crimeDateDay.getSelectedItem();
        
        String query = "INSERT INTO police_records (case_number, criminal_name, crime_type, crime_date, " +
                      "location, description, status, officer_name, age, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, caseNumberField.getText());
            pstmt.setString(2, criminalNameField.getText());
            pstmt.setString(3, crimeTypeField.getText());
            pstmt.setString(4, crimeDate);
            pstmt.setString(5, locationField.getText());
            pstmt.setString(6, descriptionArea.getText());
            pstmt.setString(7, statusChoice.getSelectedItem());
            pstmt.setString(8, officerField.getText());
            pstmt.setString(9, ageField.getText().isEmpty() ? null : ageField.getText());
            pstmt.setString(10, genderChoice.getSelectedItem());
            
            pstmt.executeUpdate();
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Record added successfully!");
            resetFields();
            
        } catch (SQLException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
    
    private void resetFields() {
        caseNumberField.setText("");
        criminalNameField.setText("");
        crimeTypeField.setText("");
        locationField.setText("");
        officerField.setText("");
        ageField.setText("");
        descriptionArea.setText("");
        crimeDateDay.select(0);
        crimeDateMonth.select(0);
        crimeDateYear.select(0);
        statusChoice.select(0);
        genderChoice.select(0);
    }
}