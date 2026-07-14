package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteRecordFrame extends Frame implements ActionListener {
    private TextField recordIdField;
    private Button deleteButton, backButton;
    private Label messageLabel;
    
    public DeleteRecordFrame() {
        setTitle("Delete Police Record");
        setSize(450, 300);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        Label titleLabel = new Label("Delete Police Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(130, 30, 200, 30);
        add(titleLabel);
        
        Label idLabel = new Label("Record ID:");
        idLabel.setBounds(80, 100, 80, 25);
        add(idLabel);
        
        recordIdField = new TextField();
        recordIdField.setBounds(170, 100, 150, 25);
        add(recordIdField);
        
        deleteButton = new Button("Delete");
        deleteButton.setBounds(100, 170, 100, 35);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(this);
        add(deleteButton);
        
        backButton = new Button("Back");
        backButton.setBounds(230, 170, 100, 35);
        backButton.setBackground(new Color(70, 130, 200));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());
        add(backButton);
        
        messageLabel = new Label();
        messageLabel.setBounds(100, 230, 300, 25);
        add(messageLabel);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            deleteRecord();
        }
    }
    
    private void deleteRecord() {
        String idText = recordIdField.getText();
        if (idText.isEmpty()) {
            messageLabel.setText("Please enter Record ID!");
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
                      "Are you sure you want to delete this record?", 
                      "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            String query = "DELETE FROM police_records WHERE record_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setInt(1, Integer.parseInt(idText));
                int rows = pstmt.executeUpdate();
                
                if (rows > 0) {
                    messageLabel.setForeground(Color.GREEN);
                    messageLabel.setText("Record deleted successfully!");
                    recordIdField.setText("");
                } else {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Record not found!");
                }
                
            } catch (SQLException | NumberFormatException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Error: " + ex.getMessage());
            }
        }
    }
}