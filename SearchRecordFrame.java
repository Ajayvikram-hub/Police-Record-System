package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchRecordFrame extends Frame implements ActionListener {
    private Choice searchType;
    private TextField searchField;
    private TextArea resultArea;
    private Button searchButton, backButton;
    
    public SearchRecordFrame() {
        setTitle("Search Police Record");
        setSize(800, 500);
        setLayout(null);
        setBackground(new Color(240, 248, 255));
        
        Label titleLabel = new Label("Search Police Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(280, 30, 200, 30);
        add(titleLabel);
        
        Label typeLabel = new Label("Search by:");
        typeLabel.setBounds(80, 80, 80, 25);
        add(typeLabel);
        
        searchType = new Choice();
        searchType.add("Case Number");
        searchType.add("Criminal Name");
        searchType.add("Crime Type");
        searchType.setBounds(170, 80, 120, 25);
        add(searchType);
        
        searchField = new TextField();
        searchField.setBounds(310, 80, 200, 25);
        add(searchField);
        
        searchButton = new Button("Search");
        searchButton.setBounds(530, 80, 100, 25);
        searchButton.setBackground(new Color(34, 139, 34));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(this);
        add(searchButton);
        
        resultArea = new TextArea();
        resultArea.setBounds(50, 130, 700, 300);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(resultArea);
        
        backButton = new Button("Back");
        backButton.setBounds(350, 450, 100, 35);
        backButton.setBackground(new Color(70, 130, 200));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());
        add(backButton);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            performSearch();
        }
    }
    
    private void performSearch() {
        String searchValue = searchField.getText();
        if (searchValue.isEmpty()) {
            resultArea.setText("Please enter search value!");
            return;
        }
        
        String column = "";
        switch (searchType.getSelectedItem()) {
            case "Case Number":
                column = "case_number";
                break;
            case "Criminal Name":
                column = "criminal_name";
                break;
            case "Crime Type":
                column = "crime_type";
                break;
        }
        
        String query = "SELECT * FROM police_records WHERE " + column + " LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + searchValue + "%");
            ResultSet rs = pstmt.executeQuery();
            
            StringBuilder sb = new StringBuilder();
            sb.append("Search Results:\n");
            sb.append("=".repeat(100)).append("\n");
            sb.append(String.format("%-8s %-12s %-20s %-15s %-12s %-20s %-20s\n",
                    "ID", "Case No", "Criminal Name", "Crime Type", "Status", "Officer", "Location"));
            sb.append("=".repeat(100)).append("\n");
            
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append(String.format("%-8d %-12s %-20s %-15s %-12s %-20s %-20s\n",
                        rs.getInt("record_id"),
                        rs.getString("case_number"),
                        rs.getString("criminal_name"),
                        rs.getString("crime_type"),
                        rs.getString("status"),
                        rs.getString("officer_name"),
                        rs.getString("location")));
            }
            
            if (!found) {
                sb.append("No records found!\n");
            }
            sb.append("=".repeat(100));
            resultArea.setText(sb.toString());
            
        } catch (SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }
}