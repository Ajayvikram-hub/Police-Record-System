package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewRecordsFrame extends Frame {
    private TextArea displayArea;
    private Button refreshButton, backButton;
    
    public ViewRecordsFrame() {
        setTitle("View Police Records");
        setSize(900, 600);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        Label titleLabel = new Label("All Police Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(350, 30, 200, 30);
        add(titleLabel);
        
        displayArea = new TextArea();
        displayArea.setBounds(50, 80, 800, 450);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(displayArea);
        
        refreshButton = new Button("Refresh");
        refreshButton.setBounds(300, 550, 100, 35);
        refreshButton.setBackground(new Color(34, 139, 34));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadRecords());
        add(refreshButton);
        
        backButton = new Button("Back");
        backButton.setBounds(450, 550, 100, 35);
        backButton.setBackground(new Color(70, 130, 200));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());
        add(backButton);
        
        loadRecords();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setVisible(true);
    }
    
    private void loadRecords() {
        displayArea.setText("");
        String query = "SELECT * FROM police_records ORDER BY record_id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            StringBuilder sb = new StringBuilder();
            sb.append("=".repeat(100)).append("\n");
            sb.append(String.format("%-8s %-12s %-20s %-15s %-12s %-20s %-20s\n", 
                    "ID", "Case No", "Criminal Name", "Crime Type", "Status", "Officer", "Crime Date"));
            sb.append("=".repeat(100)).append("\n");
            
            while (rs.next()) {
                sb.append(String.format("%-8d %-12s %-20s %-15s %-12s %-20s %-12s\n",
                        rs.getInt("record_id"),
                        rs.getString("case_number"),
                        rs.getString("criminal_name"),
                        rs.getString("crime_type"),
                        rs.getString("status"),
                        rs.getString("officer_name"),
                        rs.getDate("crime_date")));
            }
            sb.append("=".repeat(100)).append("\n");
            displayArea.setText(sb.toString());
            
        } catch (SQLException ex) {
            displayArea.setText("Error loading records: " + ex.getMessage());
        }
    }
}