package Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;

public class AdminFeeManager extends JPanel {
    JTextField txtStudentId, txtCredit, txtRemaining, txtDateMonth;
    JTable table;
    DefaultTableModel model;
    int selectedRow = -1;  // To store the selected row for editing/deleting

    public AdminFeeManager() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(225, 227, 228));

        // Top Panel: Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Fee Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblStudentId, gbc);

        txtStudentId = new JTextField(20);
        txtStudentId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtStudentId, gbc);

        JLabel lblCredit = new JLabel("Paid Amount:");
        lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblCredit, gbc);

        txtCredit = new JTextField();
        txtCredit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtCredit, gbc);

        JLabel lblRemaining = new JLabel("Remaining Amount:");
        lblRemaining.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblRemaining, gbc);

        txtRemaining = new JTextField();
        txtRemaining.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(txtRemaining, gbc);

        JLabel lblDateMonth = new JLabel("Date/Month (YYYY-MM-DD):");
        lblDateMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(lblDateMonth, gbc);

        txtDateMonth = new JTextField();
        txtDateMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(txtDateMonth, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Payment");
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnAdd.setBackground(new Color(30, 144, 255));
        btnAdd.setForeground(Color.WHITE);

        JButton btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnClear.setBackground(new Color(220, 20, 60));
        btnClear.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Center Panel: Table
        model = new DefaultTableModel(new String[]{"Fee ID", "Student ID", "Paid", "Remaining", "Date/Month", "Status"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("View Payments"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Edit/Delete Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnEdit = new JButton("Edit Payment");
        btnEdit.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnEdit.setBackground(new Color(255, 140, 0));
        btnEdit.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Payment");
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);

        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        btnAdd.addActionListener(e -> addPayment());
        btnClear.addActionListener(e -> clearForm());
        btnEdit.addActionListener(e -> updatePayment());
        btnDelete.addActionListener(e -> deletePayment());

        // Initial call to view all payments
        viewPayments(false);

        // Row Selection Listener for Edit/Delete functionality
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
            }
        });

        add(mainPanel);
    }

    void addPayment() {
        String studentId = txtStudentId.getText();
        String credit = txtCredit.getText();
        String remaining = txtRemaining.getText();
        String dateMonth = txtDateMonth.getText();

        if (studentId.isEmpty() || credit.isEmpty() || remaining.isEmpty() || dateMonth.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!dateMonth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Date must be inyyyyy-MM-DD format", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int paidAmount = Integer.parseInt(credit);
            int remainingAmount = Integer.parseInt(remaining);
            String status = (remainingAmount > 0) ? "Pending" : "Paid";

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "")) {
                PreparedStatement ps = conn.prepareStatement("SELECT std_hostel_fee FROM student WHERE std_id = ?");
                ps.setString(1, studentId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String insertQuery = "INSERT INTO fee (std_id, f_credit, f_remaining, f_date_month, f_status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertPs = conn.prepareStatement(insertQuery);
                insertPs.setString(1, studentId);
                insertPs.setInt(2, paidAmount);
                insertPs.setInt(3, remainingAmount);
                insertPs.setString(4, dateMonth);
                insertPs.setString(5, status);
                int result = insertPs.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Payment added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    viewPayments(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add payment", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Paid and Remaining amounts", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void clearForm() {
        txtStudentId.setText("");
        txtCredit.setText("");
        txtRemaining.setText("");
        txtDateMonth.setText("");
    }

    private void updatePayment() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int feeId = (int) model.getValueAt(selectedRow, 0);
        // Create and show the AdminUpdateFee window
        AdminUpdateFee updateForm = new AdminUpdateFee(feeId, this);
        // The AdminUpdateFee window will now handle loading the data and performing the update.
    }

    void deletePayment() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int feeId = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this payment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "")) {
                String deleteQuery = "DELETE FROM fee WHERE f_id = ?";
                PreparedStatement ps = conn.prepareStatement(deleteQuery);
                ps.setInt(1, feeId);
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Payment deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    viewPayments(false);
                    selectedRow = -1; // Reset selection
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete payment", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void viewPayments(boolean filter) {
        model.setRowCount(0); // Clear existing data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "")) {
            String query = "SELECT * FROM fee";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("f_id"),
                        rs.getString("std_id"),
                        rs.getInt("f_credit"),
                        rs.getInt("f_remaining"),
                        rs.getString("f_date_month"),
                        rs.getString("f_status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Fee Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
            AdminFeeManager feeManager = new AdminFeeManager();
            frame.add(feeManager);
            frame.setVisible(true);
        });
    }
}