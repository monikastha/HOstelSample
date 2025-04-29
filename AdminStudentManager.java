package Admin;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminStudentManager extends JPanel {

    JTextField txtName, txtUsername, txtAddress, txtGuardianName, txtGuardianNo, txtHostelFee, txtBalance;
    JTable table;
    DefaultTableModel model;

    public AdminStudentManager() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(225, 227, 228));

        // --- Top Panel: Add Student Form ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Student"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblName, gbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtName, gbc);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblAddress, gbc);

        txtAddress = new JTextField();
        txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(txtAddress, gbc);

        JLabel lblGuardianName = new JLabel("Guardian Name:");
        lblGuardianName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblGuardianName, gbc);

        txtGuardianName = new JTextField(15);
        txtGuardianName.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(txtGuardianName, gbc);

        JLabel lblGuardianNo = new JLabel("Guardian No:");
        lblGuardianNo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(lblGuardianNo, gbc);

        txtGuardianNo = new JTextField();
        txtGuardianNo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(txtGuardianNo, gbc);

        JLabel lblHostelFee = new JLabel("Hostel Fee:");
        lblHostelFee.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(lblHostelFee, gbc);

        txtHostelFee = new JTextField();
        txtHostelFee.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 5;
        formPanel.add(txtHostelFee, gbc);

        JLabel lblBalance = new JLabel("Remaining Fee:");
        lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(lblBalance, gbc);

        txtBalance = new JTextField();
        txtBalance.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 6;
        formPanel.add(txtBalance, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Student");
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnAdd.setBackground(new Color(30, 144, 255));
        btnAdd.setForeground(Color.WHITE);

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnClear.setBackground(new Color(220, 20, 60));
        btnClear.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // --- Center Panel: Table ---
        
        model = new DefaultTableModel(new String[]{"ID", "Name", "Username", "Address", "Guardian Name", "Guardian No", "Hostel Fee", "Remaining Fee"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("View Students"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel: Edit/Delete Buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnEdit = new JButton("Edit Student");
        btnEdit.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnEdit.setBackground(new Color(255, 140, 0));
        btnEdit.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Student");
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);

        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Button Actions
        btnAdd.addActionListener(e -> addStudent());
        btnClear.addActionListener(e -> clearForm());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        viewStudent();
        setVisible(true);
    }

    void addStudent() {
        String name = txtName.getText();
        String username = txtUsername.getText();
        String address = txtAddress.getText();
        String guardianName = txtGuardianName.getText();
        String guardianNo = txtGuardianNo.getText();
        String hostelFee = txtHostelFee.getText();
        String balance = txtBalance.getText();

        if (name.isEmpty() || username.isEmpty() || hostelFee.isEmpty() || balance.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
             PreparedStatement pst = con.prepareStatement("INSERT INTO student (std_name, std_username, std_address, std_guardian_name, std_guardian_no, std_hostel_fee, std_balance) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, name);
            pst.setString(2, username);
            pst.setString(3, address);
            pst.setString(4, guardianName);
            pst.setString(5, guardianNo);
            pst.setBigDecimal(6, new java.math.BigDecimal(hostelFee));
            pst.setBigDecimal(7, new java.math.BigDecimal(balance));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");
            clearForm();
            viewStudent();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Add Error: " + ex.getMessage());
        }
    }

    void clearForm() {
        txtName.setText("");
        txtUsername.setText("");
        txtAddress.setText("");
        txtGuardianName.setText("");
        txtGuardianNo.setText("");
        txtHostelFee.setText("");
        txtBalance.setText("");
    }

    void viewStudent() {
        model.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM student")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("std_id"),
                        rs.getString("std_name"),
                        rs.getString("std_username"),
                        rs.getString("std_address"),
                        rs.getString("std_guardian_name"),
                        rs.getString("std_guardian_no"),
                        rs.getBigDecimal("std_hostel_fee"),
                        rs.getBigDecimal("std_balance")  // Added balance to the table display
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage());
        }
    }

    void editStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int studentId = (int) model.getValueAt(selectedRow, 0);
            AdminUpdateStudent updateDialog = new AdminUpdateStudent(studentId);
            updateDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    viewStudent(); // Refresh the table after the edit dialog is closed
                }
            });
            updateDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to edit!", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int studentId = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
                 PreparedStatement pst = con.prepareStatement("DELETE FROM student WHERE std_id=?")) {

                pst.setInt(1, studentId);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
                viewStudent();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!");
        }
    }

    public static void main(String[] args) {
        new AdminStudentManager();
    }
}
