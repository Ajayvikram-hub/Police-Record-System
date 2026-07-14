package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateRecordFrame extends Frame implements ActionListener {
    private TextField recordIdField, caseNumberField, criminalNameField, crimeTypeField, locationField, officerField;
    private Choice statusChoice;
    private Button searchButton, updateButton, backButton;
    private Label messageLabel;
    
    public UpdateRecordFrame() {
        setTitle("Update Police Record");
        setSize(500, 500);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        Label titleLabel = new Label("Update Police Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(150, 30, 200, 30);
        add(titleLabel);
        
        // Record ID to search
        Label idLabel = new Label("Record ID:");
        idLabel.setBounds(50, 80, 100, 25);
        add(idLabel);
        
        recordIdField = new TextField();
        recordIdField.setBounds(160, 80, 150, 25);
        add(recordIdField);
        
        searchButton = new Button("Search");
        searchButton.setBounds(330, 80, 100, 25);
        searchButton.setBackground(new Color(255, 140, 0));
        searchButton.addActionListener(this);
        add(searchButton);
        
        // Update fields
        int yPos = 130;
        addField("Case Number:", 50, yPos, caseNumberField = new TextField());
        yPos += 40;
        addField("Criminal Name:", 50, yPos, criminalNameField = new TextField());
        yPos += 40;
        addField("Crime Type:", 50, yPos, crimeTypeField = new TextField());
        yPos += 40;
        addField("Location:", 50, yPos, locationField = new TextField());
        yPos += 40;
        addField("Officer Name:", 50, yPos, officerField = new TextField());
        yPos += 40;
        
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
        yPos += 50;
        
        updateButton = new Button("Update Record");
        updateButton.setBounds(100, yPos, 120, 35);
        updateButton.setBackground(new Color(34, 139, 34));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(this);
        add(updateButton);
        
        backButton = new Button("Back");
        backButton.setBounds(260, yPos, 100, 35);
        backButton.setBackground(new Color(70, 130, 200));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());
        add(backButton);
        
        messageLabel = new Label();
        messageLabel.setBounds(100, yPos + 50, 300, 25);
        add(messageLabel);
        
        // Initially disable update fields
        setFieldsEnabled(false);
        
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
        field.setBounds(160, y, 250, 25);
        add(field);
        field.setEnabled(false);
    }
    
    private void setFieldsEnabled(boolean enabled) {
        caseNumberField.setEnabled(enabled);
        criminalNameField.setEnabled(enabled);
        crimeTypeField.setEnabled(enabled);
        locationField.setEnabled(enabled);
        officerField.setEnabled(enabled);
        statusChoice.setEnabled(enabled);
        updateButton.setEnabled(enabled);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchRecord();
        } else if (e.getSource() == updateButton) {
            updateRecord();
        }
    }
    
    private void searchRecord() {
        String idText = recordIdField.getText();
        if (idText.isEmpty()) {
            messageLabel.setText("Please enter Record ID!");
            return;
        }
        
        String query = "SELECT * FROM police_records WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, Integer.parseInt(idText));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                caseNumberField.setText(rs.getString("case_number"));
                criminalNameField.setText(rs.getString("criminal_name"));
                crimeTypeField.setText(rs.getString("crime_type"));
                locationField.setText(rs.getString("location"));
                officerField.setText(rs.getString("officer_name"));
                statusChoice.select(rs.getString("status"));
                setFieldsEnabled(true);
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Record found! You can now update.");
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Record not found!");
                setFieldsEnabled(false);
            }
            
        } catch (SQLException | NumberFormatException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
    
    private void updateRecord() {
        String query = "UPDATE police_records SET case_number=?, criminal_name=?, crime_type=?, " +
                      "location=?, officer_name=?, status=? WHERE record_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, caseNumberField.getText());
            pstmt.setString(2, criminalNameField.getText());
            pstmt.setString(3, crimeTypeField.getText());
            pstmt.setString(4, locationField.getText());
            pstmt.setString(5, officerField.getText());
            pstmt.setString(6, statusChoice.getSelectedItem());
            pstmt.setInt(7, Integer.parseInt(recordIdField.getText()));
            
            pstmt.executeUpdate();
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Record updated successfully!");
            setFieldsEnabled(false);
            recordIdField.setText("");
            
        } catch (SQLException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
}