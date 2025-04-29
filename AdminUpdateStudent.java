package Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;

public class AdminUpdateStudent extends JFrame {

    JTextField txtName, txtUsername, txtAddress, txtGuardianName, txtGuardianNo, txtHostelFee, txtBalance;
    int studentId;

    public AdminUpdateStudent(int studentId) {
        this.studentId = studentId;

        setTitle("Update Student");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245)); // Lighter background
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("Update Student Details", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22)); // More modern font
        lblTitle.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(lblName, gbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(txtName, gbc);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(txtUsername, gbc);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(lblAddress, gbc);

        txtAddress = new JTextField();
        txtAddress.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 3;
        mainPanel.add(txtAddress, gbc);

        JLabel lblGuardianName = new JLabel("Guardian Name:");
        lblGuardianName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(lblGuardianName, gbc);

        txtGuardianName = new JTextField(15);
        txtGuardianName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 4;
        mainPanel.add(txtGuardianName, gbc);

        JLabel lblGuardianNo = new JLabel("Guardian No:");
        lblGuardianNo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(lblGuardianNo, gbc);

        txtGuardianNo = new JTextField();
        txtGuardianNo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 5;
        mainPanel.add(txtGuardianNo, gbc);

        JLabel lblHostelFee = new JLabel("Hostel Fee:");
        lblHostelFee.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(lblHostelFee, gbc);

        txtHostelFee = new JTextField();
        txtHostelFee.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 6;
        mainPanel.add(txtHostelFee, gbc);

        JLabel lblBalance = new JLabel("Remaining Fee:");
        lblBalance.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(lblBalance, gbc);

        txtBalance = new JTextField();
        txtBalance.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 7;
        mainPanel.add(txtBalance, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(39, 174, 96)); // Green

        JButton btnCancel = new JButton("Cancel");
        styleButton(btnCancel, new Color(231, 76, 60)); // Red

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        loadStudentData();

        btnUpdate.addActionListener(e -> updateStudent());
        btnCancel.addActionListener(e -> dispose()); // Only dispose, AdminStudentManager will refresh

        setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    void loadStudentData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
             PreparedStatement pst = con.prepareStatement("SELECT * FROM student WHERE std_id=?")) {

            pst.setInt(1, studentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("std_name"));
                txtUsername.setText(rs.getString("std_username"));
                txtAddress.setText(rs.getString("std_address"));
                txtGuardianName.setText(rs.getString("std_guardian_name"));
                txtGuardianNo.setText(rs.getString("std_guardian_no"));
                txtHostelFee.setText(rs.getString("std_hostel_fee"));
                txtBalance.setText(rs.getString("std_balance"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage());
        }
    }

    void updateStudent() {
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String address = txtAddress.getText().trim();
        String guardianName = txtGuardianName.getText().trim();
        String guardianNo = txtGuardianNo.getText().trim();
        String hostelFee = txtHostelFee.getText().trim();
        String balance = txtBalance.getText().trim();

        if (name.isEmpty() || username.isEmpty() || hostelFee.isEmpty() || balance.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
             PreparedStatement pst = con.prepareStatement(
                     "UPDATE student SET std_name=?, std_username=?, std_address=?, std_guardian_name=?, std_guardian_no=?, std_hostel_fee=?, std_balance=? WHERE std_id=?")) {

            pst.setString(1, name);
            pst.setString(2, username);
            pst.setString(3, address);
            pst.setString(4, guardianName);
            pst.setString(5, guardianNo);
            pst.setBigDecimal(6, new BigDecimal(hostelFee));
            pst.setBigDecimal(7, new BigDecimal(balance));
            pst.setInt(8, studentId);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Student Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Only dispose here
            } else {
                JOptionPane.showMessageDialog(this, "Error updating student. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for Hostel Fee or Remaining Fee. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUpdateStudent(1));
    }
}